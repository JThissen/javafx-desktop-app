package CustomDashboard;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

@SuppressWarnings("Duplicates")
public class CustomRipple
{
    private class Ripple extends Circle
    {
        private Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(radiusProperty(), 0, Interpolator.LINEAR)),
                new KeyFrame(Duration.millis(0), new KeyValue(opacityProperty(), 1, Interpolator.LINEAR)),
                new KeyFrame(Duration.millis(500), new KeyValue(radiusProperty(), 200 * 0.75, Interpolator.LINEAR)),
                new KeyFrame(Duration.millis(500), new KeyValue(opacityProperty(), 0, Interpolator.LINEAR)));

        public Ripple(double xPos, double yPos, double buttonWidth)
        {
            super(xPos, yPos, 0, null);
            setFill(Color.rgb(71, 130, 197, 1));
        }
    }

    public void createRipple(double screenX, double screenY, Pane pane, Node node)
    {
        Bounds bounds = node.getBoundsInParent();
        double width = node.getLayoutBounds().getWidth();
        double height = node.getLayoutBounds().getHeight();
        Node mask = new Rectangle(bounds.getMinX(), bounds.getMinY() + 25, width, height);
        Ripple ripple = new Ripple(screenX, screenY, node.getLayoutBounds().getWidth());
        pane.getChildren().add(0, ripple);
        ripple.setClip(mask);
        ripple.toFront();
        ripple.timeline.play();

        Timeline stopTimeline = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                ripple.timeline.stop();
                pane.getChildren().remove(ripple);
            }
        }));

        stopTimeline.play();
    }
}