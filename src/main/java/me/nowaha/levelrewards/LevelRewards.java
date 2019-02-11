package me.nowaha.levelrewards;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class LevelRewards extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        if (getConfig().get("rewards") == null) {
            getConfig().set("rewards.1", Arrays.asList("say Well done {player}!"));
            saveConfig();
            reloadConfig();
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("levelrewards")) {
            if (args.length < 1) {
                sender.sendMessage("§b" + getDescription().getFullName() + " §fby §b" + getDescription().getAuthors());
                sender.sendMessage("§bCommands:\n §f- /levelrewards §breload§f: Reload the configuration file.");
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("levelrewards.reload")) {
                    sender.sendMessage("§bReloading config...");
                    reloadConfig();
                    sender.sendMessage("§aThe config was reloaded successfully!");
                } else {
                    sender.sendMessage("§cYou cannot use that command.");
                }
            }
        }
        return true;
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLevelChange(PlayerLevelChangeEvent e) {
        if (((MemorySection)getConfig().get("rewards")).getKeys(false).contains(e.getNewLevel() + "")) {
            List<String> commands = getConfig().getStringList("rewards." + e.getNewLevel());
            for (String command : commands) {
                command = command.replaceAll("\\{player}", e.getPlayer().getName());
                getServer().dispatchCommand(getServer().getConsoleSender(), command);
            }
        }
    }


}
