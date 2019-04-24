package CustomDashboard;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("Duplicates")
public class CustomPieChart
{
    private List<PieChartData> data;
    private List<PieChartData> deepCopy;
    private List<Shape> shapes;
    private Pane pane;
    private StackPane stackPane;
    private GridPane gridPane;
    private Label percentage;
    private Label avg;
    private StackPane sp;
    private double centerX;
    private double centerY;
    private double outerRadius;
    private double innerRadius;
    private double separatorInterval;
    private double paneX;
    private double paneY;
    private double totalValue;
    private double cellHeight;
    private String animPercentage;
    private String animPercentageStyle;
    private String animDataPointText;
    private boolean hover;

    public CustomPieChart(List<PieChartData> data, double outerRadius, double innerRadius, double separatorInterval)
    {
        this.pane = new Pane();
        this.data = data;
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
        this.separatorInterval = separatorInterval;
        this.paneX =  outerRadius * 2;
        this.paneY =  outerRadius * 2;
        this.centerX = this.paneX / 2;
        this.centerY = this.paneY / 2;
        this.shapes = new ArrayList<Shape>();
        this.stackPane = new StackPane();
        this.gridPane = new GridPane();
        percentage = new Label();
        avg = new Label();
        sp = new StackPane();

        pane.setMaxSize(this.paneX, this.paneY);
        //pane.setStyle("-fx-background-color: #FFFFFF;");

        SetColors();
        CreateChart();
    }

    private void CreateChart()
    {
        totalValue = 0;
        double totalAngle = 360;
        double currentAngle = 0;
        double pointsPerDegree;

        avg.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 8; -fx-font-weight: bold");
        avg.setTranslateY(11);
        sp.setMouseTransparent(true);
        sp.getChildren().addAll(percentage, avg);
        gridPane.add(sp, 0, 0);

        for(PieChartData i : this.data)
            totalValue += i.getValue();

        pointsPerDegree = totalValue / totalAngle;

        Arc center = new Arc();
        center.setCenterX(this.centerX);
        center.setCenterY(this.centerY);
        center.setRadiusX(this.innerRadius);
        center.setRadiusY(this.innerRadius);
        center.setType(ArcType.ROUND);
        center.setStartAngle(0);
        center.setLength(totalAngle);

        for(int i = 0; i < this.data.size(); ++i)
        {
            PieChartData pieChartData = this.data.get(i);
            double degrees = pieChartData.getValue() / pointsPerDegree;
            Arc arc = new Arc();
            arc.setCenterX(this.centerX);
            arc.setCenterY(this.centerY);
            double maxRadius = this.outerRadius * 2;
            double modifiedRadius = this.outerRadius + ((maxRadius - this.outerRadius) * (degrees / totalAngle));
            arc.setRadiusX(modifiedRadius);
            arc.setRadiusY(modifiedRadius);
            arc.setType(ArcType.ROUND);
            arc.setStartAngle(currentAngle);
            arc.setLength(degrees);
            currentAngle += degrees;

            Arc separation = new Arc();
            separation.setCenterX(this.centerX);
            separation.setCenterY(this.centerY);
            separation.setRadiusX(modifiedRadius);
            separation.setRadiusY(modifiedRadius);
            separation.setType(ArcType.ROUND);
            separation.setLength(separatorInterval);
            separation.setStartAngle(currentAngle - (separatorInterval / 2));

            Shape subtracted = Shape.subtract(arc, center);
            subtracted = Shape.subtract(subtracted, separation);
            subtracted.setFill(pieChartData.getColor());
            subtracted.setId(new StringBuilder("Data Point ").append(i + 1).toString());

            Shape finalSubtracted = subtracted;
            subtracted.setOnMouseExited(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    hover = false;
                    finalSubtracted.setFill(pieChartData.getColor());
                    finalSubtracted.setScaleX(1d);
                    finalSubtracted.setScaleY(1d);
                    finalSubtracted.setEffect(null);
                    percentage.setText(animPercentage);
                    percentage.setStyle(animPercentageStyle);
                    avg.setText(animDataPointText);
                }
            });

            Shape finalSubtracted1 = subtracted;
            subtracted.setOnMouseEntered(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    hover = true;
                    RadialGradient radialGradient = new RadialGradient(0, 0, paneX / 2, paneY / 2, 50, false, CycleMethod.REFLECT,
                            new Stop(0, Color.rgb(255, 255, 255, 1)), new Stop(1, pieChartData.getColor()));
                    finalSubtracted.setFill(radialGradient);
                    AddGlowEffect(finalSubtracted, pieChartData.getColor(), 20d);

                    StringBuilder sb = new StringBuilder().append("-fx-text-fill: ").append(Utilities.ConvertToHex(pieChartData.getColor()))
                            .append("; -fx-font-weight: bold; -fx-font-size: 13");

                    percentage.setText("+" + String.valueOf((int)(pieChartData.getValue() / totalValue * 100)) + "%");
                    percentage.setStyle(sb.toString());
                    String text = pieChartData.getName();
                    avg.setText(text);
                }
            });

            this.shapes.add(subtracted);
            this.pane.getChildren().add(subtracted);
        }

        TableView table = CreateTable(20, 3);
        VBox rectangleHolder = CreateRectangles(7.5, 3, 12);

        GridPane tableHolder = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(10);
        col2.setPercentWidth(90);
        tableHolder.getColumnConstraints().addAll(col1, col2);
        tableHolder.add(rectangleHolder, 0, 0);
        tableHolder.add(table, 1, 0);
        tableHolder.setGridLinesVisible(false);
        tableHolder.setMouseTransparent(true);

        RowConstraints gridRow0 = new RowConstraints();
        RowConstraints gridRow1 = new RowConstraints();
        ColumnConstraints gridcol0 = new ColumnConstraints();
        gridRow0.setPercentHeight(75);
        gridRow1.setPercentHeight(25);
        gridcol0.setPercentWidth(100);
        gridPane.getRowConstraints().addAll(gridRow0, gridRow1);
        gridPane.getColumnConstraints().addAll(gridcol0);
        gridPane.add(this.pane, 0, 0);
        gridPane.add(tableHolder, 0, 1);
        //gridPane.setStyle("-fx-border-color: #323540");
        GridPane.setHalignment(this.pane, HPos.CENTER);
        GridPane.setValignment(this.pane, VPos.CENTER);
        GridPane.setHalignment(rectangleHolder, HPos.CENTER);
        GridPane.setValignment(rectangleHolder, VPos.CENTER);
        gridPane.setMaxWidth(245);
        gridPane.setTranslateX(8);

        AnimateChart();
        //SlowlyRotate();
    }

    private VBox CreateRectangles(double spacing, int rows, int rectangleSize)
    {
        VBox rectangleHolder = new VBox(spacing);
        rectangleHolder.setAlignment(Pos.CENTER);

        for(int i = 0; i < rows; ++i)
        {
            Rectangle rect = new Rectangle(rectangleSize, rectangleSize);
            rect.setFill(deepCopy.get(i).getColor());
            rectangleHolder.getChildren().add(rect);
        }
        return rectangleHolder;
    }

    public GridPane GetPane()
    {
        return this.gridPane;
    }

    public List<PieChartData> GetListChartData()
    {
        return this.data;
    }

    private void SetColors()
    {
        Color color1 = Color.rgb(68, 163, 219, 1);
        Color color2 = Color.rgb(240, 121, 253, 1);
        Collections.sort(this.data);

        for(int i = 0; i < this.data.size(); ++i)
        {
            float ratio = (float) i / (float) this.data.size();
            float red = (float)(color2.getRed() * ratio + color1.getRed() * (1-ratio));
            float green = (float)(color2.getGreen() * ratio + color1.getGreen() * (1-ratio));
            float blue = (float)(color2.getBlue() * ratio + color1.getBlue() * (1-ratio));
            this.data.get(i).setColor(Color.color(red, green, blue));
        }
    }

    private TableView CreateTable(int cellHeight, int rows)
    {
        TableView table = new TableView();
        TableColumn<PieChartData, String> tableColumn1 = new TableColumn<>("Name");
        TableColumn<PieChartData, Double> tableColumn2 = new TableColumn("Values");
        tableColumn1.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumn1.setCellFactory(item->new TableCell<PieChartData, String>()
        {
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                if(empty)
                    setText(null);
                else
                {
                    setText(item);
                    setStyle("-fx-background-color: #272a38; -fx-font-weight: normal; -fx-text-fill: #FFFFFF; -fx-alignment: center-left; -fx-text-alignment: center; -fx-font-size: 9;"); //272a38
                }
            }
        });
        tableColumn2.setStyle("-fx-alignment: CENTER-RIGHT");
        tableColumn2.setCellValueFactory(new PropertyValueFactory<>("value"));
        tableColumn2.setCellFactory(item->new TableCell<PieChartData, Double>()
        {
            @Override
            protected void updateItem(Double item, boolean empty)
            {
                super.updateItem(item, empty);
                if(empty)
                    setText(null);
                else
                {
                    setText("+" + String.valueOf((int)(item / totalValue * 100)) + "%");
                    setStyle("-fx-background-color: #272a38; -fx-font-weight: bold; -fx-text-fill: #3b8ebe; -fx-alignment: center-right; -fx-font-size: 9;");
                }
            }
        });

        deepCopy = new ArrayList<PieChartData>(this.data.size());
        for(PieChartData c : this.data)
            deepCopy.add(new PieChartData(c));

        List<PieChartData> displayedData = new ArrayList<PieChartData>();

        Collections.sort(deepCopy);
        Collections.reverse(deepCopy);
        for(int i = 0; i < rows; ++i)
            displayedData.add(deepCopy.get(i));

        table.setFixedCellSize(cellHeight);
        this.cellHeight = cellHeight;
        double tableHeight = displayedData.size() * table.getFixedCellSize();
        table.setMinHeight(tableHeight);
        table.setMaxHeight(tableHeight);
        table.setPrefHeight(tableHeight);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.addEventFilter(ScrollEvent.ANY, Event::consume);
        table.getStylesheets().add(getClass().getClassLoader().getResource("CustomDashboard/CustomPieChartStyleSheet.css").toExternalForm());

        table.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                TableHeaderRow tableHeaderRow = (TableHeaderRow) table.lookup("TableHeaderRow");
                tableHeaderRow.setVisible(false);
                tableHeaderRow.setMaxHeight(0);
                tableHeaderRow.setMinHeight(0);
                tableHeaderRow.setPrefHeight(0);
            }
        });

        table.getColumns().addAll(tableColumn1, tableColumn2);
        table.getItems().addAll(displayedData);
        return table;
    }

    private void AddGlowEffect(Shape shape, Color color, double glowStrength)
    {
        DropShadow glow = new DropShadow();
        glow.setOffsetX(0d);
        glow.setOffsetY(0d);
        glow.setColor(color);
        glow.setWidth(glowStrength);
        glow.setHeight(glowStrength);
        shape.setEffect(glow);
    }

    private void SlowlyRotate()
    {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(this.pane.rotateProperty(), 0, Interpolator.LINEAR)),
                new KeyFrame(Duration.millis(200000), new KeyValue(this.pane.rotateProperty(), 360, Interpolator.LINEAR)));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void AnimateChart()
    {
        int[] counter = {0};
        RadialGradient[] radialGradient = new RadialGradient[1];
        List<Color> originalColors = new ArrayList<Color>();

        for(int i = 0; i < shapes.size(); ++i)
            originalColors.add((Color)shapes.get(i).getFill());

        Timeline timeline = new Timeline(

                new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                Color originalColor = originalColors.get(counter[0]);
                radialGradient[0] = new RadialGradient(0, 0, paneX / 2, paneY / 2, 50, false, CycleMethod.REFLECT,
                        new Stop(0, Color.rgb(255, 255, 255, 1)), new Stop(1, originalColor));

                shapes.get(counter[0]).setFill(radialGradient[0]);
                AddGlowEffect(shapes.get(counter[0]), originalColor, 20d);

                animPercentage = "+" + String.valueOf((int)(data.get(counter[0]).getValue() / totalValue * 100)) + "%";
                animPercentageStyle = new StringBuilder("-fx-text-fill: ").append(Utilities.ConvertToHex(originalColor))
                        .append("; -fx-font-weight: bold; -fx-font-size: 13").toString();
                animDataPointText = data.get(counter[0]).getName();

                if(!hover)
                {
                    percentage.setText(animPercentage);
                    percentage.setStyle(animPercentageStyle);
                    avg.setText(animDataPointText);
                }
            }
        }),
                new KeyFrame(Duration.millis(3000), new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                Color originalColor = originalColors.get(counter[0]);
                shapes.get(counter[0]).setFill(originalColor);
                shapes.get(counter[0]).setEffect(null);

                counter[0]++;
                if(counter[0] >= shapes.size())
                    counter[0] = 0;
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}