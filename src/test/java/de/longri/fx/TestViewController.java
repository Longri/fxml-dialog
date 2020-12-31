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
import javafx.event.EventHandler;
import javafx.stage.Stage;
import java.net.URL;

public class TestViewController {

    private Stage primaryStage;

    public void onDialog1Clicked(ActionEvent actionEvent) {
        URL url = TestView.class.getResource("/de/longri/fx/TestContent.fxml");
        FxmlDialog.show(primaryStage.getScene(), url, new ContentControllerCallBack() {
            @Override
            public void callBack(Object controller) {
                TestContentController contentController = (TestContentController) controller;
                contentController.label.setText("Hallo Dialog Label");
                contentController.button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Hello World from Dialog caller, overrides the action event handler from controller!");
                    }
                });
            }
        }, DialogResponse.OK);
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
