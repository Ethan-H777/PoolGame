# To run the application, please use:
gradle run

# Extension Features
Configurable Pockets, more Coloured Balls, Difficulty Level, Time, Score, Undo and Cheat.

# Game Notes
- User will be required to select a difficulty level to start the game.
- User can click "Switch Level" button to change difficulty level.
- User can undo a shot and a cheat action by "Undo" button, but not able to undo after win.
- User can remove balls in selected colour by clicking corresponding cheat buttons such as "Red Balls".

# Config Notes
When entering config details, please note the following restrictions:
- Friction must be value between 0 - 1 (not inclusive). [Would recommend switching between 0.95, 0.9, 0.85 to see changes].
- Ball X and Y positions must be within the size of the table width and length, including the ball radius (10).
- Ball colours must be Paint string values as expected.
- The filenames of three config.json must not be modified, only the values inside the file can be changed.

# Design Patterns
Prototype:
Performance.java, Score.java, Timer.java and GameManager.java
PocketStrategy.java, BallStrategy.java, BlueStrategy.java and BlackStrategy.java

Memento:
CareTaker.java, GameMemento.java, BallMemento.java, ScoreMemento.java,
TimerMemento.java, Ball.java, Timer.java and Score.java

Observer:
Timer.java, Score.java, Performance.java, Observer.java, ScoreObserver.java and TimObserver.java

Factory:
BallReader.java, TableReader.java, PocketReader.java, Reader.java,
BallReaderFactory.java, PocketReaderFactory.java, TableReaderFactory.java and ReaderFactory.java

Builder:
BallReader.java, PocketReader.java, BallBuilder.java, PocketBuilder.java, PoolBallBuilder.java,
PoolPocketBuilder.java, Ball.java and Pocket.java

Strategy:
Ball.java, PocketStrategy.java, BallStrategy.java, BlueStrategy.java and BlackStrategy.java



