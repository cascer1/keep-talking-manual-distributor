# keep Talking and Nobody Explodes manual distributor

This tool takes the Bomb Defusal Manual for Keep Talking and Nobody Explodes, and randomly distributes the pages among players.

Obviously, it is infinitely more fun to play the game in person, huddled around a table in a dimly lit room frantically passing manual pages to each other while the person defusing the bomb is hurridly calling out numbers and colors and port configurations. 
However, it's not always possible to gather a group of friends together around a table. 
For that situation we created this tool.

## How to execute the tool

**You must have Java 11 or newer installed to use this tool.**

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

The tool will generate a PDF file for you in the directory where it is located.