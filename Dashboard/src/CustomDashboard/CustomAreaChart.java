package CustomDashboard;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class CustomAreaChart extends AreaChart
{
    ObservableList<XYChart.Series<String, Double>> data;
    private double opacity;
    private double dataPointSize;

    public CustomAreaChart(Axis xAxis, Axis yAxis, double dataPointSize, int numberOfSeries)
    {
        super(xAxis, yAxis);
        this.dataPointSize = dataPointSize;
        setData(CreateData(30, numberOfSeries));
        getStylesheets().add(getClass().getClassLoader().getResource("CustomDashboard/CustomAreaChartStyleSheet.css").toExternalForm());
        setLegendVisible(false);
    }

    private void NodeInfoAnimation()
    {
        ObservableList<XYChart.Data<String, Double>> listOfData = data.get(0).getData();

        for(int i = 0; i < listOfData.size(); ++i)
        {
            SequentialTransition sequentialTransition = new SequentialTransition();

            double width = 15;
            double height = 15;
            Pane pane = new Pane();
            pane.setMinSize(width, height);
            pane.setStyle("-fx-background-color: transparent;");

            Pane pane2 = new Pane();
            pane2.setTranslateX(width / 2.0);
            pane2.setTranslateY(height / 2.0);
            pane2.setMinSize(1, 1);
            pane2.setMouseTransparent(true);

            Circle head = new Circle(1, Color.WHITE);
            sequentialTransition.getChildren().add(CreateHeadAnimation(head));

            Line line = new Line();
            line.setStroke(Color.WHITE);
            line.setStrokeWidth(1.0);
            sequentialTransition.getChildren().add(CreateLineAnimation(line));

            HBox hBox = new HBox();
            hBox.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 1.0), new CornerRadii(0.0), new Insets(0))));

            DecimalFormat decimals = new DecimalFormat("#.##");
            Text text = new Text(new StringBuilder("x: ").append(listOfData.get(i).getXValue()).append(" y: ").append(decimals.format(listOfData.get(i).getYValue())).toString());
            text.setFont(Font.font(10));
            hBox.setMargin(text, new Insets(2, 4, 4, 8));

            hBox.getChildren().add(text);
            hBox.setVisible(false);
            sequentialTransition.getChildren().add(CreateHboxAnimation(hBox, text));

            pane2.getChildren().addAll(head, line, hBox);
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
                    hBox.setVisible(false);
                }
            });
        }
    }

    private Animation CreateHboxAnimation(HBox hBox, Text text)
    {
        double height = 20;
        double y = -height / 2.0;
        Rectangle rectangle = new Rectangle();
        rectangle.setArcWidth(10.0);
        rectangle.setArcHeight(10.0);
        rectangle.setWidth(text.getLayoutBounds().getWidth() + 20);
        hBox.setClip(rectangle);
        hBox.setMinWidth(text.getLayoutBounds().getWidth() + 20);

        return new Timeline(
                new KeyFrame(Duration.millis(1),
                        new KeyValue(rectangle.heightProperty(), height),
                        new KeyValue(rectangle.widthProperty(), 0),
                        new KeyValue(rectangle.yProperty(), -0),
                        new KeyValue(hBox.visibleProperty(), true),
                        new KeyValue(hBox.layoutYProperty(), y),
                        new KeyValue(hBox.layoutXProperty(), 20)),
                new KeyFrame(Duration.millis(100),
                        new KeyValue(rectangle.widthProperty(), text.getLayoutBounds().getWidth() + 20))
                );
    }

    private Animation CreateLineAnimation(Line line)
    {
        return new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(line.visibleProperty(), true),
                        new KeyValue(line.startXProperty(), 0),
                        new KeyValue(line.endXProperty(), 0)),
                new KeyFrame(Duration.millis(100),
                        new KeyValue(line.endXProperty(), 20)
                )
        );
    }

    private Animation CreateHeadAnimation(Circle circle)
    {
        return new Timeline(
                new KeyFrame(Duration.ZERO,
                    new KeyValue(circle.visibleProperty(), true),
                        new KeyValue(circle.fillProperty(), Color.WHITE),
                    new KeyValue(circle.radiusProperty(), 1)),

                new KeyFrame(Duration.millis(100),
                        new KeyValue(circle.radiusProperty(), circle.getRadius() * 3)));
    }

    @Override
    protected void layoutChildren()
    {
        super.layoutChildren();
        double opacity = 0.5;
        for(int i = 0; i < data.size(); ++i)
        {
            CreateGradient(data.get(i).getData(), i, opacity);
            //opacity -= 0.1;
        }

        NodeInfoAnimation();
    }

    private void CreateGradient(ObservableList<XYChart.Data<String, Double>> listOfData, int index, double opacity)
    {
        List<Double> coordinates = new ArrayList();
        LinearGradient linearGradient = new LinearGradient(0, 0, 0, 1, true,
                CycleMethod.NO_CYCLE, new Stop(0.0, Color.rgb(68, 163, 219, opacity)),
                new Stop(0.5, Color.rgb(60, 113, 222, opacity)),
                new Stop(1.0, Color.rgb(240, 121, 253, opacity)));

        for(int i = 0; i < listOfData.size(); ++i)
        {
            double x = getXAxis().getDisplayPosition(listOfData.get(i).getXValue());
            double y = getYAxis().getDisplayPosition(listOfData.get(i).getYValue());

            coordinates.add(x);
            coordinates.add(y);
        }

        for(XYChart.Data<String, Double> item : listOfData)
        {
            StackPane sp = (StackPane)item.getNode();
            sp.setPrefWidth(dataPointSize);
            sp.setPrefHeight(dataPointSize);
        }

        coordinates.add(getXAxis().getDisplayPosition(listOfData.get(listOfData.size()-1).getXValue()));
        coordinates.add(getYAxis().getDisplayPosition(0.0));
        coordinates.add(getXAxis().getDisplayPosition(listOfData.get(0).getXValue()));
        coordinates.add(getYAxis().getDisplayPosition(0.0));
        coordinates.toArray();
        Double[] coordinatesArray = new Double[coordinates.size()];

        for(int i = 0; i < coordinates.size(); ++i)
            coordinatesArray[i] = coordinates.get(i);

        Polygon polygon = new Polygon();
        polygon.setId("Polygon");
        polygon.getPoints().addAll(coordinatesArray);
        polygon.setMouseTransparent(true);
        polygon.toBack();

        polygon.setFill(linearGradient);
        getPlotChildren().add(polygon);

        String sbLine = new StringBuilder().append(".default-color").append(String.valueOf(index)).append(".chart-series-area-line").toString();
        String sbAreaFill = new StringBuilder().append(".default-color").append(String.valueOf(index)).append(".chart-series-area-fill").toString();
        Node line = lookup(sbLine);
        Node areaFill = lookup(sbAreaFill);
        line.setStyle("-fx-stroke: #44A3DB");
        areaFill.setStyle("-fx-fill: transparent");
    }

    private ObservableList<Series<String, Double>> CreateData(int dataPointsAmount, int numberOfSeries)
    {
        data = FXCollections.observableArrayList();
        Random random = new Random();

        for(int i = 0; i < numberOfSeries; ++i)
        {
            XYChart.Series<String, Double> a = new XYChart.Series<>();
            a.setName(String.valueOf(i));

            for(int j = 1; j < dataPointsAmount + 1; ++j)
                a.getData().add(new XYChart.Data<String, Double>(Integer.toString(j), random.nextDouble()));

            data.add(a);
        }
        return data;
    }
}