package space.commandf1.unban.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import space.commandf1.unban.Main;
import space.commandf1.unban.config.Configs;

import java.util.ArrayList;
import java.util.List;

public final class UnbanByOneselfCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("kcore.admin") || !commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou dont have permission.q"));
            return true;
        }
        if (strings.length < 1) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /UnbanByOneself reload\n&c/UnbanByOneself nameblacklist <name>\n&c/UnbanByOneself hwidblacklist <hwid>"));
            return true;
        }
        if (!strings[0].equalsIgnoreCase("reload") && !strings[0].equalsIgnoreCase("nameblacklist") && !strings[0].equalsIgnoreCase("hwidblacklist")) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /UnbanByOneself reload\n&c/UnbanByOneself nameblacklist <name>\n&c/UnbanByOneself hwidblacklist <hwid>"));
            return true;
        }
        if (strings[0].equalsIgnoreCase("reload")) {
            Main.getInstance().saveResource("config.yml", false);
            Configs.load();
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReload!"));
        }
        if (strings[0].equalsIgnoreCase("nameblacklist")) {
            List<String> NAME_BLACKLIST = Configs.NAME_BLACKLIST;
            if (NAME_BLACKLIST.contains(strings[1])) {
                NAME_BLACKLIST.remove(strings[1]);
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRemove " + strings[1]));
            } else {
                NAME_BLACKLIST.add(strings[1]);
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAdd " + strings[1]));
            }
            Main.getMainConfig().set("name-blacklist", NAME_BLACKLIST);
        }
        if (strings[0].equalsIgnoreCase("hwidblacklist")) {
            List<String> HWID_BLACKLIST = Configs.HWID_BLACKLIST;
            if (HWID_BLACKLIST.contains(strings[1])) {
                HWID_BLACKLIST.remove(strings[1]);
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRemove " + strings[1]));
            } else {
                HWID_BLACKLIST.add(strings[1]);
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAdd " + strings[1]));
            }
            Main.getMainConfig().set("hwid-blacklist", HWID_BLACKLIST);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> tabList = new ArrayList<>();
        if (strings.length == 1) {
            List<String> getList = new ArrayList<>();
            getList.add("reload");
            getList.add("nameblacklist");
            getList.add("hwidblacklist");
            for (String tabString : getList) {
                if (tabString.toLowerCase().startsWith(strings[0].toLowerCase())) {
                    tabList.add(tabString);
                }
            }
            return tabList;
        } else {
            tabList.add(Main.getInstance().getDescription().getName());
        }
        return tabList;
    }
}
