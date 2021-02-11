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

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.effect.BlendMode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class TestViewController {

    private Stage primaryStage;

    public void onDialog1Clicked(ActionEvent actionEvent) {
        actionEvent.consume();
        URL url = TestView.class.getResource("/de/longri/fx/TestContent.fxml");
        FxmlDialog dialog = FxmlDialog.getDialog(url, DialogResponse.OK, DialogResponse.Cancel);
        TestContentController contentController = (TestContentController) (dialog != null ? dialog.getContentController() : null);
        if (contentController != null) {
            contentController.label.setText("Hallo Dialog Label");
            contentController.button.setOnAction(event -> System.out.println("Hello World from Dialog caller, " +
                    "overrides the action event handler from controller!"));
        }
        DialogResponse response = DialogResponse.unknown;
        if (dialog != null) {
            response = dialog.show(primaryStage.getScene());
        }
        System.out.println("Response: " + response);
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void onDialog2Clicked(ActionEvent actionEvent) {
        actionEvent.consume();
        URL url = TestView.class.getResource("/de/longri/fx/TestContent.fxml");
        FxmlDialog dialog = FxmlDialog.getDialog(url, DialogResponse.OK, DialogResponse.Apply);
        TestContentController contentController = (TestContentController) (dialog != null ? dialog.getContentController() : null);
        if (contentController != null) {
            contentController.label.setText("Hallo Dialog Label");
            contentController.button.setOnAction(event -> System.out.println("Hello World from Dialog caller, " +
                    "overrides the action event handler from controller!"));
        }
        DialogResponse response = DialogResponse.unknown;
        if (dialog != null) {
            response = dialog.show(primaryStage.getScene());
        }
        System.out.println("Response: " + response);
    }

    public void onDialog3Clicked(ActionEvent actionEvent) {
        actionEvent.consume();
    }

    public void onDialog4Clicked(ActionEvent actionEvent) {
        actionEvent.consume();
        URL url = TestView.class.getResource("/de/longri/fx/TestContent.fxml");
        FxmlDialog dialog = FxmlDialog.getTimedDialog(url, Duration.seconds(5), DialogResponse.OK, DialogResponse.Cancel);
        TestContentController contentController = (TestContentController) (dialog != null ? dialog.getContentController() : null);
        if (contentController != null) {
            contentController.label.setText("Hallo Dialog Label");
            contentController.button.setOnAction(event -> System.out.println("Hello World from Dialog caller, " +
                    "overrides the action event handler from controller!"));
        }
        DialogResponse response = DialogResponse.unknown;
        if (dialog != null) {
            response = dialog.show(primaryStage.getScene());
        }
        System.out.println("Response: " + response);
    }

    public void onDialog5Clicked(ActionEvent actionEvent) {
        actionEvent.consume();
        DialogResponse response = FxmlDialog.showAndWaitTimedAlert(Duration.seconds(10), Alert.AlertType.CONFIRMATION,
                null, "Content Text for question", primaryStage.getScene());
        System.out.println("Response: " + response);
    }

    public void onDialog6Clicked(ActionEvent actionEvent) {
        actionEvent.consume();
        DialogResponse response = FxmlDialog.showAndWaitTimedAlert(Duration.seconds(10), Alert.AlertType.ERROR,
                null, "Content Text for Error ", primaryStage.getScene());
        System.out.println("Response: " + response);
    }

    public void onDialog7Clicked(ActionEvent actionEvent) {
        actionEvent.consume();
        String html = "<html><body width='%1s'>"
                + "<p>Mit Auswahl dieser Option wird "
                + "die Wichtigkeit auf Hoch gesetzt!"
                + "<br><br>"
                + "<p>Bedenke bitte deine Auswahl.";
        DialogHtmlContent content = new DialogHtmlContent(html);
        content.setBlendMode(BlendMode.DARKEN);
        DialogResponse response = FxmlDialog.showAndWaitTimedAlert(Duration.seconds(10), Alert.AlertType.ERROR,
                null, content, primaryStage.getScene());
        System.out.println("Response: " + response);
    }

    public void onDialog8Clicked(ActionEvent actionEvent) {
        DialogResponse response = FxmlDialog.showAndWaitRemember(
                null, "Content Text for Question ", primaryStage.getScene());
        System.out.println("Response: " + response);
    }

    public void onDialog9Clicked(ActionEvent actionEvent) {

        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                for (int i = 0; i < 10; i++) {
                    updateProgress(i, 10);
                    updateMessage("Message: " + i + "\n mit mehreren Zeilen\n also drei!");
                    Thread.sleep(500);
                }
                updateProgress(10, 10);
                return null;
            }
        };

        FxmlDialog.showProgressDialog(task, primaryStage.getScene(), 300, 130);

        Thread thread = new Thread(task);
        thread.start();


    }
}
