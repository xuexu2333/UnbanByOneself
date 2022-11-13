package space.commandf1.unban.util;

import javafx.application.Platform;
import org.apache.commons.codec.binary.Base64;
import space.commandf1.unban.Main;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Util {

    public Util() {
        this.addREADME();
        try {
            if (this.checkVersion()) {
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("[DEBUG] ERROR-1: " + e);
        }
    }

    public void addREADME() {
        try {
            File file = new File("README.txt");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Kamox自助解封 UnbanByOneself\n" +
                    "安装java 1.8使用双击打开。\n" +
                    "如果不确定你的java版本请win+r输入cmd 后 输入java -version\n" +
                    "如果不行请在当前目录下创建一个Start.cmd 内容输入\n" +
                    "java -jar UnbanbyOneself.jar\n" +
                    "pause\n" +
                    "打开后请填写正确的ID 每天有五次提交申请机会。\n" +
                    "每个计算机每天仅限提交5次;\n" +
                    "每个ID每天仅限提交5次。\n" +
                    "注意 如果你没被ban提交申请会消耗一次机会 没有填写正确的ID也会消耗一次 等等。\n" +
                    "每天晚上12点重置次数。\n" +
                    "待补充 以后会更新！\n" +
                    "关于系统内容信息收集: \n" +
                    "CPU名称 ;\n" +
                    "环境变量 ;\n" +
                    "等内容 。\n" +
                    "关于软件内网络访问、建立连接: \n" +
                    "使用网络与服务器进行连接 发送如上信息以及你的MC ID到服务器 无任何其他信息收集。\n" +
                    "本软件通过360杀毒 360安全卫士 火绒 等杀毒软件。\n" +
                    "本项目未调用任何有关系统函数 包括Runtime 以及 任何敏感区函数。\n" +
                    "❤Powered by Lite_Shark&commandf1❤" + "\n");
            bufferedWriter.close();
        } catch (Exception ignored) {
        }
    }

    public boolean checkVersion() throws IOException {
        AtomicBoolean isExit = new AtomicBoolean(false);
        Thread thread = new Thread(() -> {
            try {
                URL url1 = new URL("https://www.kamox.cf/UnbanByOneself/version");
                URLConnection urlConnection = url1.openConnection();
                urlConnection.addRequestProperty("User-Agent", "Mozilla");
                urlConnection.setReadTimeout(30000);
                urlConnection.setConnectTimeout(30000);
                InputStream in = url1.openStream();
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader bufr = new BufferedReader(isr);
                String str;
                StringBuilder stringBuilder = new StringBuilder();
                while ((str = bufr.readLine()) != null) {
                    stringBuilder.append(str);
                }
                String version;
                try {
                    version = stringBuilder.toString().split("\"version\":\"")[1].split("\"}")[0];
                } catch (Exception e) {
                    try {
                        version = stringBuilder.toString().split("\"version\":\"")[1].split("\"}")[0];
                    } catch (Exception e1) {
                        version = "1";
                    }
                }
                try {
                    int i = Integer.parseInt(version);
                    if (i > Integer.parseInt(Main.getVersion())) {
                        System.out.println("[TIP] Old version please update! Now version: " + i);
                        System.out.println("[TIP] 新版本已更新, 请尽快更新到新版本, 版本号: " + i);
                        JOptionPane.showMessageDialog(null, "新版本已更新, 请尽快更新到新版本, 版本号: " + i, "TIP", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    if (version.equals("BAN")) {
                        System.out.println("[WARNING] BAD VERSION");
                        System.out.println("[警告] 版本已停用");
                        boolean isSHOW = true;
                        while (isSHOW) {
                            if (Main.stage == null) {
                                Thread.sleep(1);
                            } else {
                                isSHOW = false;
                            }
                        }
                        Platform.runLater(() -> Main.stage.close());
                        JOptionPane.showMessageDialog(null, "版本已停用", "警告", JOptionPane.WARNING_MESSAGE);
                        isExit.set(true);
                        ;
                        System.exit(0);
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("ERROR-3: " + e);
            }
        });
        thread.setName("WEB THREAD");
        thread.start();
        return isExit.get();
    }
}
