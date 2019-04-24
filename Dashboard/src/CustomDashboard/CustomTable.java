package CustomDashboard;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class CustomTable
{
    private List<TableData> data;
    private TableView table;
    TableColumn<TableData, String> tableColumn1;
    TableColumn<TableData, String> tableColumn2;
    TableColumn<TableData, Double> tableColumn3;
    TableColumn<TableData, String> tableColumn4;

    public CustomTable()
    {

        GenerateFakeData(10);
    }

    public TableView CreateTable(int cellHeight)
    {
        table = new TableView();

        tableColumn1 = new TableColumn<>("Item");
        tableColumn2 = new TableColumn<>("Customer");
        tableColumn3 = new TableColumn<>("Price");
        tableColumn4 = new TableColumn<>("Product ID");

        tableColumn1.setId("Item");
        tableColumn2.setId("Customer");
        tableColumn3.setId("Price");
        tableColumn4.setId("ProductID");

        tableColumn1.setSortable(false);
        tableColumn2.setSortable(false);
        tableColumn3.setSortable(false);
        tableColumn4.setSortable(false);

        tableColumn1.setCellValueFactory(new PropertyValueFactory<>("item"));
        tableColumn2.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tableColumn3.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableColumn4.setCellValueFactory(new PropertyValueFactory<>("productID"));

        SetCellFactories();
        table.setFixedCellSize(cellHeight);

        tableColumn1.prefWidthProperty().bind(table.widthProperty().multiply((double)1/2));
        tableColumn2.prefWidthProperty().bind(table.widthProperty().multiply((double)1/6));
        tableColumn3.prefWidthProperty().bind(table.widthProperty().multiply((double)1/6));
        tableColumn4.prefWidthProperty().bind(table.widthProperty().multiply((double)1/6));

        tableColumn1.setResizable(false);
        tableColumn2.setResizable(false);
        tableColumn3.setResizable(false);
        tableColumn4.setResizable(false);

        //table.addEventFilter(MouseEvent.MOUSE_DRAGGED, Event::consume);
        table.getColumns().addAll(tableColumn1, tableColumn2, tableColumn3, tableColumn4);
        table.getItems().addAll(data);

        table.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                table.getStylesheets().add(getClass().getClassLoader().getResource("CustomDashboard/CustomTableStyleSheet.css").toExternalForm());
            }
        });

        table.setTranslateX(13);
        table.setMaxWidth(760);

        return table;
    }

    private void GenerateFakeData(int numberOfItems)
    {
        data = new ArrayList<TableData>();
        char[] characters = new char[4];
        Random random = new Random();

        for(int i = 0; i < numberOfItems; ++i)
        {
            int itemMin = 1;
            int itemMax = 4;
            int itemNumber = random.nextInt((itemMax-itemMin) + 1) + itemMin;
            String itemName = "Item " + itemNumber;
            String customerName = "Customer " + (i + 1);
            int price = itemNumber * 100;
            int charMin = 65;
            int charMax = 90;

            for(int j = 0; j < 4; ++j)
            {
                char randomCharacter = (char) (random.nextInt((charMax - charMin) + 1) + charMin);
                characters[j] = randomCharacter;
            }

            String productID = String.valueOf(characters[0] + "-" + characters[1] + characters[2] + characters[3]);
            data.add(new TableData(itemName, customerName, price, productID));
        }
    }

    private void SetCellFactories()
    {
        tableColumn1.setCellFactory(item->new TableCell<TableData, String>()
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
                    setStyle("-fx-background-color: #272a38; -fx-font-weight: normal; -fx-text-fill: #FFFFFF; -fx-alignment: center-left; -fx-text-alignment: center;");
                }
            }
        });

        tableColumn2.setCellFactory(item->new TableCell<TableData, String>()
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
                    setStyle("-fx-background-color: #272a38; -fx-font-weight: normal; -fx-text-fill: #FFFFFF; -fx-alignment: center; -fx-text-alignment: center;");
                }
            }
        });

        tableColumn3.setCellFactory(item->new TableCell<TableData, Double>()
        {
            @Override
            protected void updateItem(Double item, boolean empty)
            {
                super.updateItem(item, empty);
                if(empty)
                    setText(null);
                else
                {
                    String euro = "\u20AC";
                    setText(euro + String.valueOf(item.intValue()));
                    setStyle("-fx-background-color: #272a38; -fx-font-weight: normal; -fx-text-fill: #3b8ebe; -fx-alignment: center; -fx-text-alignment: center;");
                }
            }
        });

        tableColumn4.setCellFactory(item->new TableCell<TableData, String>()
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
                    setStyle("-fx-background-color: #272a38; -fx-font-weight: normal; -fx-text-fill: #FFFFFF; -fx-alignment: center; -fx-text-alignment: center;");
                }
            }
        });
    }
}