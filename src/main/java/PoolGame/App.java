package PoolGame;

import PoolGame.config.*;

import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/** Main application entry point. */
public class App extends Application {
    private final String EASY = "src/main/resources/config_easy.json";
    private final String NORMAL = "src/main/resources/config_normal.json";
    private final String HARD = "src/main/resources/config_hard.json";

    /**
     * @param args First argument is the path to the config file
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    /**
     * Starts the application.
     * 
     * @param primaryStage The primary stage for the application.
     */
    public void start(Stage primaryStage) {
        int BUTTONHEIGHT = 60;

        Pane pane = new Pane();
        Scene scene = new Scene(pane, 400, 300);

        Button easy = new Button("Easy Mode");
        easy.setPrefSize(200, BUTTONHEIGHT);
        easy.setLayoutX(100);
        easy.setLayoutY(20);
        pane.getChildren().add(easy);

        easy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGameScene(EASY, primaryStage);
            }
        });

        Button normal = new Button("Normal Mode");
        normal.setPrefSize(200, BUTTONHEIGHT);
        normal.setLayoutX(100);
        normal.setLayoutY(20 + BUTTONHEIGHT + 20);
        pane.getChildren().add(normal);

        normal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGameScene(NORMAL, primaryStage);
            }
        });

        Button hard = new Button("Hard Mode");
        hard.setPrefSize(200, BUTTONHEIGHT);
        hard.setLayoutX(100);
        hard.setLayoutY(20 + BUTTONHEIGHT + 20 + BUTTONHEIGHT + 20);
        pane.getChildren().add(hard);

        hard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGameScene(HARD, primaryStage);
            }
        });

        primaryStage.setTitle("Game Difficulty Level");
        primaryStage.setScene(scene);
        primaryStage.show();

//        startGameScene("src/main/resources/config_easy.json", primaryStage);


    }

    public void startGameScene(String configPath, Stage primaryStage){
        // READ IN CONFIG
        GameManager gameManager = new GameManager();
//        List<String> args = getParameters().getRaw();
//        String configPath = checkConfig(args);

        ReaderFactory tableFactory = new TableReaderFactory();
        Reader tableReader = tableFactory.buildReader();
        tableReader.parse(configPath, gameManager);

        ReaderFactory pocketFactory = new PocketReaderFactory();
        Reader pocketReader = pocketFactory.buildReader();
        pocketReader.parse(configPath, gameManager);

        ReaderFactory ballFactory = new BallReaderFactory();
        Reader ballReader = ballFactory.buildReader();
        ballReader.parse(configPath, gameManager);
        gameManager.buildManager();

        Button level = new Button("Switch Level");
        level.setPrefSize(100, 30);
        level.setLayoutX(600 + 100);
        level.setLayoutY(0);
        gameManager.getPane().getChildren().add(level);

        level.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String newPath;
                if (configPath.equals(EASY)){
                    newPath = NORMAL;
                } else if (configPath.equals(NORMAL)) {
                    newPath = HARD;
                } else{
                    newPath = EASY;
                }

                startGameScene(newPath, primaryStage);
            }
        });

        // START GAME MANAGER
//        gameManager.run();
        primaryStage.setTitle("Pool");
        primaryStage.setScene(gameManager.getScene());
        primaryStage.show();
        gameManager.run();
    }

    /**
     * Checks if the config file path is given as an argument.
     * 
     * @param args
     * @return config path.
     */
//    private static String checkConfig(List<String> args) {
//        String configPath;
//        if (args.size() > 0) {
//            configPath = args.get(0);
//        } else {
//            configPath = "src/main/resources/config_easy.json";
//        }
//        return configPath;
//    }

}
