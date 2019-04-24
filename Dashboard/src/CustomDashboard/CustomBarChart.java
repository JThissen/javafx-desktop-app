package CustomDashboard;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.Random;

public class CustomBarChart extends BarChart
{
    ObservableList<XYChart.Series<String, Double>> data;
    private String[] days;

    public CustomBarChart(Axis axis, Axis axis2)
    {
        super(axis, axis2);
        getStylesheets().add(getClass().getClassLoader().getResource("CustomDashboard/CustomBarChartStyleSheet.css").toExternalForm());
        setLegendVisible(false);
        days = new String[]{"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        setData(CreateChart());
        ModifyBarColors();
        setBarGap(0.0);
        setCategoryGap(5.0);
    }

    @Override
    protected void layoutChildren()
    {
        super.layoutChildren();
        NodeInfoAnimation();
    }

    private void ModifyBarColors()
    {
        Color color1 = Color.rgb(68, 163, 219, 1);
        Color color2 = Color.rgb(124, 121, 253, 1);
        Color color3 = Color.rgb(240, 121, 253, 1);
        Color[] colors = new Color[]{color1, color2, color3};

        for(int i = 0, colorCounter = 0; i < days.length; ++i)
        {
            if(colorCounter >= colors.length)
                colorCounter = 0;

            String barLookup = new StringBuilder().append(".data").append(i).append(".chart-bar").toString();
            String fxBarFill = new StringBuilder().append("-fx-bar-fill: ").append(Utilities.ConvertToHex(colors[colorCounter])).toString();
            Node node = lookup(barLookup);
            node.setStyle(fxBarFill);

            int finalColorCounter = colorCounter;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            node.setOnMouseEntered(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    AddGlowEffect(node, colors[finalColorCounter], 15);
                    Color colorModified = Color.hsb(colors[finalColorCounter].getHue(), colors[finalColorCounter].getSaturation(),
                            Math.min(colors[finalColorCounter].getBrightness() * 1.5, 1.0), 1.0);

                    RadialGradient radialGradient = new RadialGradient(0, 0, 0, 0, 50, false, CycleMethod.REFLECT,
                            new Stop(0, Color.rgb(255, 255, 255, 1)), new Stop(1, colors[finalColorCounter]));

                    String fxBarFillModified = new StringBuilder().append("-fx-bar-fill: ").append(Utilities.ConvertToHex(colorModified)).toString();
                    node.setStyle(fxBarFillModified);
                }
            });

            node.setOnMouseExited(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    node.setEffect(null);
                    node.setStyle(fxBarFill);
                }
            });

            colorCounter++;
        }
    }

    private ObservableList<Series<String, Double>> CreateChart()
    {
        data = FXCollections.observableArrayList();
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        Random random = new Random();

        for(int i = 0; i < days.length; ++i)
        {
            XYChart.Data<String, Double>chartData = new XYChart.Data<String, Double>(days[i], random.nextDouble());
            series.getData().add(chartData);
        }

        data.add(series);
        return data;
    }

    private void AddGlowEffect(Node node, Color color, double glowStrength)
    {
        DropShadow glow = new DropShadow();
        glow.setOffsetX(0d);
        glow.setOffsetY(0d);
        glow.setColor(color);
        glow.setWidth(glowStrength);
        glow.setHeight(glowStrength);
        node.setEffect(glow);
    }

    private void NodeInfoAnimation()
    {
        ObservableList<XYChart.Data<String, Double>> listOfData = data.get(0).getData();

        for(int i = 0; i < listOfData.size(); ++i)
        {
            SequentialTransition sequentialTransition = new SequentialTransition();

            double width = 23;
            double height = 15;
            Pane pane = new Pane();
            //pane.setTranslateX(4);
            pane.setMinSize(width, height);
            pane.setStyle("-fx-background-color: transparent;");

            Pane pane2 = new Pane();
            pane2.setTranslateX(width / 2.0);
            pane2.setTranslateY(height / 2.0);
            pane2.setMinSize(1, 1);
            pane2.setMouseTransparent(true);

            Circle head = new Circle(2, Color.WHITE);
            sequentialTransition.getChildren().add(CreateHeadAnimation(head));

            Line line = new Line();
            line.setStroke(Color.WHITE);
            line.setStrokeWidth(1.0);
            sequentialTransition.getChildren().add(CreateLineAnimation(line));

            VBox vBox = new VBox();
            vBox.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 1.0), new CornerRadii(0.0), new Insets(0))));

            DecimalFormat decimals = new DecimalFormat("#.##");
            Text text = new Text(decimals.format(listOfData.get(i).getYValue()).toString());
            text.setFont(Font.font(10));
            vBox.setMargin(text, new Insets(4, 2, 2, 2));

            vBox.getChildren().add(text);
            vBox.setVisible(false);
            sequentialTransition.getChildren().add(CreateHboxAnimation(vBox, text));

            pane2.getChildren().addAll(head, line, vBox);
            pane.getChildren().add(pane2);
            ((StackPane) listOfData.get(i).getNode()).toFront();
            ((StackPane) listOfData.get(i).getNode()).getChildren().add(pane);
            ((StackPane) listOfData.get(i).getNode()).setId(String.valueOf(i));

            pane.setOnMouseEntered(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    sequentialTransition.stop();
                    sequentialTransition.setRate(1.0);
                    sequentialTransition.play();
                }
            });

            pane.setOnMouseExited(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    sequentialTransition.stop();
                    sequentialTransition.setRate(-1.0);
                    sequentialTransition.play();
                    vBox.setVisible(false);
                }
            });
        }
    }

    private Animation CreateHboxAnimation(VBox hBox, Text text)
    {
        double height = 23;
        double y = -height / 2.0;
        Rectangle rectangle = new Rectangle();
        rectangle.setArcWidth(10.0);
        rectangle.setArcHeight(10.0);
        rectangle.setHeight(height);
        rectangle.setWidth(height);
        hBox.setClip(rectangle);
        hBox.setMinHeight(height);
        hBox.setMinWidth(height);

        return new Timeline(
                new KeyFrame(Duration.millis(1),
                        new KeyValue(rectangle.widthProperty(), height),
                        new KeyValue(rectangle.heightProperty(), 0),
                        new KeyValue(rectangle.yProperty(), -0),
                        new KeyValue(hBox.visibleProperty(), true),
                        new KeyValue(hBox.layoutXProperty(), y),
                        new KeyValue(hBox.layoutYProperty(), 10)),
                new KeyFrame(Duration.millis(100),
                        new KeyValue(rectangle.heightProperty(), height))
        );
    }

    private Animation CreateLineAnimation(Line line)
    {
        return new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(line.visibleProperty(), true),
                        new KeyValue(line.startYProperty(), 0),
                        new KeyValue(line.endYProperty(), 0)),
                new KeyFrame(Duration.millis(100),
                        new KeyValue(line.endYProperty(), 10)
                )
        );
    }

    private Animation CreateHeadAnimation(Circle circle)
    {
        return new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(circle.visibleProperty(), true),
                        new KeyValue(circle.fillProperty(), Color.WHITE),
                        new KeyValue(circle.radiusProperty(), 2)),

                new KeyFrame(Duration.millis(100),
                        new KeyValue(circle.radiusProperty(), circle.getRadius() * 2)));
    }
}