package Ez.SodiumClient.SRC.Events.handler;

import Ez.SodiumClient.SRC.utils.InventoryUtils;
import java.util.List;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryEventHandler implements Listener {
    public InventoryEventHandler() {
    }

    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        if (event.getClick() == ClickType.MIDDLE) {
            Player player = (Player)event.getWhoClicked();
            Inventory inventory = event.getClickedInventory();
            if (inventory != null && inventory.getType() != InventoryType.CREATIVE && player.getGameMode() != GameMode.CREATIVE) {
                List<ItemStack> list = InventoryUtils.getFilteredInventory(inventory);
                InventoryUtils.sortItemStackAZ(list);
                InventoryUtils.sortItemStackByCount(list);
                InventoryUtils.sortItemStackByDurability(list);
                list = InventoryUtils.reduceStacks(list);
                list = InventoryUtils.expandStacks(list);
                inventory.clear();

                for(int i = 0; i < list.size(); ++i) {
                    inventory.setItem(i, (ItemStack)list.get(i));
                }

                player.playSound(player.getLocation(), Sound.ENTITY_PIG_SADDLE, 1.0F, 1.0F);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "(!) " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Sorted item!"));
                player.updateInventory();
            }
        }

    }
}
