package fr.jSlim.models.cell;

import fr.jSlim.controller.HomepageController;
import fr.jSlim.models.enums.State;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellNode extends Rectangle {

    private SquareImpl square;

    public CellNode(int x, int y, double width, double height, HomepageController controller) {

        // create rectangle
        super(width, height);


        setStroke(Color.BLACK);
        setFill(State.VOID.getColor());

        setOnMouseClicked(event -> {
            changeState(controller.getCurrentState());
            controller.updateDLabel();
        });

        square = new SquareImpl();
        square.setRow(x);
        square.setColumn(y);
        square.setState(controller.getCurrentState());
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(SquareImpl square) {
        this.square = square;
    }

    public void changeState(State cellType) {
        this.getSquare().setState(cellType);
        this.setFill(cellType.getColor());
    }
}