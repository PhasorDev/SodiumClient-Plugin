package Ez.SodiumClient.SRC.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class InventoryUtils {
    public InventoryUtils() {
    }

    public static List<ItemStack> getFilteredInventory(Inventory inventory) {
        return (List)(new ArrayList(Arrays.asList(inventory.getContents()))).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static void sortItemStackAZ(List<ItemStack> list) {
        list.sort((o1, o2) -> {
            return o1.getType().toString().compareToIgnoreCase(o2.getType().toString());
        });
    }

    public static void sortItemStackByCount(List<ItemStack> list) {
        list.sort((o1, o2) -> {
            return Integer.compare(o2.getAmount(), o1.getAmount());
        });
    }

    public static void sortItemStackByDurability(List<ItemStack> list) {
        list.sort((o1, o2) -> {
            return o1 instanceof Damageable && o2 instanceof Damageable ? Integer.compare(((Damageable)o1).getDamage(), ((Damageable)o2).getDamage()) : 0;
        });
    }

    public static List<ItemStack> reduceStacks(List<ItemStack> items) {
        ArrayList<ItemStack> newList = new ArrayList();
        Iterator var2 = items.iterator();

        while(var2.hasNext()) {
            ItemStack item = (ItemStack)var2.next();
            ItemStack existingItem = (ItemStack)newList.stream().filter((tempItem) -> {
                return tempItem.isSimilar(item);
            }).findFirst().orElse(null); //(Object)
            if (existingItem == null) {
                newList.add(item);
            } else {
                existingItem.setAmount(existingItem.getAmount() + item.getAmount());
            }
        }

        return newList;
    }

    public static List<ItemStack> expandStacks(List<ItemStack> items) {
        ArrayList<ItemStack> newList = new ArrayList();
        Iterator var2 = items.iterator();

        while(var2.hasNext()) {
            ItemStack item = (ItemStack)var2.next();

            while(item.getAmount() > 0) {
                int amount = Math.min(item.getAmount(), item.getMaxStackSize());
                ItemStack clone = item.clone();
                clone.setAmount(amount);
                newList.add(clone);
                item.setAmount(item.getAmount() - amount);
            }
        }

        return newList;
    }
}
