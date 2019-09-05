# keep Talking and Nobody Explodes manual distributor

This tool takes the Bomb Defusal Manual for Keep Talking and Nobody Explodes, and randomly distributes the pages among players.

Obviously, it is infinitely more fun to play the game in person, huddled around a table in a dimly lit room frantically passing manual pages to each other while the person defusing the bomb is hurridly calling out numbers and colors and port configurations. 
However, it's not always possible to gather a group of friends together around a table. 
For that situation we created this tool.

![demo gif](demo.gif)


## How to execute the tool

**You must have Java 11 or newer installed to use this tool.**

The simple way of running the tool is to use one of the included scripts. However, you can run the jar file directly if you prefer.

To run the tool, simply execute the jar file (`java -jar keeptalking.jar`)  
**Note:** On Windows, you have to use `javaw` instead: `javaw -jar keeptalking.jar`

## How to use the tool

1. Enter your password, all players must use the same password
2. Enter how many players there are
3. Enter which player you are (to ensure you get your own unique set of pages)
4. Select your manual version, or enter your own values
5. Choose whether you want to skip intro pages (otherwise, players might get pages with no useful information on them)
6. Choose whether you want to randomize the page count per player (this means one player can have more pages than another player)
7. Click Generate

The tool will generate a PDF file for you in the directory from where you execute it.

## Using custom manuals

The tool supports custom manual files (in PDF format). 
To use a custom manual, select the `Other` option. 
After you click the Generate button, the tool will ask you three questions:

1. The amount of pages your file has. This is the total number of pages of the file.
2. The amount of intro pages your file has. This is the amount of pages at the beginning of the file that don't contain any useful information. (e.g: a title page)
3. The name of your file. This is the full filename (e.g: `manual.pdf`) of the manual file. It must be located in the directory from where you are running the tool.

