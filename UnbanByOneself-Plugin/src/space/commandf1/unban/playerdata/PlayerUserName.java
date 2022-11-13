package space.commandf1.unban.playerdata;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class PlayerUserName implements Serializable {

    private final String name;
    private final Map<String, Integer> playerNameUnbanFrequency = new HashMap<>();

    public PlayerUserName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getPlayerNameUnbanFrequency() {
        return this.playerNameUnbanFrequency;
    }

    public String getName() {
        return this.name;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.name);
    }
}
