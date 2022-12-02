package Ez.SodiumClient.SRC;

import Ez.SodiumClient.SRC.Command.Info.SodiumClient;
import Ez.SodiumClient.SRC.Command.config.Reload;
import Ez.SodiumClient.SRC.Events.BlockBreak;
import Ez.SodiumClient.SRC.Events.Player.PlayerDropItem;
import Ez.SodiumClient.SRC.Events.Player.PlayerJoin;
import Ez.SodiumClient.SRC.Events.Player.PlayerQuit;
import Ez.SodiumClient.SRC.Events.handler.EntityDamageEventHandler;
import Ez.SodiumClient.SRC.Events.handler.EntityHealEventHandler;
import Ez.SodiumClient.SRC.Events.handler.InventoryEventHandler;
import Ez.SodiumClient.SRC.Events.handler.WorldEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public final class Setup extends JavaPlugin implements Listener {


    public void register() {
        getCommand("sodium").setExecutor(new SodiumClient());
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new Gui(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

    }*/
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(ChatColor.RED +"[SodiumClient] Plugin Disable");
        Iterator var1 = Bukkit.getWorlds().iterator();
        INSTANCE = null;
        while(var1.hasNext()) {
            World world = (World)var1.next();
            List<Entity> armorStands = (List)world.getEntities().stream().filter((entity) -> {
                return entity instanceof ArmorStand && entity.getScoreboardTags().contains("dmg.unsaved");
            }).collect(Collectors.toList());
            armorStands.forEach(Entity::remove);
        }

    }
    private double maxDistance = 32.0;
    private int updateTimer = 40;
    private boolean displayNames = true;
    private boolean nullPlayerName = false;
    private String amountColor = "&e&l";
    private String playerNameColor = "&e";
    private Map<String, String> itemsMap;
    private List<String> disabledWorlds;

    public static Setup INSTANCE;
    public Setup() {
    }

    public void onEnable() {
        INSTANCE = this;
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginCommand("itemmerge").setExecutor(new ReloadCommand(this));
        Bukkit.getPluginCommand("sodium").setExecutor(new Reload(this));
        this.itemsMap = new HashMap();
        this.disabledWorlds = new ArrayList();
        this.setupPreferences();
        getCommand("sodium").setExecutor(new SodiumClient());
       //getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[SodiumClient] Plugin Enable");
        this.getServer().getPluginManager().registerEvents(new EntityDamageEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new EntityHealEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new WorldEventHandler(), this);
        Iterator var1 = Bukkit.getWorlds().iterator();
        this.getServer().getPluginManager().registerEvents(new InventoryEventHandler(), this);

        while(var1.hasNext()) {
            World world = (World)var1.next();
            List<Entity> armorStands = (List)world.getEntities().stream().filter((entity) -> {
                return entity instanceof ArmorStand && entity.getScoreboardTags().contains("dmg.unsaved");
            }).collect(Collectors.toList());
            armorStands.forEach(Entity::remove);
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Iterator var0 = Bukkit.getWorlds().iterator();

            while(var0.hasNext()) {
                World world = (World)var0.next();
                List<Entity> armorStands = (List)world.getEntities().stream().filter((entity) -> {
                    return entity instanceof ArmorStand && entity.getScoreboardTags().contains("dmg.shouldDelete");
                }).collect(Collectors.toList());
                armorStands.forEach(Entity::remove);
            }

        }, 0L, 0L);
    }

    public void setupPreferences() {
        this.maxDistance = this.getConfig().getDouble("Maximum Distance");
        this.updateTimer = this.getConfig().getInt("Update Timer");
        this.displayNames = this.getConfig().getBoolean("Display Name");
        this.nullPlayerName = this.getConfig().getBoolean("Display Name On Null Player");
        this.playerNameColor = this.getConfig().getString("Player Name Color");
        Iterator var1 = this.getConfig().getConfigurationSection("Custom Names").getKeys(false).iterator();

        while(var1.hasNext()) {
            String materialString = (String)var1.next();
            String name = this.getConfig().getString("Custom Names." + materialString);
            this.itemsMap.put(materialString, name);
        }

        this.disabledWorlds = this.getConfig().getStringList("Disabled Worlds");
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (!this.displayNames) {
            Item item = event.getItemDrop();
            item.setCustomName(this.color(this.playerNameColor + event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onDropSpawn(final ItemSpawnEvent event) {
        if (!this.disabledWorlds.contains(event.getEntity().getWorld().getName())) {
            final ItemStack item = event.getEntity().getItemStack();
            final ItemMeta meta = item.getItemMeta();
            int amount = event.getEntity().getItemStack().getAmount();
            if (this.displayNames) {
                if (event.getEntity().getItemStack().getMaxStackSize() > 1) {
                    event.getEntity().setCustomName(this.color(String.valueOf(this.setDropName(event.getEntity())) + ChatColor.GOLD + " | x" + amount)); //event.getEntity().setCustomName(this.color(String.valueOf(this.setDropName(event.getEntity())) + " | x" + amount));
                } else {
                    event.getEntity().setCustomName(this.color(this.setDropName(event.getEntity())));
                }
            }

            (new BukkitRunnable() {
                public void run() {
                    if (event.getEntity().isDead()) {
                        this.cancel();
                    }

                    List<Entity> entities = event.getEntity().getNearbyEntities(Setup.this.maxDistance, Setup.this.maxDistance, Setup.this.maxDistance);
                    boolean isPlayerNearby = false;
                    Iterator var3 = entities.iterator();

                    while(var3.hasNext()) {
                        Entity entity = (Entity)var3.next();
                        if (entity.getType() == EntityType.PLAYER) {
                            isPlayerNearby = true;
                        }
                    }

                    if (!isPlayerNearby || meta != null && meta.hasDisplayName() && meta.getDisplayName().equalsIgnoreCase("") || Setup.this.itemsMap.containsKey(item.getType().toString()) && ((String)Setup.this.itemsMap.get(item.getType().toString())).equalsIgnoreCase("")) {
                        event.getEntity().setCustomNameVisible(false);
                    } else if (isPlayerNearby) {
                        event.getEntity().setCustomNameVisible(true);
                    }

                }
            }).runTaskTimer(this, 2L, (long)this.updateTimer);
        }
    }

    @EventHandler
    public void onItemMerge(final ItemMergeEvent event) {
        final String name = this.setDropName(event.getTarget());
        final int amount = event.getEntity().getItemStack().getAmount() + event.getTarget().getItemStack().getAmount();
        if (this.displayNames) {
            (new BukkitRunnable() {
                public void run() {
                    event.getTarget().setCustomName(Setup.this.color(name + ChatColor.GOLD + " | x" + amount));
                }
            }).runTaskLater(this, 1L);
        }
    }

    public String color(String msg) {
        StringBuilder coloredMsg = new StringBuilder();

        for(int i = 0; i < msg.length(); ++i) {
            if (msg.charAt(i) == '&') {
                coloredMsg.append('&');
            } else {
                coloredMsg.append(msg.charAt(i));
            }
        }

        return coloredMsg.toString();
    }

    private String setDropName(Item item) {
        String name;
        if (!this.itemsMap.containsKey(item.getItemStack().getType().toString())) {
            if (item.getItemStack().getItemMeta() != null) {
                if (item.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("")) {
                    if (item.getCustomName() == null) {
                        name = item.getName();
                    } else {
                        name = item.getCustomName();
                    }
                } else {
                    name = item.getItemStack().getItemMeta().getDisplayName();
                }
            } else if (item.getCustomName() != null) {
                name = item.getCustomName();
            } else {
                name = item.getName();
                if (name.contains(".")) {
                    name = this.getFriendlyName(item.getItemStack());
                }
            }
        } else {
            name = (String)this.itemsMap.get(item.getItemStack().getType().toString());
        }

        if (name.contains(this.color( " | x")) && name.contains("")) {
            name = name.subSequence(0, name.length() - 6).toString();
        }

        return name;
    }

    private String format(String s) {
        if (!s.contains("_")) {
            return this.capitalize(s);
        } else {
            String[] j = s.split("_");
            StringBuilder c = new StringBuilder();
            String[] var4 = j;
            int var5 = j.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String f = var4[var6];
                f = this.capitalize(f);
                c.append(c.toString().equalsIgnoreCase("") ? f : " " + f);
            }

            return c.toString();
        }
    }

    private String capitalize(String text) {
        String firstLetter = text.substring(0, 1).toUpperCase();
        String next = text.substring(1).toLowerCase();
        return firstLetter + next;
    }

    private String getFriendlyName(ItemStack item) {
        Material m = item.getType();
        return this.format(m.name());
    }
}
