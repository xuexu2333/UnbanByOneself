package space.commandf1.unban.playerdata;

import space.commandf1.unban.Main;
import space.commandf1.unban.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

public final class PlayerUserNameManager {
    public static void write(PlayerUserName playerUserName) {
        File file = new File(Main.getInstance().getDataFolder(), "datas");
        if (!file.exists()) {
            file.mkdirs();
        }

        File data = new File(file, Util.toSHA256String(playerUserName.getName()) + ".data");
        if (!data.exists()) {
            try {
                data.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(data.toPath()));
            objectOutputStream.writeObject(playerUserName);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerUserName read(String username) {
        File file = new File(Main.getInstance().getDataFolder(), "datas");
        if (!file.exists()) {
            file.mkdirs();
        }

        File data = new File(file, "PN-" + Util.toSHA256String(username) + ".data");
        if (!data.exists()) {
            return null;
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(data.toPath()));
            return (PlayerUserName) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
