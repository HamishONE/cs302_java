## COMPSYS 302 Group 20

Welcome to Hamish and Roman's git repo for their COMPSYS 302 Java project.

This is an offline Java game based on the 1980s Atari Warlords game.

#### Build instructions

* Run the game: `gradle run`
* Run the tests: `gradle test`

##### Note for systems other than the university linux system:

* Gradle and Java 8 including JavaFX are required.
* If you do not have Gradle installed you can replace 'gradle' with 'gradlew'. This will download the
	correct version of gradle for you from the internet. On linux `chmod +x gradlew` will be required.
* Gradle will connect to Maven Central to download JUnit as required.

#### How to play the game

* Operate the menu using the up/down arrow keys, Enter to select an option and Esc to move back a level.
* Once the game starts each player has one corner of the screen. They have a pair of keys to move their
	paddle left and right to deflect the ball and prevent their walls being destroyed and their warlord
	killed.
* All other corner will be controlled by AI players.

##### Key Mappings:

* Player 1 (top left): Left/right arrow keys.
* Player 2 (top right): 'A' and 'D' keys.
* Player 3 (bottom left): '4' and '6' keys in the main part of the keyboard.
* Player 4 (bottom right): '4' and '6' keys in the number pad.
