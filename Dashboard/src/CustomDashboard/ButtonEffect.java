package CustomDashboard;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


@SuppressWarnings("Duplicates")
public class ButtonEffect
{
    private class SquareExpansion extends Rectangle
    {
        private Timeline timeline;

        public SquareExpansion(double x, double y, double width, double height, double panelWidth, double animationTime, double startTime)
        {
            super(x, y, width, height);
            setFill(Color.rgb(71, 130, 197, 1));
            timeline = new Timeline(
                    new KeyFrame(Duration.millis(0), new KeyValue(opacityProperty(), 0.8)),
                    new KeyFrame(Duration.millis(startTime), new KeyValue(opacityProperty(), 0.8)),
                    new KeyFrame(Duration.millis(animationTime + startTime), new KeyValue(opacityProperty(), 0)),
                    new KeyFrame(Duration.millis(0), new KeyValue(widthProperty(), 0)),
                    new KeyFrame(Duration.millis(animationTime), new KeyValue(widthProperty(), panelWidth)));
        }
    }

    public void CreateEffect(double screenX, double screenY, Node button, Pane pane, double panelWidth, double animationTime, double startTime)
    {
        Bounds bounds = button.getBoundsInParent();
        double width = button.getLayoutBounds().getWidth();
        double height = button.getLayoutBounds().getHeight();
        Node mask = new Rectangle(bounds.getMinX(), bounds.getMinY(), width, height);
        SquareExpansion squareExpansion = new SquareExpansion(screenX, screenY, width, height, panelWidth, animationTime, startTime);
        pane.getChildren().add(0, squareExpansion);
        squareExpansion.toFront();
        squareExpansion.timeline.play();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                squareExpansion.timeline.stop();
                pane.getChildren().remove(squareExpansion);
            }
        }));

        timeline.play();
    }
}