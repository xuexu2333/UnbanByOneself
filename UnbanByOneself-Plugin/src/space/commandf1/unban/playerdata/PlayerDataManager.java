package space.commandf1.unban.playerdata;

import space.commandf1.unban.Main;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

public final class PlayerDataManager {

    public static void write(PlayerData playerData) {
        File file = new File(Main.getInstance().getDataFolder(), "datas");
        if (!file.exists()) {
            file.mkdirs();
        }

        File data = new File(file, playerData.getHwid() + ".data");
        if (!data.exists()) {
            try {
                data.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(data.toPath()));
            objectOutputStream.writeObject(playerData);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerData read(String hwid) {
        File file = new File(Main.getInstance().getDataFolder(), "datas");
        if (!file.exists()) {
            file.mkdirs();
        }

        File data = new File(file, "PD-" + hwid + ".data");
        if (!data.exists()) {
            return null;
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(data.toPath()));
            return (PlayerData) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
