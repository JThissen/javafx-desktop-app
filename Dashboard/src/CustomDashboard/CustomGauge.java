package CustomDashboard;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.util.*;

public class CustomGauge
{
    private Pane sp;
    private Canvas canvas;
    private ObservableList<PathElement> elements;
    private String itemName;
    private double radius;
    private double strokeWidth;
    private Color color;
    private int randomQuantity;
    private int maxQuantity;
   // private Rectangle rectangle;
    private Circle circle;

    public CustomGauge(double radius, double strokeWidth, String itemName, Color color)
    {
        this.radius = radius;
        this.strokeWidth = strokeWidth;
        this.itemName = itemName;
        this.color = color;

        Random random = new Random();
        int min = 300;
        maxQuantity = 999;
        randomQuantity = random.nextInt((maxQuantity - min) + 1) + min;
    }

    public Pane CreatePane()
    {
        sp = new Pane();
        sp.getChildren().addAll(CreateBackgroundArc(), CreateForegroundArc(), CreateItemLabel(),
                CreateUnitsLeftLabel(), CreateQuantityLabel(), circle);
        return sp;
    }

    private Label CreateUnitsLeftLabel()
    {
        Label unitsLeftLabel = new Label(String.valueOf(randomQuantity));
        unitsLeftLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold");
        unitsLeftLabel.layoutXProperty().bind(sp.widthProperty().subtract(unitsLeftLabel.widthProperty()).divide(2));
        unitsLeftLabel.layoutYProperty().bind(sp.heightProperty().multiply(0.40));
        return unitsLeftLabel;
    }

    private Label CreateQuantityLabel()
    {
        Label quantityLabel = new Label("QTY");
        quantityLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 9");
        quantityLabel.layoutXProperty().bind(sp.widthProperty().subtract(quantityLabel.widthProperty()).divide(2));
        quantityLabel.layoutYProperty().bind(sp.heightProperty().multiply(0.50));
        return quantityLabel;
    }

    private Label CreateItemLabel()
    {
        String[] possibleLabels = new String[]{"ITEM 1", "ITEM 2", "ITEM 3", "ITEM 4"};
        String labelName = null;

        switch(itemName)
        {
            case "ITEM 1":
                labelName = possibleLabels[0];
                break;

            case "ITEM 2":
                labelName = possibleLabels[1];
                break;

            case "ITEM 3":
                labelName = possibleLabels[2];
                break;

            case "ITEM 4":
                labelName = possibleLabels[3];
                break;
        }

        Label label = new Label(labelName);
        label.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold");
        label.layoutXProperty().bind(sp.widthProperty().subtract(label.widthProperty()).divide(2));
        label.layoutYProperty().bind(sp.heightProperty().multiply(0.035));
        return label;
    }

    private Node CreateBackgroundArc()
    {
        Arc arc = new Arc();
        arc.setRadiusX(this.radius);
        arc.setRadiusY(this.radius);
        arc.setStartAngle(0.0);
        arc.setStroke(Color.rgb(50, 53, 64, 1.0));
        arc.setFill(Color.TRANSPARENT);
        arc.setStrokeWidth(this.strokeWidth);
        arc.setLength(360.0);
        arc.setType(ArcType.OPEN);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.centerXProperty().bind(sp.widthProperty().divide(2));
        arc.centerYProperty().bind(sp.heightProperty().divide(2));
        return arc;
    }

    private Node CreateForegroundArc()
    {
        Arc arc = new Arc();
        arc.setRadiusX(this.radius);
        arc.setRadiusY(this.radius);
        arc.setStartAngle(0.0);
        arc.setStroke(this.color);
        arc.setFill(Color.TRANSPARENT);
        arc.setStrokeWidth(this.strokeWidth * 0.5);
        arc.setLength((360.0 / maxQuantity) * randomQuantity);
        arc.setType(ArcType.OPEN);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.centerXProperty().bind(sp.widthProperty().divide(2));
        arc.centerYProperty().bind(sp.heightProperty().divide(2));

        Tooltip tooltip = new Tooltip(String.valueOf(maxQuantity - randomQuantity) + " of " + maxQuantity + " items sold.");

        circle = new Circle();
        circle.setRadius(this.radius + this.strokeWidth * 0.5);
        circle.setFill(Color.TRANSPARENT);
        circle.centerXProperty().bind(arc.centerXProperty());
        circle.centerYProperty().bind(arc.centerYProperty());

        circle.setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                tooltip.show(arc, circle.localToScreen(circle.getBoundsInParent()).getMinX(),
                        circle.localToScreen(circle.getBoundsInParent()).getMinY());
                AddGlowEffect(arc, color, 15);
                arc.setStroke(Color.hsb(color.getHue(), color.getSaturation(), Math.min(color.getBrightness() * 1.2, 1.0), 1.0));
            }
        });

        circle.setOnMouseExited(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                tooltip.hide();
                arc.setEffect(null);
                arc.setStroke(color);
            }
        });

        return arc;
    }

    private void AddGlowEffect(Arc arc, Color color, double glowStrength)
    {
        DropShadow glow = new DropShadow();
        glow.setOffsetX(0d);
        glow.setOffsetY(0d);
        glow.setColor(color);
        glow.setWidth(glowStrength);
        glow.setHeight(glowStrength);
        arc.setEffect(glow);
    }


    //    private Path CreatePath()
//    {
//        double radius = 50.0;
//        double centerX = 200.0;
//        double centerY = 100.0;
//
//        Path path = new Path();
//        path.setStrokeWidth(10);
//        path.setStroke(Color.LIMEGREEN);
//        elements = path.getElements();
//        elements.add(new MoveTo(centerX + radius, centerY));
//
//        int maxDegrees = 360;
//        for(int i = 0; i < maxDegrees; ++i)
//        {
//            elements.add(new LineTo(centerX + (radius * Math.cos(DegreeToRadians(i))), centerY + (radius * Math.sin(DegreeToRadians(i)))));
//        }
//        return path;
//    }


    //Path path = CreatePath();

//        Circle circle = new Circle();
//        circle.setRadius(5.0);
//        circle.setFill(Color.BLACK);
//
//        PathTransition pathTransition = new PathTransition();
//        pathTransition.setDuration(Duration.millis(5000));
//        pathTransition.setPath(path);
//        pathTransition.setNode(circle);
//        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
//        pathTransition.setCycleCount(Timeline.INDEFINITE);
//        pathTransition.play();

    //sp.getChildren().addAll(path, circle);
}