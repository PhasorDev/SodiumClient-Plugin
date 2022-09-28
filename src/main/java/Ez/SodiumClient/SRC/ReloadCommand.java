package Ez.SodiumClient.SRC;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class ReloadCommand implements CommandExecutor, TabCompleter {
    private Setup plugin;

    public ReloadCommand(Setup plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() || sender.hasPermission("itemmerge.reload")) {
            this.plugin.reloadConfig();
            this.plugin.setupPreferences();
            sender.sendMessage(this.plugin.color(ChatColor.YELLOW + "DropNames configuration file reloaded"));
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("itemmerge")) {
            return null;
        } else {
            List<String> tabList = new ArrayList();
            if (sender.isOp() || sender.hasPermission("itemmerge.reload")) {
                tabList.add("reload");
            }

            return tabList;
        }
    }
}