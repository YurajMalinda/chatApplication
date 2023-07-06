package lk.ijse.chatApp.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientFormController extends Thread {
    public AnchorPane client;
    public Label lblName;
    public ScrollPane txtChat;
    public TextField txtMessage;
    public AnchorPane emojiPane;
    public VBox vBox;

    BufferedReader bufferedReader;
    PrintWriter printWriter;
    Socket socket;
    private FileChooser fileChooser;
    private File file;

    public void initialize(){
        String userName = LoginFormController.userName;
        lblName.setText(userName);
        try {
            socket = new Socket("localhost",4000);
            System.out.println("Socket is connected with server.");
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(),true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        emojiPane.setVisible(false);
    }

    @Override
    public void run() {
            try {
                while (true){
                String message = bufferedReader.readLine();
                String[] tokens = message.split(" ");
                String cmd = tokens[0];

                StringBuilder fullMessage = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMessage.append(tokens[i] + " ");
                }

                String[] messageToAr = message.split(" ");
                String st = "";
                for (int i = 0; i < messageToAr.length - 1; i++) {
                    st += messageToAr[i + 1] + " ";
                }

                Text text = new Text(st);
                String firstChars = "";
                if (st.length() > 3) {
                    firstChars = st.substring(0, 3);
                }

                if (firstChars.equalsIgnoreCase("img")) {
                    st = st.substring(3, st.length() - 1);
                    File file = new File(st);
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(150);
                    imageView.setFitHeight(200);

                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);

                    if (!cmd.equalsIgnoreCase(lblName.getText())) {
                        vBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        Text text1 = new Text(" " + cmd + " :");
                        hBox.getChildren().add(text1);
                        hBox.getChildren().add(imageView);
                    } else {
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(imageView);
                        Text text1 = new Text("");
                        hBox.getChildren().add(text1);
                    }
                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                } else {
                    TextFlow textFlow = new TextFlow();
                    if (!cmd.equalsIgnoreCase(lblName.getText() + " :")) {
                        Text txtName = new Text(cmd + " ");
                        txtName.getStyleClass().add("txtName");
                        textFlow.getChildren().add(txtName);

                        textFlow.setStyle("-fx-color: rgb(239, 242, 255);" + "-fx-background-color: rgb(255, 40, 28);" + "-fx-background-radius: 10px");
                        textFlow.setPadding(new Insets(3, 10, 3, 10));
                    }
                    textFlow.getChildren().add(text);
                    textFlow.setMaxWidth(200);


                    TextFlow textFlow1 = new TextFlow(textFlow);
                    HBox hBox = new HBox(12);
                    if (!cmd.equalsIgnoreCase(lblName.getText() + " :")) {
                        vBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(textFlow1);
                    } else {
                        Text text2 = new Text(fullMessage + " ");
                        TextFlow textFlow2 = new TextFlow(text2);
                        hBox.getChildren().add(textFlow2);
                        hBox.setPadding(new Insets(2, 5, 2, 10));
                        textFlow2.setStyle("-fx-color: rgb(239, 242, 255);" + "-fx-background-color: rgb(191, 241, 9);" + "-fx-background-radius: 10px");
                        textFlow2.setPadding(new Insets(3, 10, 3, 10));
                    }
                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public void txtMessageOnAction(ActionEvent actionEvent) {
        String message = txtMessage.getText();
        printWriter.println(lblName.getText()+": "+message);
        txtMessage.clear();

        if(message.equalsIgnoreCase("bye")||(message.equalsIgnoreCase("Bye")||(message.equalsIgnoreCase("logout")))){
            System.exit(0);
        }
    }

    public void mouseOnClickHideEmoji(MouseEvent mouseEvent) {
        emojiPane.setVisible(false);
    }

    public void btnEmojiOnAction(MouseEvent mouseEvent) {
        emojiPane.setVisible(true);
    }

    public void btnSendOnAction(MouseEvent mouseEvent) {
        String message = txtMessage.getText();
        printWriter.println(lblName.getText()+": "+message);
        txtMessage.clear();

        if(message.equalsIgnoreCase("bye")||(message.equalsIgnoreCase("Bye")||(message.equalsIgnoreCase("logout")))){
            System.exit(0);
        }
    }

    public void btnCameraOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.file = fileChooser.showOpenDialog(stage);
        printWriter.println(lblName.getText()+" "+"img"+file.getPath());
    }

    public void emojiPaneOnAction(MouseEvent mouseEvent) {
    }

    public void sad(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128546));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void lot_sad(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128554));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void money(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(129297));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void love(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128525));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void green_sad(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128560));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void smile_one_eyy(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128540));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void cry_yes(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128546));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void cry_head(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128550));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void real_amile(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128514));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void tuin(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128519));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void woow(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128559));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void smile_normal(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128513));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void large_smile(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128522));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void small_smile(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128578));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }

    public void tong_smile(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128539));
        txtMessage.setText(emoji);
        emojiPane.setVisible(false);
    }
}
