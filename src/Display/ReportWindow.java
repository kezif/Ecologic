package Display;

import Calcul.Calculations.Company;
import Calcul.Calculations.Element;
import Calcul.Calculations.SumElem;
import Utils.DataFromExcel.CatalogOfElements;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ReportWindow {

    public Label reportLabel;
    public VBox elementsTable;
    public Label costLabel;
    public Label poluLabel;
    public TableView table;
    public ListView listSum;
    public ListView listHotnCold;

    private Company company;

    @FXML
    private void initialize() {
        //costLabel.setText("$2");
        //poluLabel.setText("Высокий");
        //poluLabel.setStyle("-fx-text-fill: red");
        //System.out.println(company);
        table.setEditable(false);
        reportLabel.setText("В пределах нормы");
    }

    public void ExpandButtonClicked() {
        System.out.println("r");
    }

    public void initData(Company company) {
        this.company = company;
        //System.out.println(company);
        ArrayList<TableViewElement> elList = new ArrayList<>();
        boolean bad = false;
        for (Element e : company.getElements()) {
            TableViewElement t = new TableViewElement(e.getName(), e.getCm(), e.getCmi(), e.getMPC(), e.getUz());
            elList.add(t);
            if (t.getUzValue() > 100)
                bad = true;
        }
        reportLabel.setText(bad ? "Нарушена норма" : "В пределах нормы");
        reportLabel.setStyle(bad ? "-fx-text-fill: red" : "-fx-text-fill: black");
        fillTableWithAllData(elList);


        ArrayList<SumElem> sumelem = CatalogOfElements.getInstance().sumelem;

        ArrayList<String> lst = new ArrayList<String>();
        for (SumElem el : sumelem) {
            if(company.checkElem(el.getSumelem())!= null){
                int sum = 0;
                String mes = "";
                for (String code : el.getSumelem()) {
                    Element e = company.getElementByCode(code);
                    sum += Math.round(e.getUz()*100f);
                    mes += e.getName() + " + ";
                }
                mes = mes.substring(0,mes.length()-2);
                lst.add(sum + " % - " + mes);
            }
        }
        listSum.setItems(FXCollections.observableArrayList(lst));
        listSum.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String  item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item);
                    setStyle(Integer.valueOf(item.split(" ")[0]) > 100 ? "-fx-background-color: lightcoral;" : "-fx-background-color:#f4f4f4");
                }
            }
        });

    }

    TableColumn elementNameColumn;
    TableColumn cmElementColumn;
    TableColumn cmiElementColumn;
    TableColumn mpcElementColumn;
    TableColumn uzElementColumn;

    private TableView fillTableWithAllData(ArrayList<TableViewElement> elList) {
        final ObservableList<TableViewElement> data = FXCollections.observableArrayList(elList);

        elementNameColumn = new TableColumn("Элемент");
        cmElementColumn = new TableColumn("Конц. [г/м³]");
        cmiElementColumn = new TableColumn("Сми [г/м\u00B3]");
        mpcElementColumn = new TableColumn("ПДК [г/м\u00B3]");
        uzElementColumn = new TableColumn("% допуст.");

        elementNameColumn.setCellValueFactory(
                new PropertyValueFactory<TableViewElement, String>("name")
        );
        cmElementColumn.setCellValueFactory(
                new PropertyValueFactory<TableViewElement, String>("Cm")
        );
        cmiElementColumn.setCellValueFactory(
                new PropertyValueFactory<TableViewElement, String>("Cmi")
        );
        mpcElementColumn.setCellValueFactory(
                new PropertyValueFactory<TableViewElement, String>("MPC")
        );
        uzElementColumn.setCellValueFactory(
                new PropertyValueFactory<TableViewElement, String>("Uz")
        );
        table.setRowFactory(row -> new TableRow<TableViewElement>() {
            @Override
            public void updateItem(TableViewElement item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    if (Integer.valueOf(item.getUzValue()) > 100)
                        setStyle("-fx-background-color:lightcoral; -fx-highlight-text-fill: #FFFFFF;-fx-highlight-color: tomato");
                    else
                        setStyle("-fx-background-color:#f4f4f4; -fx-highlight-text-fill: #000000;-fx-highlight-color: #242424 ");
                }
            }
        });

        table.getColumns().addAll(elementNameColumn, cmElementColumn, cmiElementColumn, mpcElementColumn, uzElementColumn);
        table.setItems(data);
        uzElementColumn.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(uzElementColumn);
        table.sort();
        return table;
    }


    public static class TableViewElement {
        private final SimpleStringProperty name;
        private final SimpleDoubleProperty Cm;
        private final SimpleDoubleProperty Cmi;
        private final SimpleDoubleProperty MPC;
        private final SimpleIntegerProperty Uz;

        double precision = 1000;

        public TableViewElement(String name, Double cm, Double cmi, Double MPC, Double uz) {
            this.name = new SimpleStringProperty(name);
            Cm = new SimpleDoubleProperty(cm);
            Cmi = new SimpleDoubleProperty(cmi);
            this.MPC = new SimpleDoubleProperty(MPC);
            Uz = new SimpleIntegerProperty((int) Math.round(uz * 100));
        }

        public String getName() {
            return name.get();
        }

        public double getCm() {
            return Math.round(Cm.get() * precision) / precision;
        }

        public double getCmi() {
            return Math.round(Cmi.get() * precision) / precision;
        }

        public double getMPC() {
            return Math.round(MPC.get() * precision) / precision;
        }

        public int getUzValue() {
            return Uz.get();
        }

        public String getUz() {
            return Uz.get() + " %";
        }

        @Override
        public String toString() {
            return String.format("Имя - %s Концентрация [г/м\u00B3] - %.3f Сми [г/м\u00B3] - %.3f ПДК [г/м\u00B3] - %.3f Уровень загрязнения  от нормы - %d%%",
                    this.getName(), this.getCm(), this.getCmi(), this.getMPC(), this.getUz());
        }
    }
}
