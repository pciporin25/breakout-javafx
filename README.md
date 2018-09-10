game
====

This project implements the game of Breakout.

Name: Peter Ciporin

### Timeline

Start Date: 9/3/18

Finish Date: 9/9/18

Hours Spent: 30

### Resources Used
* Professor Duvall's resources from Lab
* https://pixabay.com/en/boat-tug-sea-ocean-ship-toy-red-3346862/ (Added extra inner tube)
* https://pixabay.com/en/water-sea-atlantic-ocean-wave-1555170/
* https://pixabay.com/en/bora-bora-french-polynesia-sunset-685303/
* https://pixabay.com/en/indian-ocean-the-indian-ocean-sea-3630244/
* https://pixabay.com/en/arctic-ice-floating-iceberg-water-1031106/
* https://pixabay.com/en/mountains-ice-bergs-antarctica-berg-482689/
* https://pixabay.com/en/underwater-blue-ocean-sea-diving-802092/
* https://stackoverflow.com/questions/34514694/display-variable-value-in-text-javafx
* https://gist.github.com/jewelsea/1588531 (reference for splash screen)


### Running the Program

Main class: BreakoutGameManager.java

Data files needed: Everything in resources directory

Key/Mouse inputs: Arrow keys to move raft, '/' and 'z' to jump raft, spacebar to start ball movement, click to start secret level after game over

Cheat keys: 'S' to go to Game Over screen (clicking starts the secret level), 'A' to destroy blocks with one hit, 'L' to add a life, 'P' to generate a power-up, 'C' to clear all blocks in current level

Known Bugs:
When an extra ball is generated, collisions are sometimes not detected with a few specific bricks.

Extra credit: 


### Notes
While I implemented nearly everything I mentioned in my plan, I will note a few areas where my project fell short:
 * My 'satellite' block was an ambitious idea but proved difficult to implement in practice
 * While the paddle cannot jump in the air (which would have been rather useless), instead you can jump it from side to side across the screen
 * I decided against including a timer in my game since I believed it would be extraneous, and so any mention of that in my plan was not implemented
 * I did not implement the musical cheat key, since it has no use for debugging

### Impressions
Overall, I found this project challenging but rewarding.  While in hindsight I wish I mad made some alternative architectural decisions (i.e. having my 
Level class extend the Scene class, better separating my game logic into distinct modules), I am pleased with the outcome of my project.

