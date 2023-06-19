package com.example.buscaminas;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class SMButton extends Button {

    private final int column;
    private final int row;
    private final int minesAround;

    public SMButton(String s, int column, int row, int minesAround){
        super(s);
        this.column = column;
        this.row = row;
        this.minesAround = minesAround;
    }
    public int getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }
    public int getMinesAround() {
        return minesAround;
    }

    @Override
    public String toString(){
        return column + "," + row + " " + minesAround;
    }

    public void press(Event event, GridPane gridPane){
        press(gridPane, (SMButton) event.getSource());
    }
    public void press(GridPane gridPane, SMButton thisSMButton){
        int min = thisSMButton.getMinesAround();
        if (min==-1){
            Main.lose(gridPane);
        } else {
            thisSMButton.setText((min==0) ? "  ":String.valueOf(thisSMButton.getMinesAround()));
            thisSMButton.setStyle("-fx-border-color: grey; -fx-background-color: white");
            if (min==0){
                int column = thisSMButton.getColumn();
                int row = thisSMButton.getRow();
                for (Node n:gridPane.getChildren()){
                    SMButton smButton = (SMButton) n;
                    if (Math.abs(smButton.getColumn()-column) < 2 && Math.abs(smButton.getRow()-row) < 2 &&
                            !smButton.getStyle().equals("-fx-border-color: grey; -fx-background-color: white")){
                        smButton.press(gridPane, smButton);
                    }
                }
            }
        }
    }

    public void pressSecondary(GridPane gridPane) {
        if (this.getStyle().equals("-fx-border-color: grey")){
            this.setStyle("-fx-border-color: red");
        } else if (this.getStyle().equals("-fx-border-color: red")){
            this.setStyle("-fx-border-color: grey");
        } else {
            int count = 0;
            for (Node n:gridPane.getChildren()) {
                SMButton smButton = (SMButton) n;
                if (Math.abs(smButton.getColumn() - column) < 2 && Math.abs(smButton.getRow() - row) < 2
                        && smButton.getStyle().equals("-fx-border-color: red")) {
                    count++;
                }
            }
            if (count == minesAround){
                for (Node n:gridPane.getChildren()) {
                    SMButton smButton = (SMButton) n;
                    if (Math.abs(smButton.getColumn() - column) < 2 && Math.abs(smButton.getRow() - row) < 2
                            && smButton.getStyle().equals("-fx-border-color: grey")) {
                        smButton.press(gridPane, smButton);
                    }
                }
            }
        }
    }
}

