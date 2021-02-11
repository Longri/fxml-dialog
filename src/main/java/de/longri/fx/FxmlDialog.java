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

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FxmlDialog {

    private final static AtomicInteger dialogCount = new AtomicInteger();
    private final static HashMap<Integer, DialogResponse> responseHashMap = new HashMap<>();

    public static FxmlDialog getTimedDialog(URL contentFxmlUrl, Duration closeDuration, DialogResponse... responseTypes) {
        FxmlDialog dialog = getDialog(contentFxmlUrl, responseTypes);
        if (dialog != null) dialog.setTimed(closeDuration);
        return dialog;
    }

    public static FxmlDialog getDialog(URL contentFxmlUrl, DialogResponse... responseTypes) {
        try {
            FXMLLoader loader = new FXMLLoader(FxmlDialog.class.getResource("/de/longri/fx/FxmlDialog.fxml"));
            Parent parent = loader.load();
            FxmlDialog dialog = loader.getController();
            int dialogNumber = dialogCount.incrementAndGet();

            FXMLLoader contentLoader = new FXMLLoader(contentFxmlUrl);
            Node contentNode = contentLoader.load();
            Object contentController = contentLoader.getController();

            dialog.fxmlContentPane.getChildren().add(contentNode);
            dialog.setContentController(contentController);
            dialog.setParent(parent, contentController, dialogNumber);
            dialog.enableButtons(responseTypes);
            return dialog;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FxmlDialog getDialog(Node contentNode, DialogResponse... responseTypes) {
        try {
            FXMLLoader loader = new FXMLLoader(FxmlDialog.class.getResource("/de/longri/fx/FxmlDialog.fxml"));
            Parent parent = loader.load();
            FxmlDialog dialog = loader.getController();
            int dialogNumber = dialogCount.incrementAndGet();

            dialog.fxmlContentPane.getChildren().add(contentNode);

            dialog.setContentController(null);
            dialog.setParent(parent, null, dialogNumber);
            dialog.enableButtons(responseTypes);
            return dialog;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DialogResponse showAndWaitTimedAlert(Duration duration, Alert.AlertType alertType,
                                                       String headerText, String contentText, Scene parentScene) {
        DialogHtmlContent dialogHtmlContent = new DialogHtmlContent(contentText);
        dialogHtmlContent.setIsHtmlContent(false);
        return showAndWaitTimedAlert(duration, alertType, headerText, dialogHtmlContent, parentScene);
    }

    public static DialogResponse showAndWaitTimedAlert(Duration duration, Alert.AlertType alertType, String headerText,
                                                       DialogHtmlContent dialogHtmlContent, Scene parentScene) {
        Node node = getAlertDialogNode(alertType, headerText, dialogHtmlContent);
        FxmlDialog dialog = FxmlDialog.getDialog(node, DialogResponse.OK,
                alertType == Alert.AlertType.CONFIRMATION ? DialogResponse.Cancel : null);
        if (dialog != null) {
            dialog.setTimed(duration);
            return dialog.show(parentScene);
        }
        return DialogResponse.unknown;
    }

    public static DialogResponse showAndWaitAlert(Alert.AlertType alertType,
                                                  String headerText, String contentText, Scene parentScene) {
        DialogHtmlContent dialogHtmlContent = new DialogHtmlContent(contentText);
        dialogHtmlContent.setIsHtmlContent(false);
        return showAndWaitAlert(alertType, headerText, dialogHtmlContent, parentScene);
    }


    public static DialogResponse showAndWaitAlert(Alert.AlertType alertType, String headerText,
                                                  DialogHtmlContent dialogHtmlContent, Scene parentScene) {
        Node node = getAlertDialogNode(alertType, headerText, dialogHtmlContent);
        FxmlDialog dialog = FxmlDialog.getDialog(node, DialogResponse.OK,
                alertType == Alert.AlertType.CONFIRMATION ? DialogResponse.Cancel : null);
        if (dialog != null) {
            return dialog.show(parentScene);
        }
        return DialogResponse.unknown;
    }

    public static DialogResponse showAndWaitRemember(String headerText, String contentText, String rememberText, Scene scene) {
        DialogHtmlContent htmlContent = new DialogHtmlContent(contentText);
        htmlContent.setIsHtmlContent(false);
        return showAndWaitRemember(headerText, htmlContent, rememberText, scene);
    }

    public static DialogResponse showAndWaitRemember(String headerText, String contentText, Scene scene) {
        DialogHtmlContent htmlContent = new DialogHtmlContent(contentText);
        htmlContent.setIsHtmlContent(false);
        return showAndWaitRemember(headerText, htmlContent, scene);
    }

    public static DialogResponse showAndWaitRemember(String headerText, DialogHtmlContent htmlContent, Scene scene) {
        return showAndWaitRemember(headerText, htmlContent, "remember", scene);
    }

    public static DialogResponse showAndWaitRemember(String headerText, DialogHtmlContent htmlContent, String rememberText, Scene scene) {
        Node node = getAlertDialogNode(Alert.AlertType.CONFIRMATION, headerText, htmlContent);
        FxmlDialog dialog = FxmlDialog.getDialog(node, DialogResponse.OK, DialogResponse.Cancel);
        if (dialog != null) {
            dialog.setRemember(true);
            dialog.setRememberText(rememberText);
            return dialog.show(scene);
        }
        return DialogResponse.unknown;
    }

    public static VBox getAlertDialogNode(Alert.AlertType alertType, String headerText, DialogHtmlContent dialogHtmlContent) {
        Alert alert = new Alert(alertType);
        if (headerText != null) alert.setHeaderText(headerText);// if null use default
        alert.setContentText(dialogHtmlContent.getHtmlContent());
        alert.show();
        Parent node = alert.getGraphic().getParent().getParent().getParent();

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10, 50, 0, 50));
        GridPane gridPane = null;
        Label label = null;

        for (Node child : node.getChildrenUnmodifiable()) {
            if (child instanceof GridPane) {
                gridPane = (GridPane) child;
            } else if (child instanceof Label) {
                label = (Label) child;
            }
        }

        if (dialogHtmlContent.isHtmlContent()) {
            WebView webView = new WebView();
            webView.getEngine().loadContent(dialogHtmlContent.getHtmlContent());
            webView.setPrefSize(dialogHtmlContent.getPrefWidth(), dialogHtmlContent.getPrefHeight());
            webView.setBlendMode(dialogHtmlContent.getBlendMode());
            vBox.getChildren().addAll(gridPane, webView);
        } else {
            vBox.getChildren().addAll(gridPane, label);
        }
        alert.close();
        vBox.autosize();
        vBox.layout();
        return vBox;
    }

    public static void showProgressDialog(Task<Void> task, Scene scene) {
        showProgressDialog(task, scene, -1, -1);
    }

    public static void showProgressDialog(Task<Void> task, Scene scene, double width, double height) {

        if (task == null || scene == null) return;

        //create content and bind task properties
        VBox vBox = new VBox();

        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(10);

        Label lbl = new Label("");
        ProgressBar pb = new ProgressBar();
        vBox.getChildren().addAll(lbl, pb);

        FxmlDialog dialog = getDialog(vBox, DialogResponse.none);
        if (dialog == null) return;

        dialog.show(scene, false);
        if (width > 0) {
            dialog.dialogStage.setWidth(width);
            pb.setPrefWidth(width - 20);
        }
        if (height > 0) dialog.dialogStage.setHeight(height);

        pb.progressProperty().bind(task.progressProperty());
        lbl.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(event -> {
            //close dialog
            dialog.dialogStage.close();
        });
    }

    @FXML
    public Pane fxmlContentPane;
    @FXML
    public Button btnOk;
    @FXML
    public Button btnCancel;
    @FXML
    public Button btnApply;
    @FXML
    public ProgressBar progress;
    @FXML
    private Object contentController;
    @FXML
    public CheckBox chkRemember;
    private final Stage dialogStage = new Stage();
    private int responseNumber;
    private Parent parent;
    private Timeline timeline;
    private boolean remember = false;


    @FXML
    public void initialize() {

    }

    private void setParent(Parent parent, Object controller, int dialogNumber) {
        this.parent = parent;
        this.contentController = controller;
        this.responseNumber = dialogNumber;
    }

    private void setContentController(Object controller) {
        this.contentController = controller;
    }

    public Object getContentController() {
        return this.contentController;
    }

    public DialogResponse show(Scene parentScene) {
        return show(parentScene, true);
    }

    public DialogResponse show(Scene parentScene, boolean wait) {
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setResizable(false);
        dialogStage.initStyle(StageStyle.UTILITY);

        Scene dialogScene = new Scene(parent);

        //set style from parent scene
        dialogScene.getStylesheets().addAll(parentScene.getStylesheets());

        dialogStage.setScene(dialogScene);
        if (timeline != null) timeline.play();
        dialogStage.sizeToScene();
        this.btnOk.requestFocus();
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
        if (wait) dialogStage.showAndWait();
        else {
            dialogStage.show();
            return DialogResponse.none;
        }


        DialogResponse response = responseHashMap.get(this.responseNumber);
        responseHashMap.remove(this.responseNumber);
        return response;
    }

    private void setTimed(Duration duration) {
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), 0)),
                new KeyFrame(duration, e -> {
                    // close dialog with OK
                    responseHashMap.put(this.responseNumber, DialogResponse.OK);
                    this.dialogStage.close();
                }, new KeyValue(progress.progressProperty(), 1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        progress.setVisible(true);
        progress.setOnMouseClicked(e -> {
            if (progress.getProgress() >= 0) {
                //stop closing timer
                timeline.stop();
                progress.setProgress(-1);
            } else {
                //restart closing timer
                timeline.playFromStart();
            }
        });
    }

    private void enableButtons(DialogResponse[] responseTypes) {
        this.btnOk.setVisible(false);
        this.btnCancel.setVisible(false);
        this.btnApply.setVisible(false);
        for (DialogResponse type : responseTypes) {
            if (type == null) continue;
            switch (type) {
                case OK:
                    this.btnOk.setVisible(true);
                    break;
                case Cancel:
                    this.btnCancel.setVisible(true);
                    break;
                case Apply:
                    this.btnApply.setVisible(true);
            }
        }

        // arrange buttons from right to left
        double buttonMargin = 14;
        double right = buttonMargin;
        if (btnOk.isVisible()) {
            AnchorPane.setRightAnchor(btnOk, right);
            right += btnOk.getPrefWidth() + buttonMargin;
        }

        if (btnApply.isVisible()) {
            AnchorPane.setRightAnchor(btnApply, right);
            right += btnApply.getPrefWidth() + buttonMargin;
        }

        if (btnCancel.isVisible()) {
            AnchorPane.setRightAnchor(btnCancel, right);
            right += btnCancel.getPrefWidth() + buttonMargin;
        }
        AnchorPane.setRightAnchor(progress, right);
    }

    public void onOkClicked(ActionEvent actionEvent) {
        actionEvent.consume();
        responseHashMap.put(this.responseNumber, this.remember && this.chkRemember.isSelected() ?
                DialogResponse.RememberOk : DialogResponse.OK);
        this.dialogStage.close();
    }

    public void onCancelClicked(ActionEvent actionEvent) {
        actionEvent.consume();
        responseHashMap.put(this.responseNumber, this.remember && this.chkRemember.isSelected() ?
                DialogResponse.RememberCancel : DialogResponse.Cancel);
        this.dialogStage.close();
    }

    public void onApplyClicked(ActionEvent actionEvent) {
        actionEvent.consume();
    }

    public final void setOnApplyAction(EventHandler<ActionEvent> actionEventEventHandler) {
        this.btnApply.setOnAction(actionEventEventHandler);
    }

    public void onOkTyped(KeyEvent keyEvent) {
        keyEvent.consume();
        responseHashMap.put(this.responseNumber, DialogResponse.OK);
        this.dialogStage.close();
    }

    public void onCancelTyped(KeyEvent keyEvent) {
        keyEvent.consume();
        responseHashMap.put(this.responseNumber, DialogResponse.Cancel);
        this.dialogStage.close();
    }

    public void onApplyTyped(KeyEvent keyEvent) {
        keyEvent.consume();
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
        chkRemember.setVisible(remember);
    }

    public void setRememberText(String rememberText) {
        this.chkRemember.setText(rememberText);
    }

    public void setOkText(String txt) {
        this.btnOk.setText(txt);
    }

    public void setCancelText(String txt) {
        this.btnCancel.setText(txt);
    }

}
