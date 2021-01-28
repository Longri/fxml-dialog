/*******************************************************************************
 * Copyright 2021 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.longri.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FxmlDialog {

    private final static AtomicInteger dialogCount = new AtomicInteger();
    private final static HashMap<Integer, DialogResponse> responseHashMap = new HashMap<>();
    public Pane fxmlContentPane;

    public static DialogResponse show(Scene parentScene, URL contentFxmlUrl, ContentControllerCallBack contentCallBack, DialogResponse... responseTypes) {
        DialogResponse response = DialogResponse.unknown;
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);

            FXMLLoader loader = new FXMLLoader(FxmlDialog.class.getResource("/de/longri/fx/FxmlDialog.fxml"));
            Parent parent = loader.load();

            Scene dialogScene = new Scene(parent);
            FxmlDialog dialog = loader.getController();
            dialog.setStage(dialogStage);

            int dialogNumber = dialogCount.incrementAndGet();
            dialog.setResponseNumber(dialogNumber);

            //set style from parent scene
            dialogScene.getStylesheets().addAll(parentScene.getStylesheets());


            //load content from fxml file
            FXMLLoader contentLoader = new FXMLLoader(contentFxmlUrl);
            dialog.fxmlContentPane.getChildren().add(contentLoader.load());
            contentCallBack.callBack(contentLoader.getController());

            dialogStage.setScene(dialogScene);

            // Calculate the center position of the parent Stage
            Window primaryStage = parentScene.getWindow();
            double centerXPosition = primaryStage.getX() + primaryStage.getWidth() / 2d;
            double centerYPosition = primaryStage.getY() + primaryStage.getHeight() / 2d;

            // Relocate the pop-up Stage
            dialogStage.setOnShown(ev -> {
                dialogStage.setX(centerXPosition - dialogStage.getWidth() / 2d);
                dialogStage.setY(centerYPosition - dialogStage.getHeight() / 2d);
                dialogStage.show();
            });
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
