package space.commandf1.unban;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import space.commandf1.unban.client.Client;
import space.commandf1.unban.controllers.UnbanController;
import space.commandf1.unban.hwid.HWID;
import space.commandf1.unban.util.SetTitle;
import space.commandf1.unban.util.Util;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public final class Main extends Application {

    public static HWID hwid;
    public static Client client;
    public static Util util;
    public static Stage stage;
    public static String ip = "DXZO4ph1VYRYJIX31wl5lg==";
    public static int port = Integer.parseInt("bkjn2vEQV6M=");
    private static String version;

    public static String getVersion() {
        return version;
    }

    public static void main(String[] args) {
        System.out.println("Kamox UnbanOneByself");
        System.out.println("[DEBUG] Loading...");
        JOptionPane.showMessageDialog(null, "本软件由Lite_Shark & commandf1提供支持。", "TIP", JOptionPane.INFORMATION_MESSAGE);
        version = "1";
        util = new Util();
        hwid = new HWID();
        try {
            client = new Client(ip, port);
        } catch (IOException ignored) {
        }
        boolean isLinux = false;
        try {
            SetTitle.INSTANCE.SetConsoleTitleA("Kamox UnbanByOneself");
        } catch (Throwable e) {
            isLinux = true;
        }
        if (!System.getProperty("os.name").toLowerCase().contains("windows".toLowerCase()) || isLinux) {
            System.out.println("[WARNING] This software does not support running on platforms other than Windows/Linux. If you can view the GUI, ignore this message. Please feed back to the author. Your system name is: " + System.getProperty("os.name") + ".");
            System.out.println("[警告] 本软件不支持在除WINDOWS/LINUX平台上运行 如果你能查看到GUI则忽略此消息, 请反馈给作者, 你的系统名称: " + System.getProperty("os.name") + "。");
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("datas/ip.data"))));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            JSONObject json = JSON.parseObject(buffer.toString());
            ip = json.getString("ip");
            port = json.getIntValue("port");
        } catch (Exception ignored) {
        }
        System.out.println("[DEBUG] Loaded!");
        Application.launch();
        System.out.println("[DEBUG] Closed!");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene show = UnbanController.show();
        primaryStage.setTitle("Kamox UnbanByOneself");
        primaryStage.setScene(show);
        primaryStage.getIcons().add(new Image("img/unban.jpg"));
        primaryStage.setResizable(false);
        show.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm());
        primaryStage.show();
        stage = primaryStage;
    }
}
