# chessBoss

A bot for playing chess on chess.com

### Getting started
 - A recent version of Java 7 is required
 - Library used: Robot
 - Stockfish 8 is required, and the path for the binary has to be set in StockfishInterface.java
 
## Example

This program auto-recognize the chess.com chessboard, the only thing needed is to start the program and have the chessboard
on screen at the same time ! The bot will then read the chessboard, ask stockfish for the best move to play and play it itself.

Some settings can be made through the Main.java file, like the bot speed, the random factors making the speed vary,... This project
still need a nice graphic interface to be complete, maybe I will add it someday.

[Example video](https://www.youtube.com/watch?v=KRxwmpGqLFA)

## Useful settings in Main.java

boolean capture: if true then the bot will capture each piece as its basis for further recognition (and store it)
playColor.color couleurDeJeu: if null then the bot will try to auto detect the player's color (it looks for the left tower), but
it can also be set on white or black manually


