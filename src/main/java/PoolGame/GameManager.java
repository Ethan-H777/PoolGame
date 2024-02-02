package PoolGame;

import PoolGame.Memento.*;
import PoolGame.objects.*;
import java.util.ArrayList;

import PoolGame.observer.ScoreObserver;
import PoolGame.observer.TimeObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.paint.Paint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import javafx.util.Duration;
import javafx.util.Pair;

/**
 * Controls the game interface; drawing objects, handling logic and collisions.
 */
public class GameManager {
    private Table table;
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private Line cue;
    private boolean cueSet = false;
    private boolean cueActive = false;
    private boolean winFlag = false;
    private Score score;
    private int tickCount = 0;
    private Timer timer;
    private CareTaker careTaker;

    private final double TABLEBUFFER = Config.getTableBuffer();
    private final double TABLEEDGE = Config.getTableEdge();
    private final double FORCEFACTOR = 0.1;

    private Scene scene;
    private GraphicsContext gc;
    private Pane pane;

    /**
     * Initialises timeline and cycle count.
     */
    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Builds GameManager properties such as initialising pane, canvas,
     * graphicscontext, and setting events related to clicks.
     */
    public void buildManager() {
        pane = new Pane();
        setClickEvents(pane);
        this.scene = new Scene(pane, table.getxLength() + TABLEBUFFER * 2 + 100, table.getyLength() + TABLEBUFFER * 2);
        Canvas canvas = new Canvas(table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);

        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        setUpTimeAndScore();
        setUpCheat();

        careTaker = new CareTaker();

    }

    /**
     * Draws all relevant items - table, cue, balls, pockets - onto Canvas.
     * Used Exercise 6 as reference.
     */
    private void draw() {
        tick();

        // Fill in background
        gc.setFill(Paint.valueOf("white"));
        gc.fillRect(0, 0, table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);

        // Fill in edges
        gc.setFill(Paint.valueOf("brown"));
        gc.fillRect(TABLEBUFFER - TABLEEDGE, TABLEBUFFER - TABLEEDGE, table.getxLength() + TABLEEDGE * 2,
                table.getyLength() + TABLEEDGE * 2);

        // Fill in Table
        gc.setFill(table.getColour());
        gc.fillRect(TABLEBUFFER, TABLEBUFFER, table.getxLength(), table.getyLength());

        // Fill in Pockets
        for (Pocket pocket : table.getPockets()) {
            gc.setFill(Paint.valueOf("black"));
            gc.fillOval(pocket.getxPos() - pocket.getRadius(), pocket.getyPos() - pocket.getRadius(),
                    pocket.getRadius() * 2, pocket.getRadius() * 2);
        }

        // Cue
        if (this.cue != null && cueActive) {
            gc.strokeLine(cue.getStartX(), cue.getStartY(), cue.getEndX(), cue.getEndY());
        }

        for (Ball ball : balls) {
            if (ball.isActive()) {
                gc.setFill(ball.getColour());
                gc.fillOval(ball.getxPos() - ball.getRadius(),
                        ball.getyPos() - ball.getRadius(),
                        ball.getRadius() * 2,
                        ball.getRadius() * 2);
            }

        }

        // Win
        if (winFlag) {
            gc.setStroke(Paint.valueOf("white"));
            gc.setFont(new Font("Impact", 80));
            gc.strokeText("Win and bye", table.getxLength() / 2 + TABLEBUFFER - 180,
                    table.getyLength() / 2 + TABLEBUFFER);

            timer.stop();
        }

    }

    /**
     * Updates positions of all balls, handles logic related to collisions.
     * Used Exercise 6 as reference.
     */
    public void tick() {
        //win when all balls except cueBall are not active for play
        if (isWin()) winFlag = true;

        tickCount++;
        //add the timer by 1 every second
        if ((tickCount % 60) == 0){
            timer.addTime();
        }

        for (Ball ball : balls) {
            ball.tick();

            if (ball.isCue() && cueSet) {
                //save the current state
                saveGame();

                hitBall(ball);
            }

            double width = table.getxLength();
            double height = table.getyLength();

            // Check if ball landed in pocket
            for (Pocket pocket : table.getPockets()) {
                if (pocket.isInPocket(ball)) {
                    if (ball.isCue()) {
                        this.reset();
                    } else {
                        if (!ball.remove()){
                            // Check if when ball is removed, any other balls are present in its space. (If
                            // another ball is present, blue ball is removed)
                            score.addScore(ball);

                            for (Ball otherBall : balls) {
                                double deltaX = ball.getxPos() - otherBall.getxPos();
                                double deltaY = ball.getyPos() - otherBall.getyPos();
                                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                                if (otherBall != ball && otherBall.isActive() && distance < 10) {
                                    ball.remove();
                                }
                            }
                        }
                    }
                    break;
                }
            }

            // Handle the edges (balls don't get a choice here)
            if (ball.getxPos() + ball.getRadius() > width + TABLEBUFFER) {
                ball.setxPos(width - ball.getRadius());
                ball.setxVel(ball.getxVel() * -1);
            }
            if (ball.getxPos() - ball.getRadius() < TABLEBUFFER) {
                ball.setxPos(ball.getRadius());
                ball.setxVel(ball.getxVel() * -1);
            }
            if (ball.getyPos() + ball.getRadius() > height + TABLEBUFFER) {
                ball.setyPos(height - ball.getRadius());
                ball.setyVel(ball.getyVel() * -1);
            }
            if (ball.getyPos() - ball.getRadius() < TABLEBUFFER) {
                ball.setyPos(ball.getRadius());
                ball.setyVel(ball.getyVel() * -1);
            }

            // Apply table friction
            double friction = table.getFriction();
            ball.setxVel(ball.getxVel() * friction);
            ball.setyVel(ball.getyVel() * friction);

            // Check ball collisions
            for (Ball ballB : balls) {
                if (checkCollision(ball, ballB)) {
                    Point2D ballPos = new Point2D(ball.getxPos(), ball.getyPos());
                    Point2D ballBPos = new Point2D(ballB.getxPos(), ballB.getyPos());
                    Point2D ballVel = new Point2D(ball.getxVel(), ball.getyVel());
                    Point2D ballBVel = new Point2D(ballB.getxVel(), ballB.getyVel());
                    Pair<Point2D, Point2D> changes = calculateCollision(ballPos, ballVel, ball.getMass(), ballBPos,
                            ballBVel, ballB.getMass(), false);
                    calculateChanges(changes, ball, ballB);
                }
            }
        }
    }

    private void saveGame(){
        //save all balls
        for (Ball b : balls){
            careTaker.addBallMemento(b, b.save());
        }

        //save the current score
        careTaker.updateScoreState(score.save());

        //save current time
        careTaker.updateTimerState(timer.save());

    }

    private void setGame(){
        if (isWin()) return;

        for (Ball ball : balls){
            BallMemento bm = careTaker.getBallMementos(ball);
            if (bm != null) {
                ball.restore(bm);
            }
        }

        //set score state to current score
        ScoreMemento sm = careTaker.getScoreState();
        if (sm != null){
            score.restore(sm);
            score.inform();
        }

        //set timer state to current timer
        TimerMemento tm = careTaker.getTimerState();
        if (tm != null){
            timer.restore(tm);
            timer.inform();
        }

    }


    /**
     * check the game win
     * @return true if all balls except cue ball is fallen/non-active
     */
    public boolean isWin(){
        for (Ball b : balls){
            if (b.isActive() && !b.isCue()){
                return false;
            }
        }
        return true;
    }

    private void setUpTimeAndScore(){
        Label timerLabel = new Label("Time: 0:00");
        timerLabel.setFont(Font.font(15));
        timerLabel.setLayoutX(600 + TABLEBUFFER * 2);
        timerLabel.setLayoutY(30);
        pane.getChildren().add(timerLabel);

        Label scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Font.font(15));
        scoreLabel.setLayoutX(600 + TABLEBUFFER * 2);
        scoreLabel.setLayoutY(30 + 20);
        pane.getChildren().add(scoreLabel);

        timer = new Timer(0, false);
        score = new Score(0);
        TimeObserver timeObserver = new TimeObserver(timer, timerLabel);
        ScoreObserver scoreObserver = new ScoreObserver(score, scoreLabel);
    }

    private void setUpCheat(){
        int BUTTONHEIGHT = 20;
        Label cheatLabel = new Label("Cheat:");
        cheatLabel.setFont(Font.font(15));
        cheatLabel.setLayoutX(600 + TABLEBUFFER * 2);
        cheatLabel.setLayoutY(60 + BUTTONHEIGHT);
        pane.getChildren().add(cheatLabel);

        Button red = new Button("Red Balls");
        red.setPrefSize(100, BUTTONHEIGHT);
        red.setLayoutX(600 + TABLEBUFFER * 2);
        red.setLayoutY(60 + BUTTONHEIGHT * 2);
        pane.getChildren().add(red);

        red.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cheatBalls(Paint.valueOf("red"));
            }
        });

        Button yellow = new Button("Yellow Balls");
        yellow.setPrefSize(100, BUTTONHEIGHT);
        yellow.setLayoutX(600 + TABLEBUFFER * 2);
        yellow.setLayoutY(60 + BUTTONHEIGHT * 3 + 10);
        pane.getChildren().add(yellow);

        yellow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cheatBalls(Paint.valueOf("yellow"));
            }
        });

        Button green = new Button("Green Balls");
        green.setPrefSize(100, BUTTONHEIGHT);
        green.setLayoutX(600 + TABLEBUFFER * 2);
        green.setLayoutY(60 + BUTTONHEIGHT * 4 + 10 * 2);
        pane.getChildren().add(green);

        green.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cheatBalls(Paint.valueOf("green"));
            }
        });

        Button brown = new Button("Brown Balls");
        brown.setPrefSize(100, BUTTONHEIGHT);
        brown.setLayoutX(600 + TABLEBUFFER * 2);
        brown.setLayoutY(60 + BUTTONHEIGHT * 5 + 10 * 3);
        pane.getChildren().add(brown);

        brown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cheatBalls(Paint.valueOf("brown"));
            }
        });

        Button blue = new Button("Blue Balls");
        blue.setPrefSize(100, BUTTONHEIGHT);
        blue.setLayoutX(600 + TABLEBUFFER * 2);
        blue.setLayoutY(60 + BUTTONHEIGHT * 6 + 10 * 4);
        pane.getChildren().add(blue);

        blue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cheatBalls(Paint.valueOf("blue"));
            }
        });

        Button purple = new Button("Purple Balls");
        purple.setPrefSize(100, BUTTONHEIGHT);
        purple.setLayoutX(600 + TABLEBUFFER * 2);
        purple.setLayoutY(60 + BUTTONHEIGHT * 7 + 10 * 5);
        pane.getChildren().add(purple);

        purple.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cheatBalls(Paint.valueOf("purple"));
            }
        });

        Button black = new Button("Black Balls");
        black.setPrefSize(100, BUTTONHEIGHT);
        black.setLayoutX(600 + TABLEBUFFER * 2);
        black.setLayoutY(60 + BUTTONHEIGHT * 8 + 10 * 6);
        pane.getChildren().add(black);

        black.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cheatBalls(Paint.valueOf("black"));
            }
        });

        Button orange = new Button("Orange Balls");
        orange.setPrefSize(100, BUTTONHEIGHT);
        orange.setLayoutX(600 + TABLEBUFFER * 2);
        orange.setLayoutY(60 + BUTTONHEIGHT * 9 + 10 * 7);
        pane.getChildren().add(orange);

        orange.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cheatBalls(Paint.valueOf("orange"));
            }
        });

//        Button save = new Button("Save");
//        save.setPrefSize(100, BUTTONHEIGHT);
//        save.setLayoutX(600 + TABLEBUFFER * 2);
//        save.setLayoutY(60 + BUTTONHEIGHT * 10 + 10 * 8);
//        pane.getChildren().add(save);
//
//        save.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                saveGame();
//            }
//        });

        Button undo = new Button("Undo");
        undo.setPrefSize(100, BUTTONHEIGHT);
        undo.setLayoutX(600 + TABLEBUFFER * 2);
        undo.setLayoutY(60 + BUTTONHEIGHT * 10 + 10 * 8);
        pane.getChildren().add(undo);

        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setGame();
            }
        });


    }

    /**
     * remove balls in selected colour from play
     *
     * @param color of the cheat balls
     */
    private void cheatBalls(Paint color){
        saveGame();

        for (Ball b : balls){
            if (b.getColour() == color){
                b.disActive();
                score.addScore(b);
            }
        }
    }

    /**
     * Resets the game.
     */
    public void reset() {
        for (Ball ball : balls) {
            ball.reset();
        }

        score.clearScore();
    }

    public Pane getPane(){ return this.pane; }

    /**
     * @return scene.
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Sets the table of the game.
     * 
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * @return table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Sets the balls of the game.
     * 
     * @param balls
     */
    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    public void setPocketsToTable(ArrayList<Pocket> pockets){
        table.setPockets(pockets);
    }

    /**
     * Hits the ball with the cue, distance of the cue indicates the strength of the
     * strike.
     * 
     * @param ball
     */
    private void hitBall(Ball ball) {
        double deltaX = ball.getxPos() - cue.getStartX();
        double deltaY = ball.getyPos() - cue.getStartY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Check that start of cue is within cue ball
        if (distance < ball.getRadius()) {
            // Collide ball with cue
            double hitxVel = (cue.getStartX() - cue.getEndX()) * FORCEFACTOR;
            double hityVel = (cue.getStartY() - cue.getEndY()) * FORCEFACTOR;

            ball.setxVel(hitxVel);
            ball.setyVel(hityVel);
        }

        cueSet = false;

    }

    /**
     * Changes values of balls based on collision (if ball is null ignore it)
     * 
     * @param changes
     * @param ballA
     * @param ballB
     */
    private void calculateChanges(Pair<Point2D, Point2D> changes, Ball ballA, Ball ballB) {
        ballA.setxVel(changes.getKey().getX());
        ballA.setyVel(changes.getKey().getY());
        if (ballB != null) {
            ballB.setxVel(changes.getValue().getX());
            ballB.setyVel(changes.getValue().getY());
        }
    }

    /**
     * Sets the cue to be drawn on click, and manages cue actions
     * 
     * @param pane
     */
    private void setClickEvents(Pane pane) {
        pane.setOnMousePressed(event -> {
            cue = new Line(event.getX(), event.getY(), event.getX(), event.getY());
            cueSet = false;
            cueActive = true;
        });

        pane.setOnMouseDragged(event -> {
            cue.setEndX(event.getX());
            cue.setEndY(event.getY());
        });

        pane.setOnMouseReleased(event -> {
            cueSet = true;
            cueActive = false;
        });
    }

    /**
     * Checks if two balls are colliding.
     * Used Exercise 6 as reference.
     * 
     * @param ballA
     * @param ballB
     * @return true if colliding, false otherwise
     */
    private boolean checkCollision(Ball ballA, Ball ballB) {
        if (ballA == ballB || !ballA.isActive() || !ballB.isActive()) {
            return false;
        }

        return Math.abs(ballA.getxPos() - ballB.getxPos()) < ballA.getRadius() + ballB.getRadius() &&
                Math.abs(ballA.getyPos() - ballB.getyPos()) < ballA.getRadius() + ballB.getRadius();
    }

    /**
     * Collision function adapted from assignment, using physics algorithm:
     * http://www.gamasutra.com/view/feature/3015/pool_hall_lessons_fast_accurate_.php?page=3
     *
     * @param positionA The coordinates of the centre of ball A
     * @param velocityA The delta x,y vector of ball A (how much it moves per tick)
     * @param massA     The mass of ball A (for the moment this should always be the
     *                  same as ball B)
     * @param positionB The coordinates of the centre of ball B
     * @param velocityB The delta x,y vector of ball B (how much it moves per tick)
     * @param massB     The mass of ball B (for the moment this should always be the
     *                  same as ball A)
     *
     * @return A Pair in which the first (key) Point2D is the new
     *         delta x,y vector for ball A, and the second (value) Point2D is the
     *         new delta x,y vector for ball B.
     */
    public static Pair<Point2D, Point2D> calculateCollision(Point2D positionA, Point2D velocityA, double massA,
            Point2D positionB, Point2D velocityB, double massB, boolean isCue) {

        // Find the angle of the collision - basically where is ball B relative to ball
        // A. We aren't concerned with
        // distance here, so we reduce it to unit (1) size with normalize() - this
        // allows for arbitrary radii
        Point2D collisionVector = positionA.subtract(positionB);
        collisionVector = collisionVector.normalize();

        // Here we determine how 'direct' or 'glancing' the collision was for each ball
        double vA = collisionVector.dotProduct(velocityA);
        double vB = collisionVector.dotProduct(velocityB);

        // If you don't detect the collision at just the right time, balls might collide
        // again before they leave
        // each others' collision detection area, and bounce twice.
        // This stops these secondary collisions by detecting
        // whether a ball has already begun moving away from its pair, and returns the
        // original velocities
        if (vB <= 0 && vA >= 0 && !isCue) {
            return new Pair<>(velocityA, velocityB);
        }

        // This is the optimisation function described in the gamasutra link. Rather
        // than handling the full quadratic
        // (which as we have discovered allowed for sneaky typos)
        // this is a much simpler - and faster - way of obtaining the same results.
        double optimizedP = (2.0 * (vA - vB)) / (massA + massB);

        // Now we apply that calculated function to the pair of balls to obtain their
        // final velocities
        Point2D velAPrime = velocityA.subtract(collisionVector.multiply(optimizedP).multiply(massB));
        Point2D velBPrime = velocityB.add(collisionVector.multiply(optimizedP).multiply(massA));

        return new Pair<>(velAPrime, velBPrime);
    }
}
