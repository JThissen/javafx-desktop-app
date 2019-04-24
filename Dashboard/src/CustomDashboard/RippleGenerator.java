package CustomDashboard;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class RippleGenerator extends Button
{
    private class Ripple extends Circle
    {
        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(radiusProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(3), new KeyValue(radiusProperty(), 100)),
                new KeyFrame(Duration.seconds(3), new KeyValue(opacityProperty(), 0))
        );

        private Ripple(double centerX, double centerY)
        {
            super(centerX, centerY, 0, null);
            setStroke(Color.rgb(200, 200, 225));
        }
    }

    private double generatorCenterX = 100.0;
    private double generatorCenterY = 100.0;
    private Timeline generate = new Timeline(
            new KeyFrame(Duration.seconds(0.5), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    createRipple();
                }
            })
    );

    public void createRipple()
    {
        Ripple ripple = new Ripple(generatorCenterX, generatorCenterY);
        getChildren().add(ripple);
        ripple.animation.play();

        Timeline remover = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ripple.animation.stop();
            }
        }));

        remover.play();
    }

    public void startGenerating()
    {
        generate.play();
    }

    public void stopGenerating()
    {
        generate.stop();
    }

    public void setGeneratorCenterX(double generatorCenterX)
    {
        this.generatorCenterX = generatorCenterX;
    }

    public void setGeneratorCenterY(double generatorCenterY)
    {
        this.generatorCenterY = generatorCenterY;
    }
}