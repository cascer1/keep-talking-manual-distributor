package com.dongtronic.keeptalking

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.dialogs.MessageDialog
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import java.io.File
import java.io.FileNotFoundException

import java.io.IOException
import java.util.ArrayList
import java.util.regex.Pattern

object Main {

    private var passwordTextBox: TextBox? = null
    private var playerCountTextBox: TextBox? = null
    private var thisPlayerTextBox: TextBox? = null
    private var manualVersionComboBox: ComboBox<String>? = null
    private var textGUI: WindowBasedTextGUI? = null
    private var manualPages: TextBox? = null
    private var introPages: TextBox? = null
    private var manualFileName: TextBox? = null
    private var skipIntroPages: CheckBox? = null
    private var randomPlayers: CheckBox? = null
    private val shortNumberPattern = Pattern.compile("[0-9]{1,2}")
    private val longNumberPattern = Pattern.compile("[0-9]{1,3}")

    @JvmStatic
    fun main(args: Array<String>) {
        val terminalFactory = DefaultTerminalFactory()
        var screen: Screen? = null

        try {
            screen = terminalFactory.createScreen()
            screen!!.startScreen()

            textGUI = MultiWindowTextGUI(screen, TextColor.ANSI.BLUE)
            val window = BasicWindow("Keep Talking and Nobody Explodes - Manual Distributor")
            val contentPanel = Panel(GridLayout(2))
            val gridLayout = contentPanel.layoutManager as GridLayout
            gridLayout.horizontalSpacing = 3


            /*
             * Line 1 (Introduction)
             */
            val title = Label("Please enter the relevant data")
            title.layoutData = GridLayout.createLayoutData(
                    GridLayout.Alignment.BEGINNING, // Horizontal alignment in the grid cell if the cell is larger than the component's preferred size
                    GridLayout.Alignment.BEGINNING, // Vertical alignment in the grid cell if the cell is larger than the component's preferred size
                    true, // Give the component extra horizontal space if available
                    false, // Give the component extra vertical space if available
                    2, // Horizontal span
                    1)                  // Vertical span
            contentPanel.addComponent(title)

            /*
             * Line 2 (Password)
             */
            contentPanel.addComponent(Label("Password"))

            passwordTextBox = TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER))
            contentPanel.addComponent(passwordTextBox!!)

            /*
             * Line 3 (Player count)
             */
            contentPanel.addComponent(Label("Number of Players"))

            playerCountTextBox = TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER))
            playerCountTextBox!!.setValidationPattern(shortNumberPattern)
            contentPanel.addComponent(playerCountTextBox!!)

            /*
             * Line 4 (Current player)
             */
            contentPanel.addComponent(Label("Which player are you?"))

            thisPlayerTextBox = TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER))
            thisPlayerTextBox!!.setValidationPattern(shortNumberPattern)
            contentPanel.addComponent(thisPlayerTextBox!!)

            /*
             * Line 5 (Manual version)
             */
            contentPanel.addComponent(Label("Manual Version"))
            val manualVersionsAsString = ArrayList<String>()

            for (version in ManualVersion.values()) {
                manualVersionsAsString.add(version.manualName)
            }

            manualVersionComboBox = ComboBox(manualVersionsAsString)
            manualVersionComboBox!!.isReadOnly = true
            manualVersionComboBox!!.layoutData = GridLayout.createHorizontallyFilledLayoutData(1)
            contentPanel.addComponent(manualVersionComboBox!!)

            /*
             * Line 6 (Empty)
             */
            contentPanel.addComponent(
                    EmptySpace()
                            .setLayoutData(
                                    GridLayout.createHorizontallyFilledLayoutData(2)))

            /*
             * Line 7 (Misc)
             */
            skipIntroPages = CheckBox().setLabel("Skip intro pages").setChecked(true)
            randomPlayers = CheckBox().setLabel("Uneven page distribution")
            contentPanel.addComponent(skipIntroPages!!)
            contentPanel.addComponent(randomPlayers!!)

            /*
             * Line 8 (Generate button)
             */
            contentPanel.addComponent(EmptySpace().setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)))
            contentPanel.addComponent(Button("Generate", Runnable { generate() })
                    .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)))

            /*
             * Line 9 (Empty)
             */
            contentPanel.addComponent(
                    EmptySpace()
                            .setLayoutData(
                                    GridLayout.createHorizontallyFilledLayoutData(2)))

            /*
             * Line 10 (Separator)
             */
            contentPanel.addComponent(
                    Separator(Direction.HORIZONTAL)
                            .setLayoutData(
                                    GridLayout.createHorizontallyFilledLayoutData(2)))

            /*
             * Line 11 (Close button)
             */
            contentPanel.addComponent(
                    Button("Close", Runnable { window.close() }).setLayoutData(
                            GridLayout.createHorizontallyEndAlignedLayoutData(2)))


            window.component = contentPanel
            textGUI!!.addWindowAndWait(window)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun validate() {
        require(playerCountTextBox!!.text.isNotEmpty()) { "Please enter a player count" }

        require(thisPlayerTextBox!!.text.isNotEmpty()) { "Please enter your player number" }

        val playerCount = Integer.parseInt(playerCountTextBox!!.text)
        val thisPlayer = Integer.parseInt(thisPlayerTextBox!!.text)

        require(playerCount <= 99) { "There may be no more than 99 players" }

        require(thisPlayer <= playerCount) { String.format("You cannot be player %s when there are only %s players.", thisPlayer, playerCount) }
    }

    private fun generate() {
        try {
            validate()

            val password = passwordTextBox!!.text
            val playerCount = Integer.parseInt(playerCountTextBox!!.text)
            val thisPlayer = Integer.parseInt(thisPlayerTextBox!!.text)
            val versionName = manualVersionComboBox!!.text
            val version = ManualVersion.fromName(versionName)!!

            val chooser = PageChooser(playerCount, version.pages, version.introPages, randomPlayers!!.isChecked, skipIntroPages!!.isChecked, password)

            if (version === ManualVersion.MANUAL) {
                customManual()
                val pageCount = Integer.parseInt(manualPages!!.text)
                val introPageCount = Integer.parseInt(introPages!!.text)
                val customFileName = manualFileName!!.text
                chooser.pages = pageCount
                chooser.introPages = introPageCount
                version.filename = customFileName
            }

            chooser.validatePlayerCount()

            val result = chooser.choose()
            val pages = result[thisPlayer]

            if (pages!!.isEmpty()) {
                MessageDialog.showMessageDialog(textGUI, "Notice",
                        "No pages were selected for you.\nTry again with fewer players.", MessageDialogButton.OK)
                return
            }

            val generatedFile = PdfSplitter.splitFile(version, pages)

            MessageDialog.showMessageDialog(textGUI, "Result", "Your manual has been generated. \n" +
                    generatedFile.absolutePath)

            MessageDialog.showMessageDialog(textGUI, "Result", "You must use pages $pages", MessageDialogButton.OK)
        } catch (ex: IllegalArgumentException) {
            MessageDialog.showMessageDialog(textGUI, "Error", ex.message, MessageDialogButton.OK)
        } catch (ex: FileNotFoundException) {
            MessageDialog.showMessageDialog(textGUI, "Error", ex.message, MessageDialogButton.OK)
        }

    }

    private fun customManual() {
        val window = BasicWindow("Custom Manual")
        val contentPanel = Panel(GridLayout(2))
        val gridLayout = contentPanel.layoutManager as GridLayout
        gridLayout.horizontalSpacing = 3

        /*
         * Line 1 (Introduction)
         */
        val title = Label("Please enter your manual details")
        title.layoutData = GridLayout.createLayoutData(
                GridLayout.Alignment.BEGINNING, // Horizontal alignment in the grid cell if the cell is larger than the component's preferred size
                GridLayout.Alignment.BEGINNING, // Vertical alignment in the grid cell if the cell is larger than the component's preferred size
                true, // Give the component extra horizontal space if available
                false, // Give the component extra vertical space if available
                2, // Horizontal span
                1)                  // Vertical span
        contentPanel.addComponent(title)

        /*
         * Line 2 (Empty)
         */
        contentPanel.addComponent(
                EmptySpace()
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)))

        /*
         * Line 3 (Page count)
         */
        contentPanel.addComponent(Label("How many pages does your manual have?"))

        manualPages = TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER))
        manualPages!!.setValidationPattern(longNumberPattern)
        contentPanel.addComponent(manualPages!!)

        /*
         * Line 4 (Empty)
         */
        contentPanel.addComponent(
                EmptySpace()
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)))

        /*
         * Line 5 (Intro pages)
         */
        contentPanel.addComponent(Label("How many intro pages does your manual have?"))

        introPages = TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER))
        introPages!!.setValidationPattern(shortNumberPattern)
        contentPanel.addComponent(introPages!!)

        /*
         * Line 6 (Empty)
         */
        contentPanel.addComponent(
                EmptySpace()
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)))

        /*
         * Line 7 (Filename)
         */

        contentPanel.addComponent(Label("What is the name of your manual file?"))

        manualFileName = TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER))
        contentPanel.addComponent(manualFileName!!)

        contentPanel.addComponent(Label("You must place the file in the same directory as this tool."),
                GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER, true, false, 2, 1))

        /*
         * Line 8 (Empty)
         */
        contentPanel.addComponent(
                EmptySpace()
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)))

        /*
         * Line 9 (Close button)
         */
        contentPanel.addComponent(
                Button("Done", Runnable { window.close() }).setLayoutData(
                        GridLayout.createHorizontallyEndAlignedLayoutData(2)))

        window.component = contentPanel
        textGUI!!.addWindowAndWait(window)
    }
}