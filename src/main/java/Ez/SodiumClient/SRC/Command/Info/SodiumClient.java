package Ez.SodiumClient.SRC.Command.Info;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SodiumClient implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmds, String label, String[] args) {
            if(cmds.getName().equalsIgnoreCase("sodium")) {
                Player player = (Player) sender;
                //player.sendMessage(ChatColor.GOLD + "Hi " + player.getName() + "?");
                Inventory inventory = Bukkit.createInventory(null,27, ChatColor.GOLD + "SodiumClient Editor");
                ItemStack stack = new ItemStack(Material.DIAMOND,1);
                ItemStack config = new ItemStack(Material.NAME_TAG,1);
                ItemStack dev = new ItemStack(Material.PLAYER_HEAD,1);
                inventory.setItem(10,stack);
                inventory.setItem(13,config);
                inventory.setItem(16,dev);
                player.openInventory(inventory);

            }
        return true;
    }
}
