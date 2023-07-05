package lk.ijse.chatApp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public TextField txtName;
    static String userName;

    public void txtNameOnAction(ActionEvent actionEvent) throws IOException {
        userName = txtName.getText();
        txtName.clear();
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(LoginFormController.class.getResource("/view/client_form.fxml"))));
        stage.close();
        stage.centerOnScreen();
        stage.show();
    }
}
