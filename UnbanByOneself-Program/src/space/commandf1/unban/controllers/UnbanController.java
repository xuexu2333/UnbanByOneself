package space.commandf1.unban.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import space.commandf1.unban.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UnbanController implements Initializable {

    @FXML
    private Button button;
    @FXML
    private TextField textField;

    public static Scene show() throws IOException {
        URL xmlRecourse = UnbanController.class.getResource("/fxml/unban.fxml");
        assert (xmlRecourse != null);
        AnchorPane anchorPane = FXMLLoader.load(xmlRecourse);
        return new Scene(anchorPane, 640, 400);
    }

    @FXML
    public void unban() {
        button.setDisable(true);
        textField.setDisable(true);
        button.setText("正在尝试解封...");

        Timeline timeline = new Timeline(new KeyFrame(new Duration(1500L), ae -> {
            if (!isLawful(textField.getText())) {
                button.setText("请求被拒绝：名称为不合法字符");
                button.setDisable(false);
                textField.setDisable(false);
                return;
            }

            try {
                int i = tryUnbanPlayer(textField.getText());
                button.setText(i == 0 ? "解封成功" : "解封失败：" + getReason(i));
            } catch (Exception e) {
                button.setText("服务器连接失败");
            }

            button.setDisable(false);
            textField.setDisable(false);
        }));

        timeline.play();
        Timeline timeline1 = new Timeline(new KeyFrame(new Duration(5000L), ae -> {
            button.setText("尝试解封");
        }));
        timeline1.play();
    }

    private int tryUnbanPlayer(String name) throws Exception {
        Main.client.send(name, Main.hwid);
        System.out.println("[DEBUG] Sending request!");
        return Main.client.read();
    }

    private String getReason(int i) {
        switch (i) {
            case 1:
                return "次数已经用尽";
            case 2:
                return "未知错误";
            default:
                return "Loading...";
        }
    }

    private boolean isLawful(String str) {
        return (str != null) && (!str.contains(" ") && (!str.equalsIgnoreCase("")));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
