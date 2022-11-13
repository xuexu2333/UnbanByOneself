package space.commandf1.unban.config;

import space.commandf1.unban.Main;

import java.util.List;

public final class Configs {
    public static int PORT;
    public static int MAX_TIME;
    public static List<String> COMMAND;
    public static int DELAY_DELETE;
    public static List<String> NAME_BLACKLIST;
    public static List<String> HWID_BLACKLIST;

    public static void load() {
        PORT = Main.getMainConfig().getInt("port");
        MAX_TIME = Main.getMainConfig().getInt("max-times");
        COMMAND = Main.getMainConfig().getStringList("command");
        DELAY_DELETE = Main.getMainConfig().getInt("time-delay-delete");
        NAME_BLACKLIST = Main.getMainConfig().getStringList("name-blacklist");
        HWID_BLACKLIST = Main.getMainConfig().getStringList("hwid-blacklist");
    }
}
