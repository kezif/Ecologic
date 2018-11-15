package Display;

import Generation.Region;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegionInfoFrom {
    public static void display(Region region) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Информация об области");
        window.setMinWidth(350);
        window.setMinHeight(250);

        Label label = new Label();
        label.setText(String.format("Температура воздуха Тв [\u00B0С]: %.1f\n\n" +
                "Максимальная скорость ветра Uм [м/с]: %.2f\n\n" +
                "Среднегодовая скорость ветра U [м/с]: %.2f", region.getTemp(), region.getSpeedMax(), region.getSpeedAv()));
        Button closeButton = new Button("Закрыть");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(30);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 200,100);
        window.setScene(scene);
        window.showAndWait();
    }
}
