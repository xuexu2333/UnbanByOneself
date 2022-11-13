package space.commandf1.unban;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import space.commandf1.unban.command.UnbanByOneselfCommand;
import space.commandf1.unban.config.Configs;
import space.commandf1.unban.playerdata.PlayerData;
import space.commandf1.unban.playerdata.PlayerDataManager;
import space.commandf1.unban.playerdata.PlayerUserName;
import space.commandf1.unban.playerdata.PlayerUserNameManager;
import space.commandf1.unban.server.Server;
import space.commandf1.unban.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

import static space.commandf1.unban.config.Configs.*;

public final class Main extends JavaPlugin {
    private static FileConfiguration config;
    private static Main instance;
    private Server server;

    public static Main getInstance() {
        return instance;
    }

    public static FileConfiguration getMainConfig() {
        return config;
    }

    @Override
    public void onLoad() {
        instance = this;

        this.setupConfigs();
        config = getConfig();

        Configs.load();

        getLogger().info("Loaded " + getDescription().getFullName());
    }

    @Override
    public void onEnable() {
        try {
            this.createServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            server = new Server(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCommand("UnbanByOneself").setExecutor(new UnbanByOneselfCommand());
        getCommand("UnbanByOneself").setTabCompleter(new UnbanByOneselfCommand());
        getLogger().info("Enable " + getDescription().getFullName());
    }

    @Override
    public void onDisable() {
        instance = null;
        config = null;
        getLogger().info("Disable " + getDescription().getFullName());
    }

    private void createServer() throws IOException {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    server.set();
                    String line = server.read();
                    if (!line.contains("|"))
                        return;
                    String[] types = line.split("\\|");
                    if (types.length != 2)
                        return;
                    PlayerData playerData = PlayerDataManager.read(types[1]) != null ? PlayerDataManager.read(types[1]) : new PlayerData(types[1]);
                    PlayerUserName playerUserName = PlayerUserNameManager.read(types[0]) != null ? PlayerUserNameManager.read(types[0]) : new PlayerUserName(types[0]);
                    assert playerData != null;
                    assert playerUserName != null;

                    if (playerData.getTimes() >= MAX_TIME || playerUserName.getPlayerNameUnbanFrequency().getOrDefault(playerUserName.getName(), 0) >= MAX_TIME) {
                        server.send("1");
                        getLogger().info(types[0] + " has send unbanned request but he is in day max time, HWID: " + types[1]);
                        return;
                    }
                    boolean isBlackList = false;
                    for (String name : NAME_BLACKLIST) {
                        if (playerUserName.getName().equals(name)) {
                            isBlackList = true;
                            break;
                        }
                    }
                    for (String hwid : HWID_BLACKLIST) {
                        if (playerData.getHwid().equals(hwid)) {
                            isBlackList = true;
                            break;
                        }
                    }
                    if (isBlackList) {
                        server.send("1");
                        getLogger().info(types[0] + " has send unbanned request but he is in blacklist, HWID" + types[1]);
                        return;
                    }
                    playerData.setTimes(playerData.getTimes() + 1);
                    playerUserName.getPlayerNameUnbanFrequency().put(playerUserName.getName(), playerUserName.getPlayerNameUnbanFrequency().getOrDefault(playerUserName.getName(), 0) + 1);

                    for (String cmd : COMMAND)
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", types[0]));

                    PlayerDataManager.write(playerData);
                    PlayerUserNameManager.write(playerUserName);

                    server.send("0");

                    getLogger().info("UNBANNED: " + types[0] + ", HWID: " + types[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.cancel();
            }
        }.runTaskTimerAsynchronously(this, 1L, 1L);

        new BukkitRunnable() {
            @Override
            public void run() {
                File file = new File(Main.getInstance().getDataFolder(), "playerdatas");
                File[] files = file.listFiles();
                Calendar cld = Calendar.getInstance();
                try {
                    if (!file.exists())
                        file.mkdirs();
                    if (Objects.nonNull(files)) {
                        for (File f : files) {
                            if (!f.isDirectory()) {
                                Date temp = new Date(Util.getFileCreatedTime(f));
                                temp.setHours(0); // ? time out methods?
                                temp.setMinutes(0);
                                temp.setSeconds(0);
                                cld.setTime(temp);
                                cld.add(Calendar.DATE, DELAY_DELETE);
                                temp = cld.getTime();
                                if (temp.getTime() < System.currentTimeMillis()) {
                                    Util.clean(f);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int time = 0;
                List<BukkitTask> tasks = new LinkedList<>();
                for (BukkitTask bukkitTask : Bukkit.getScheduler().getPendingTasks()) {
                    if (bukkitTask.getOwner().getName().equalsIgnoreCase(Main.getInstance().getDescription().getName())) {
                        tasks.add(bukkitTask);
                        time++;
                    }
                }

                if (time >= 100) {
                    for (BukkitTask task : tasks)
                        task.cancel();
                }
                this.cancel();
            }
        }.runTaskTimerAsynchronously(this, 20L, 20L);
    }

    private void setupConfigs() {
        this.saveResource("config.yml", false);
    }


    @Override
    public void saveResource(String resourcePath, boolean replace) {
        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = this.getResource(resourcePath);
        if (in == null)
            throw new IllegalArgumentException("The plugin file may have been broken, please re-download the plugin.");
        File outFile = new File(this.getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf(47);
        File outDir = new File(this.getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));
        if (!outDir.exists() && !outDir.mkdirs())
            throw new IllegalArgumentException("Can not create the dir.");
        try {
            if (!(outFile.exists() && !replace)) {
                OutputStream out = Files.newOutputStream(outFile.toPath());
                byte[] buf = new byte[1024];

                int len;
                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);

                out.close();
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
