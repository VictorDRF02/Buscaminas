package com.example.buscaminas;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashSet;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: rgb(207, 207, 207)");
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(pane, 400, 400);
        MenuItem newGame = new MenuItem("Juego nuevo");
        MenuBar menuBar = new MenuBar();
        menuBar.setPrefWidth(pane.getWidth());
        Menu game = new Menu("Juego");
        newGame.setOnAction(event -> reset(10, 10, 10, gridPane));
        game.getItems().add(newGame);
        menuBar.getMenus().add(game);
        pane.getChildren().add(menuBar);

        load(10, 10, 10, gridPane);

        pane.getChildren().add(gridPane);
        gridPane.setLayoutX(((pane.getWidth()- gridPane.getColumnCount()*26)/2));
        gridPane.setLayoutY(((pane.getHeight()- gridPane.getRowCount()*31)/2));

        stage.setTitle("BuscaMinas");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public static void reset(int column, int row, int mine, GridPane gridPane){
        gridPane.getChildren().removeAll(gridPane.getChildren());
        gridPane.setDisable(false);
        load(column, row, mine, gridPane);
    }
    public static void load(int column, int row, int mine, GridPane gridPane){
        HashSet<String> mines = mines(mine, column, row);
        int[][] table = table(mines, column, row);
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row; j++) {
                SMButton button = new SMButton("  ", i, j, table[i][j]);
                button.setStyle("-fx-border-color: grey");

                button.setOnMouseClicked(event -> {
                    if (event.getButton().toString().equals("SECONDARY")){
                        ((SMButton) event.getSource()).pressSecondary((GridPane) ((SMButton) event.getSource()).getParent());
                    } else {
                        ((SMButton) event.getSource()).press(event, (GridPane) ((SMButton) event.getSource()).getParent());
                    }

                    int count = 0;
                    for (Node n:
                            ((GridPane) ((SMButton) event.getSource()).getParent()).getChildren()) {
                        SMButton smButtonAux = (SMButton) n;
                        if (smButtonAux.getStyle().equals("-fx-border-color: grey")
                                || smButtonAux.getStyle().equals("-fx-border-color: red")){
                            count++;
                        }
                    }
                    if (count==mines.size()){
                        win(((GridPane) ((SMButton) event.getSource()).getParent()));
                    }
                });
                gridPane.add(button, i, j);
            }
        }
    }
    public static HashSet<String> mines(int cantMines, int column, int row){
        HashSet<String> mines = new HashSet<>();
        while (mines.size()<cantMines){
            mines.add(Math.round(Math.random()*(column-1)) + "," + (Math.round(Math.random()*(row-1))));
        }
        return mines;
    }

    public static int[][] table(HashSet<String> mines, int column, int row){
        int[][] table = new int[column][row];
        for (int i = 0; i < column; i++){
            for (int j = 0; j < row; j++){
                table[i][j] = mines.contains(i+","+j) ? -1:0;
            }
        }
        for (int i = 0; i < column; i++){
            for (int j = 0; j < row; j++){
                int count = 0;

                for (int k = -1; k < 2; k++){
                    for (int l = -1; l < 2; l++){
                        try {
                            assert table[i + k] != null;
                            if (table[i+k][j+l]==-1){
                                count++;
                            }
                        }   catch (Exception ignored){}
                    }
                }
                if (table[i][j] != -1){
                    table[i][j] = count;
                }
            }
        }
        return table;
    }

    public static void win(GridPane gridPane){
        gridPane.setDisable(true);
    }
    public static void lose(GridPane gridPane) {
        gridPane.setDisable(true);
        for (Node n: gridPane.getChildren()) {
            if (((SMButton) n).getMinesAround() == -1){
                n.setStyle("-fx-background-color: red");
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}