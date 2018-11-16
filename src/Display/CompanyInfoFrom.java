package Display;

import Calcul.Calculations.Company;
import Calcul.Calculations.Element;
import Utils.DataFromExcel.ExcelParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class CompanyInfoFrom {
    public static void display(Company c) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Информация о предприятии");
        window.setMinWidth(450);
        window.setMinHeight(350);

        Label label1 = new Label(c.toString());
        Label label2 = new Label("Информация о выбросах:");
        //TODO вывести всю информацию if you  brave enough
        ListView<String> listView = new ListView<>();
        ArrayList<String> companyStats = new ArrayList<>();





        for (Element el : c.getElements() ) {
            System.out.println("Element :" + el.getCode() + " Concentration " + el.getMass());
        }



        //ObservableList<String> companyStatsList = ;
        listView.setItems(FXCollections.observableArrayList(companyStats));
        /*TableView table = new TableView();
        table.setEditable(false);
        TableColumn firstCol = new TableColumn("Элемент");
        TableColumn secondCol = new TableColumn("Выброс (г/с)");
        table.getColumns().addAll(firstCol, secondCol);*/

        /*double tableWidth = table.getPrefWidth();
        firstCol.setPrefWidth(tableWidth/ 3 * 2);
        secondCol.setPrefWidth(tableWidth / 3);*/


        Button closeButton = new Button("Закрыть");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(30);
        layout.getChildren().addAll(label1, label2, listView, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 600);
        window.setScene(scene);
        window.showAndWait();
    }
}
