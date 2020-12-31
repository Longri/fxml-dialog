package de.longri.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FxmlDialog {

    private static AtomicInteger dialogCount = new AtomicInteger();
    private static HashMap<Integer, DialogResponse> responseHashMap = new HashMap<>();

    public static DialogResponse show(Scene parentScene) {
        DialogResponse response = DialogResponse.unknown;
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);

            FXMLLoader loader = new FXMLLoader(FxmlDialog.class.getResource("/theme/controls/timedDialog.fxml"));
            Parent parent = loader.load();

            Scene dialogScene = new Scene(parent);

            FxmlDialog timedDialog = loader.getController();

            timedDialog.setStage(dialogStage);

            int dialogNumber = dialogCount.incrementAndGet();
            timedDialog.setResponseNumber(dialogNumber);

            //set style from parent scene
            dialogScene.getStylesheets().addAll(parentScene.getStylesheets());

            dialogStage.setScene(dialogScene);
            dialogStage.showAndWait();
            response = responseHashMap.get(dialogNumber);
            responseHashMap.remove(dialogNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    private void setResponseNumber(int responseNumber) {
        this.responseNumber = responseNumber;
    }

    private Stage dialogStage;
    private int responseNumber;

    private void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    public void onOkClicked(ActionEvent actionEvent) {
        responseHashMap.put(this.responseNumber, DialogResponse.OK);
        this.dialogStage.close();
    }
}
