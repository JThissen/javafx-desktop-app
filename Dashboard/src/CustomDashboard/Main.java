//Custom dashboard made by Joost Thissen.
//The result of learning javafx for roughly ~1.5 weeks.

package CustomDashboard;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class Main extends Application
{
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private String hoverStyle;
    private String idleStyle;
    private String[] buttonLabels;
    private String[] buttonPathLabels;
    private Button closeButton;
    private Button minimizeButton;
    private SplitPane splitPane;
    private GridPane dashboard;
    private GridPane empty;
    private List<Button> buttonList = new ArrayList<Button>();
    private List<Button> titleBarButtons = new ArrayList<Button>();
    private List<String> buttonPaths = new ArrayList<String>();
    private List<String> titleBarButtonPaths = new ArrayList<String>();
    private List<HBox> Hboxes = new ArrayList<HBox>();
    private Label fpsLabel = new Label();

    private long[] frameTimes;
    private int frameTimeIndex;
    private boolean arrayFilled;

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        frameTimes = new long[100];
        frameTimeIndex = 0;
        arrayFilled = false;
        closeButton = new Button();
        minimizeButton = new Button();
        idleStyle = "-fx-background-color: transparent; -fx-text-fill: #3b8ebe; -fx-background-radius: 0";
        hoverStyle = "-fx-background-color: #081f4b; -fx-text-fill: #3b8ebe; -fx-background-radius: 0";
        fpsLabel.setStyle("-fx-text-fill: #FFFFFF");
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #090d14"); //#730f84
        vbox.setPrefWidth(200);
        vbox.setMinWidth(0.0);
        vbox.setMaxWidth(200);

        StackPane pane = new StackPane();
        pane.setPrefSize(vbox.getPrefWidth(), 100);
        pane.setStyle("-fx-background-color: #060e1e");
        Label logoLabel = new Label("LOGO");
        logoLabel.setStyle("-fx-text-fill: #0a58b5");
        pane.setAlignment(logoLabel, Pos.CENTER);
        pane.getChildren().add(logoLabel);

        buttonLabels = new String[]{"Dashboard", "Reports", "Archive", "Social", "Users", "Documents", "Favorites", "Tools", "Settings"};
        buttonPathLabels = new String[]
                {
                        "M21 3H3c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h5v2h8v-2h5c1.1 0 1.99-.9 1.99-2L23 5c0-1.1-.9-2-2-2zm0 14H3V5h18v12zm-2-9H8v2h11V8zm0 4H8v2h11v-2zM7 8H5v2h2V8zm0 4H5v2h2v-2z",
                        "M23 8c0 1.1-.9 2-2 2-.18 0-.35-.02-.51-.07l-3.56 3.55c.05.16.07.34.07.52 0 1.1-.9 2-2 2s-2-.9-2-2c0-.18.02-.36.07-.52l-2.55-2.55c-.16.05-.34.07-.52.07s-.36-.02-.52-.07l-4.55 4.56c.05.16.07.33.07.51 0 1.1-.9 2-2 2s-2-.9-2-2 .9-2 2-2c.18 0 .35.02.51.07l4.56-4.55C8.02 9.36 8 9.18 8 9c0-1.1.9-2 2-2s2 .9 2 2c0 .18-.02.36-.07.52l2.55 2.55c.16-.05.34-.07.52-.07s.36.02.52.07l3.55-3.56C19.02 8.35 19 8.18 19 8c0-1.1.9-2 2-2s2 .9 2 2z",
                        "M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z",
                        "M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z",
                        "M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z",
                        "M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm-1 4l6 6v10c0 1.1-.9 2-2 2H7.99C6.89 23 6 22.1 6 21l.01-14c0-1.1.89-2 1.99-2h7zm-1 7h5.5L14 6.5V12z",
                        "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z",
                        "M22.7 19l-9.1-9.1c.9-2.3.4-5-1.5-6.9-2-2-5-2.4-7.4-1.3L9 6 6 9 1.6 4.7C.4 7.1.9 10.1 2.9 12.1c1.9 1.9 4.6 2.4 6.9 1.5l9.1 9.1c.4.4 1 .4 1.4 0l2.3-2.3c.5-.4.5-1.1.1-1.4z",
                        "M15.95 10.78c.03-.25.05-.51.05-.78s-.02-.53-.06-.78l1.69-1.32c.15-.12.19-.34.1-.51l-1.6-2.77c-.1-.18-.31-.24-.49-.18l-1.99.8c-.42-.32-.86-.58-1.35-.78L12 2.34c-.03-.2-.2-.34-.4-.34H8.4c-.2 0-.36.14-.39.34l-.3 2.12c-.49.2-.94.47-1.35.78l-1.99-.8c-.18-.07-.39 0-.49.18l-1.6 2.77c-.1.18-.06.39.1.51l1.69 1.32c-.04.25-.07.52-.07.78s.02.53.06.78L2.37 12.1c-.15.12-.19.34-.1.51l1.6 2.77c.1.18.31.24.49.18l1.99-.8c.42.32.86.58 1.35.78l.3 2.12c.04.2.2.34.4.34h3.2c.2 0 .37-.14.39-.34l.3-2.12c.49-.2.94-.47 1.35-.78l1.99.8c.18.07.39 0 .49-.18l1.6-2.77c.1-.18.06-.39-.1-.51l-1.67-1.32zM10 13c-1.65 0-3-1.35-3-3s1.35-3 3-3 3 1.35 3 3-1.35 3-3 3z"
                };

        button1 = new Button();
        button2 = new Button();
        button3 = new Button();
        button4 = new Button();
        button5 = new Button();
        button6 = new Button();
        button7 = new Button();
        button8 = new Button();
        button9 = new Button();

        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
        buttonList.add(button5);
        buttonList.add(button6);
        buttonList.add(button7);
        buttonList.add(button8);
        buttonList.add(button9);

        for(int j = 0; j < buttonList.size(); ++j)
        {
            buttonList.get(j).setText(buttonLabels[j]);
            buttonList.get(j).setId(buttonLabels[j]);
            buttonPaths.add(buttonPathLabels[j]);
        }

        closeButton.setId("closeButton");
        minimizeButton.setId("minimizeButton");
        titleBarButtons.add(minimizeButton);
        titleBarButtons.add(closeButton);
        titleBarButtonPaths.add("M19 13H5v-2h14v2z");
        titleBarButtonPaths.add("M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z");

        for(int i = 0; i < titleBarButtons.size(); ++i)
        {
            SVGPath path = new SVGPath();
            path.setScaleX(0.6);
            path.setScaleY(0.6);
            path.setContent(titleBarButtonPaths.get(i));
            path.setStyle("-fx-fill: #3b8ebe");
            titleBarButtons.get(i).setGraphic(path);
            titleBarButtons.get(i).setStyle("-fx-background-color: transparent;");
        }

        for(int i = 0; i < buttonPaths.size(); ++i)
        {
            SVGPath path = new SVGPath();
            path.setContent(buttonPaths.get(i));
            path.setStyle("-fx-fill: #3b8ebe");
            Pane p = new Pane();
            p.setMinWidth(40);
            p.setMinHeight(40);
            p.setStyle("-fx-background-color: transparent");
            p.getChildren().add(path);
            path.setTranslateX(10);
            path.setTranslateY(10);

            buttonList.get(i).setGraphic(p);
            buttonList.get(i).setStyle("-fx-background-color: transparent; -fx-text-fill: #3b8ebe; -fx-background-radius: 0");
            buttonList.get(i).setPrefSize(vbox.getPrefWidth(), 50);
            buttonList.get(i).setAlignment(Pos.BASELINE_LEFT);
            buttonList.get(i).setContentDisplay(ContentDisplay.LEFT);
            buttonList.get(i).setGraphicTextGap(20);
        }

        button1.setStyle(hoverStyle);
        ButtonHandlers();

        for(int i = 0; i < buttonList.size(); ++i)
            vbox.getChildren().add(buttonList.get(i));

        CustomRipple customRipple = new CustomRipple();
        ButtonEffect buttonEffect = new ButtonEffect();
        splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setPadding(new Insets(0));

        HBox titleBar = new HBox(minimizeButton, closeButton);
        titleBar.setAlignment(Pos.TOP_RIGHT);
        titleBar.setStyle("-fx-background-color: #08142b;");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(splitPane);
        borderPane.setTop(titleBar);

        final double[] xOffset = new double[1];
        final double[] yOffset = new double[1];

        titleBar.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                xOffset[0] = primaryStage.getX() - event.getScreenX();
                yOffset[0] = primaryStage.getY() - event.getScreenY();
            }
        });

        titleBar.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                primaryStage.setX(event.getScreenX() + xOffset[0]);
                primaryStage.setY(event.getScreenY() + yOffset[0]);
                primaryStage.setOpacity(0.75);
            }
        });

        titleBar.setOnMouseReleased(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                primaryStage.setOpacity(1);
            }
        });

        ToggleButton toggleButton = new ToggleButton("TOGGLE");
        toggleButton.setStyle("-fx-background-color: #081f4b; -fx-text-fill: #4782c5; -fx-background-radius: 0");
        HBox hBox = new HBox();
        hBox.getChildren().addAll(toggleButton, fpsLabel);
        hBox.setAlignment(Pos.CENTER);

        dashboard = new GridPane();
        empty = new GridPane();
        empty.setStyle("-fx-background-color: #272a38");

        dashboard.setPadding(new Insets(0, 10, 0, 10));
        dashboard.setVgap(10);
        dashboard.setHgap(10);
        dashboard.setGridLinesVisible(false);

        List<PieChartData> listPieChartData = new ArrayList<PieChartData>();
        for(int i = 0; i < 7; ++i)
            listPieChartData.add(new PieChartData(i));

        ColumnConstraints col0 = new ColumnConstraints();
        ColumnConstraints col1 = new ColumnConstraints();
        RowConstraints row0 = new RowConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        col0.setPercentWidth(75);
        col1.setPercentWidth(25);
        row0.setPercentHeight(7.5);
        row1.setPercentHeight(37.5);
        row2.setPercentHeight(30);
        row3.setPercentHeight(25);
        dashboard.getColumnConstraints().addAll(col0, col1);
        dashboard.getRowConstraints().addAll(row0, row1, row2, row3);

        CustomPieChart customPieChart = new CustomPieChart(listPieChartData, 65, 50, 10);

        CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.setGapStartAndEnd(false);
        categoryAxis.setStartMargin(0.0);
        categoryAxis.setEndMargin(0.0);
        NumberAxis numberAxis = new NumberAxis(0, 1, 0.1);
        numberAxis.setMinorTickVisible(false);

        CustomAreaChart customAreaChart = new CustomAreaChart(categoryAxis, numberAxis,  1, 1);

        CategoryAxis categoryAxisBarChart = new CategoryAxis();
        categoryAxis.setStartMargin(0.0);
        categoryAxis.setEndMargin(0.0);

        NumberAxis numberAxisBarChart = new NumberAxis(0.0, 1.0, 0.1);
        numberAxisBarChart.setMinorTickVisible(false);

        CustomBarChart customBarChart = new CustomBarChart(categoryAxisBarChart, numberAxisBarChart);

        CustomTable customTable = new CustomTable();

        Label monthlySummaryLabel = new Label("Monthly Statistics");
        monthlySummaryLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold");
        monthlySummaryLabel.setTranslateX(15);
        dashboard.add(monthlySummaryLabel, 0, 0);
        dashboard.add(customPieChart.GetPane(), 1, 1 );
        dashboard.add(customAreaChart, 0, 1 );
        dashboard.add(customTable.CreateTable(30), 0, 2);
        dashboard.add(customBarChart, 1, 2);

        Color gaugeColor1 = Color.rgb(68, 163, 219, 1);
        Color gaugeColor2 = Color.rgb(68, 100, 219, 1);
        Color gaugeColor3 = Color.rgb(124, 121, 253, 1);
        Color gaugeColor4 = Color.rgb(240, 121, 253, 1);

        CustomGauge gauge1 = new CustomGauge(50.0, 15.0, "ITEM 1", gaugeColor1);
        CustomGauge gauge2 = new CustomGauge(50.0, 15.0, "ITEM 2", gaugeColor2);
        CustomGauge gauge3 = new CustomGauge(50.0, 15.0, "ITEM 3", gaugeColor3);
        CustomGauge gauge4 = new CustomGauge(50.0, 15.0, "ITEM 4", gaugeColor4);

        Pane gauge1Pane = gauge1.CreatePane();
        Pane gauge2Pane = gauge2.CreatePane();
        Pane gauge3Pane = gauge3.CreatePane();
        Pane gauge4Pane = gauge4.CreatePane();

        GridPane gridPaneSecondary = new GridPane();
        ColumnConstraints gridPaneSecondarycol0 = new ColumnConstraints();
        ColumnConstraints gridPaneSecondarycol1 = new ColumnConstraints();
        ColumnConstraints gridPaneSecondarycol2 = new ColumnConstraints();
        ColumnConstraints gridPaneSecondarycol3 = new ColumnConstraints();
        RowConstraints gridPaneSecondaryrow0 = new RowConstraints();

        gridPaneSecondarycol0.setPercentWidth(25);
        gridPaneSecondarycol1.setPercentWidth(25);
        gridPaneSecondarycol2.setPercentWidth(25);
        gridPaneSecondarycol3.setPercentWidth(25);
        gridPaneSecondaryrow0.setPercentHeight(100);

        gridPaneSecondary.getColumnConstraints().addAll(gridPaneSecondarycol0, gridPaneSecondarycol1, gridPaneSecondarycol2, gridPaneSecondarycol3);
        gridPaneSecondary.getRowConstraints().add(gridPaneSecondaryrow0);
        gridPaneSecondary.add(gauge1Pane, 0, 0);
        gridPaneSecondary.add(gauge2Pane, 1, 0);
        gridPaneSecondary.add(gauge3Pane, 2, 0);
        gridPaneSecondary.add(gauge4Pane, 3, 0);
        gridPaneSecondary.setGridLinesVisible(false);

        dashboard.add(gridPaneSecondary, 0, 3);
        dashboard.setId("Dashboard");

        splitPane.getItems().addAll(vbox, dashboard);

        for(Button b : buttonList)
        {
            b.setOnMousePressed(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    customRipple.createRipple(event.getSceneX(), event.getSceneY(), borderPane, b);

                    String buttonID = ((Button)event.getSource()).getId();
                    if(buttonID.equals("Dashboard"))
                    {
                        if(splitPane.getItems().contains(dashboard))
                            return;
                        else
                        {
                            splitPane.getItems().remove(empty);
                            splitPane.getItems().add(1, dashboard);
                        }
                    }
                    else
                    {
                        button1.setStyle(idleStyle);
                        splitPane.getItems().remove(dashboard);
                        splitPane.getItems().remove(empty);
                        splitPane.getItems().add(1, empty);
                    }
                    splitPane.lookup(".split-pane-divider").setStyle("-fx-background-color: #0d131e; -fx-padding: 0 1 0 1");
                    splitPane.lookup(".split-pane-divider").setMouseTransparent(true);
                }
            });
        }

        for(Button b : titleBarButtons)
        {
            b.setOnMousePressed(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    if(((Node)event.getSource()).getId().equals("minimizeButton"))
                        primaryStage.setIconified(true);
                    else  if(((Node)event.getSource()).getId().equals("closeButton"))
                        Platform.exit();
                }
            });
        }

        Scene scene = new Scene(borderPane, 1280, 720);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Custom App");
        primaryStage.show();

        splitPane.getDividers().get(0).positionProperty().set(200/scene.getWidth());
        splitPane.lookup(".split-pane-divider").setMouseTransparent(true);
        splitPane.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                toggleButton.setSelected(newValue.doubleValue() > 0.05);
            }
        });

        splitPane.lookup(".split-pane-divider").setStyle("-fx-background-color: #0d131e; -fx-padding: 0 1 0 1");
        dashboard.setStyle("-fx-background-color: #272a38");

        toggleButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                KeyValue keyValue;
                if(toggleButton.isSelected())
                {
                    keyValue = new KeyValue(splitPane.getDividers().get(0).positionProperty(), 200 / scene.getWidth());
                    toggleButton.setStyle("-fx-background-color: #081f4b; -fx-text-fill: #4782c5; -fx-background-radius: 0");
                    double startTime = 0;
                    for(int b = 0; b < buttonList.size(); b++)
                    {
                        buttonEffect.CreateEffect(buttonList.get(b).getLayoutX(), buttonList.get(b).getLayoutY() + titleBar.getHeight(), buttonList.get(b), borderPane, 200, 250, startTime);
                        startTime += 15;
                    }
                }
                else
                {
                    keyValue = new KeyValue(splitPane.getDividers().get(0).positionProperty(), 0.0);
                    toggleButton.setStyle("-fx-background-color: #090d14; -fx-text-fill: #4782c5; -fx-background-radius: 0");
                    toggleButton.setSelected(false);
                }

                new Timeline(new KeyFrame(Duration.millis(250), keyValue)).play();
            }
        });
    }

    private void ButtonHandlers()
    {
        for(Button b : buttonList)
        {
            b.setOnMouseEntered(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    b.setStyle(hoverStyle);
                }
            });

            b.setOnMouseExited(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    b.setStyle(idleStyle);

                    if(splitPane.getItems().contains(dashboard))
                        button1.setStyle(hoverStyle);
                }
            });
        }

        for(Button b : titleBarButtons)
        {
            b.setOnMouseEntered(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    b.setStyle(hoverStyle);
                }
            });

            b.setOnMouseExited(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    b.setStyle(idleStyle);
                }
            });
        }
    }

    private void SetFPSLabel(double frameRate)
    {
        fpsLabel.setText(String.format("FPS: %.3f", frameRate));
    }

    private void ShowFPS()
    {
        AnimationTimer animationTimer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                long oldFrameTime = frameTimes[frameTimeIndex] ;
                frameTimes[frameTimeIndex] = now ;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
                if (frameTimeIndex == 0) {
                    arrayFilled = true ;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime ;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
                    double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;
                    SetFPSLabel(frameRate);
                }
            }
        };
        animationTimer.start();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }
}