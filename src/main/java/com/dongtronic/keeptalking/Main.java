package com.dongtronic.keeptalking;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Main {

    private static TextBox passwordTextBox;
    private static TextBox playerCountTextBox;
    private static TextBox thisPlayerTextBox;
    private static ComboBox<String> manualVersionComboBox;
    private static WindowBasedTextGUI textGUI;
    private static TextBox manualPages;
    private static TextBox introPages;
    private static Pattern shortNumberPattern = Pattern.compile("[0-9]{1,2}");
    private static Pattern longNumberPattern = Pattern.compile("[0-9]{1,3}");

    public static void main(String[] args) {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = null;

        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();

            textGUI = new MultiWindowTextGUI(screen, TextColor.ANSI.BLUE);
            final Window window = new BasicWindow("Keep Talking and Nobody Explodes - Manual Distributor");
            Panel contentPanel = new Panel(new GridLayout(2));
            GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(3);


            /*
             * Line 1 (Introduction)
             */
            Label title = new Label("Please enter the relevant data");
            title.setLayoutData(GridLayout.createLayoutData(
                    GridLayout.Alignment.BEGINNING, // Horizontal alignment in the grid cell if the cell is larger than the component's preferred size
                    GridLayout.Alignment.BEGINNING, // Vertical alignment in the grid cell if the cell is larger than the component's preferred size
                    true,       // Give the component extra horizontal space if available
                    false,        // Give the component extra vertical space if available
                    2,                  // Horizontal span
                    1));                  // Vertical span
            contentPanel.addComponent(title);

            /*
             * Line 2 (Password)
             */
            contentPanel.addComponent(new Label("Password"));

            passwordTextBox = new TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER));
            contentPanel.addComponent(passwordTextBox);

            /*
             * Line 3 (Player count)
             */
            contentPanel.addComponent(new Label("Number of Players"));

            playerCountTextBox = new TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER));
            playerCountTextBox.setValidationPattern(shortNumberPattern);
            contentPanel.addComponent(playerCountTextBox);

            /*
             * Line 4 (Current player)
             */
            contentPanel.addComponent(new Label("Which player are you?"));

            thisPlayerTextBox = new TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER));
            thisPlayerTextBox.setValidationPattern(shortNumberPattern);
            contentPanel.addComponent(thisPlayerTextBox);

            /*
             * Line 5 (Manual version)
             */
            contentPanel.addComponent(new Label("Manual Version"));
            List<String> manualVersionsAsString = new ArrayList<>();

            for (ManualVersion version : ManualVersion.values()) {
                manualVersionsAsString.add(version.getName());
            }

            manualVersionComboBox = new ComboBox<>(manualVersionsAsString);
            manualVersionComboBox.setReadOnly(true);
            manualVersionComboBox.setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1));
            contentPanel.addComponent(manualVersionComboBox);

            /*
             * Line 6 (Generate button)
             */
            contentPanel.addComponent(new EmptySpace().setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));
            contentPanel.addComponent(new Button("Generate", Main::generate)
                    .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));

            /*
             * Line 7 (Empty)
             */
            contentPanel.addComponent(
                    new EmptySpace()
                            .setLayoutData(
                                    GridLayout.createHorizontallyFilledLayoutData(2)));

            /*
             * Line 8 (Separator)
             */
            contentPanel.addComponent(
                    new Separator(Direction.HORIZONTAL)
                            .setLayoutData(
                                    GridLayout.createHorizontallyFilledLayoutData(2)));

            /*
             * Line 9 (Close button)
             */
            contentPanel.addComponent(
                    new Button("Close", window::close).setLayoutData(
                            GridLayout.createHorizontallyEndAlignedLayoutData(2)));


            window.setComponent(contentPanel);
            textGUI.addWindowAndWait(window);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void validate() {
        if (playerCountTextBox.getText().length() == 0) {
            throw new IllegalArgumentException("Please enter a player count");
        }

        if (thisPlayerTextBox.getText().length() == 0) {
            throw new IllegalArgumentException("Please enter your player number");
        }

        int playerCount = Integer.parseInt(playerCountTextBox.getText());
        int thisPlayer = Integer.parseInt(thisPlayerTextBox.getText());

        if (playerCount > 99) {
            throw new IllegalArgumentException("There may be no more than 99 players");
        }

        if (thisPlayer > playerCount) {
            throw new IllegalArgumentException(String.format("You cannot be player %s when there are only %s players.", thisPlayer, playerCount));
        }
    }

    private static void generate() {
        try {
            validate();

            String password = passwordTextBox.getText();
            int playerCount = Integer.parseInt(playerCountTextBox.getText());
            int thisPlayer = Integer.parseInt(thisPlayerTextBox.getText());
            String versionName = manualVersionComboBox.getText();
            ManualVersion version = ManualVersion.fromName(versionName);
            assert version != null;

            PageChooser chooser = new PageChooser(playerCount, version.getPages(), password);

            if (version == ManualVersion.MANUAL) {
                customManual();
                int pageCount = Integer.parseInt(manualPages.getText());
                chooser.setPages(pageCount);
            }

            Map<Integer, ArrayList<Integer>> result = chooser.choose();

            ArrayList<Integer> pages = result.get(thisPlayer);

            //TODO: Actually generate a PDF

            MessageDialog.showMessageDialog(textGUI, "Result", "You must use pages " + pages, MessageDialogButton.OK);
        } catch (IllegalArgumentException ex) {
            MessageDialog.showMessageDialog(textGUI, "Error", ex.getMessage(), MessageDialogButton.OK);
        }
    }

    private static void customManual() {
        final Window window = new BasicWindow("Custom Manual");
        Panel contentPanel = new Panel(new GridLayout(2));
        GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
        gridLayout.setHorizontalSpacing(3);

        /*
         * Line 1 (Introduction)
         */
        Label title = new Label("Please enter your manual details");
        title.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.BEGINNING, // Horizontal alignment in the grid cell if the cell is larger than the component's preferred size
                GridLayout.Alignment.BEGINNING, // Vertical alignment in the grid cell if the cell is larger than the component's preferred size
                true,       // Give the component extra horizontal space if available
                false,        // Give the component extra vertical space if available
                2,                  // Horizontal span
                1));                  // Vertical span
        contentPanel.addComponent(title);

        /*
         * Line 2 (Empty)
         */
        contentPanel.addComponent(
                new EmptySpace()
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)));

        /*
         * Line 3 (Page count)
         */
        contentPanel.addComponent(new Label("How many pages does your manual have?"));

        manualPages = new TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER));
        manualPages.setValidationPattern(longNumberPattern);
        contentPanel.addComponent(manualPages);

        /*
         * Line 4 (Empty)
         */
        contentPanel.addComponent(
                new EmptySpace()
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)));

        /*
         * Line 5 (Intro pages)
         */
        contentPanel.addComponent(new Label("How many intro pages does your manual have?"));

        introPages = new TextBox().setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER));
        introPages.setValidationPattern(shortNumberPattern);
        contentPanel.addComponent(introPages);

        /*
         * Line 6 (Empty)
         */
        contentPanel.addComponent(
                new EmptySpace()
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)));

        /*
         * Line 7 (Close button)
         */
        contentPanel.addComponent(
                new Button("Done", window::close).setLayoutData(
                        GridLayout.createHorizontallyEndAlignedLayoutData(2)));

        window.setComponent(contentPanel);
        textGUI.addWindowAndWait(window);
    }
}