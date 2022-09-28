package Ez.SodiumClient.SRC.Events.handler;

import Ez.SodiumClient.SRC.Setup;
import Ez.SodiumClient.SRC.utils.MathHelper;
import Ez.SodiumClient.SRC.utils.NumberUtils;
import java.text.DecimalFormat;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityHealEventHandler implements Listener {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    public static final Random random = new Random();

    public EntityHealEventHandler() {
    }

    @EventHandler
    public void onEntityDamaged(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Damageable) {
            Damageable damageable = (Damageable)entity;
            if (damageable.getHealth() < damageable.getMaxHealth()) {
                String heal = event.getAmount() >= 1000.0 ? NumberUtils.formatCompact((long)event.getAmount()) : DECIMAL_FORMAT.format(event.getAmount());
                double health = MathHelper.clamp(Double.parseDouble(DECIMAL_FORMAT.format(damageable.getHealth() + event.getAmount())), 0.0, damageable.getMaxHealth());
                double f0 = (double)(random.nextInt(8) + 1) / 10.0;
                double f1 = (double)(random.nextInt(3) + 1) / 10.0;
                double xPos = random.nextInt(2) == 0 ? f0 + event.getEntity().getLocation().getX() : -f0 + event.getEntity().getLocation().getX();
                double yPos = f1 + event.getEntity().getBoundingBox().getMaxY();
                double zPos = random.nextInt(2) == 0 ? f0 + event.getEntity().getLocation().getZ() : -f0 + event.getEntity().getLocation().getZ();
                Location spawnLocation = new Location(entity.getWorld(), xPos, yPos, zPos);
                ArmorStand armorStand = (ArmorStand)event.getEntity().getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
                armorStand.setInvisible(true);
                armorStand.setSilent(true);
                armorStand.setMarker(true);
                armorStand.setGravity(false);
                armorStand.setCustomName(ChatColor.GREEN + "+" + heal + "❤" + ChatColor.AQUA + "[" + health + "❤]");
                armorStand.setCustomNameVisible(true);
                armorStand.addScoreboardTag("dmg.unsaved");
                Setup.INSTANCE.getServer().getScheduler().scheduleSyncDelayedTask(Setup.INSTANCE, () -> {
                    armorStand.remove();
                    armorStand.addScoreboardTag("dmg.shouldDelete");
                }, 20L);
            }
        }

    }
}