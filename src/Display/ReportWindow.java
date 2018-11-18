package Display;

import Calcul.Calculations.Company;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ReportWindow {

    public Label reportLabel;
    public VBox elementsTable;
    public Label costLabel;
    public Label poluLabel;

    private Company company;

    @FXML
    private void initialize() {
        costLabel.setText("$2");
        poluLabel.setText("Высокий");
        poluLabel.setStyle("-fx-text-fill: red");
        System.out.println(company);
    }

    public void ExpandButtonClicked() {
        System.out.println("r");
    }

    public void initData(Company company) {
        this.company = company;
    }
}
