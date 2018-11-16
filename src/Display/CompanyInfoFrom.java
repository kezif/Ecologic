package Display;

import Calcul.Calculations.Company;
import Calcul.Calculations.Element;
import Utils.DataFromExcel.CatalogOfElements;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;


public class CompanyInfoFrom {
    public static void display(Company c) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Информация о предприятии");
        double width = 450;
        window.setMinWidth(width);
        window.setMinHeight(350);

        Label label1 = new Label(c.toString());
        Label label2 = new Label("Информация о выбросах:");


        TableView table = new TableView();
        table.setEditable(false);

        //table.setPrefWidth(width * .9);
        TableColumn elementNameColumn = new TableColumn("Элемент");
        elementNameColumn.setPrefWidth(width / 3 * 1.75);
        TableColumn MassElementColumn = new TableColumn("Выброс (г/с)");
        MassElementColumn.setPrefWidth(width / 3);

        table.getColumns().addAll(elementNameColumn, MassElementColumn);


        CatalogOfElements catEl = CatalogOfElements.getInstance();
        ArrayList<TableViewElement> elList = new ArrayList<>();
        for (Element el : c.getElements() ) {
            elList.add(new TableViewElement(catEl.getElementByCode(el.getCode()).getName(), el.getMass()));
            //System.out.println(catEl.getElementByCode(el.getCode()).getName() + " Concentration - " + el.getMass());
        }
        final ObservableList<TableViewElement> data = FXCollections.observableArrayList(elList);

        elementNameColumn.setCellValueFactory(
                new PropertyValueFactory<TableViewElement,String>("name")
        );
        MassElementColumn.setCellValueFactory(
                new PropertyValueFactory<TableViewElement,Double>("value")
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(data);


        Button closeButton = new Button("Закрыть");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(30);
        layout.getChildren().addAll(label1, label2, table, closeButton);
        layout.setAlignment(Pos.CENTER);


        Scene scene = new Scene(layout, 300, 600);
        window.setScene(scene);
        window.showAndWait();
    }

    public static class TableViewElement{
        private final SimpleStringProperty name;
        private final SimpleDoubleProperty value;
        private TableViewElement(String name, double value){
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleDoubleProperty(value);
        }

        public String getName() {
            return name.get();
        }

        public double getValue() {
            return value.get();
        }

    }

    /*public static class Person {
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;

        private Person(String fName, String lName, String email) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
        }

        public String getFirstName() {
            return firstName.get();
        }
        public void setFirstName(String fName) {
            firstName.set(fName);
        }

        public String getLastName() {
            return lastName.get();
        }
        public void setLastName(String fName) {
            lastName.set(fName);
        }

        public String getEmail() {
            return email.get();
        }
        public void setEmail(String fName) {
            email.set(fName);
        }

    }*/
}
