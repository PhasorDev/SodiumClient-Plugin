package Ez.SodiumClient.SRC.Events.handler;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldEventHandler implements Listener {
    public WorldEventHandler() {
    }

    @EventHandler
    public void onServerOpened(WorldLoadEvent event) {
        Iterator var2 = Bukkit.getWorlds().iterator();

        while(var2.hasNext()) {
            World world = (World)var2.next();
            List<Entity> armorStands = (List)world.getEntities().stream().filter((entity) -> {
                return entity instanceof ArmorStand && entity.getScoreboardTags().contains("dmg.unsaved");
            }).collect(Collectors.toList());
            Bukkit.broadcastMessage(String.valueOf(armorStands.size()));
            armorStands.forEach(Entity::remove);
        }

    }

    @EventHandler
    public void onServerClosed(WorldUnloadEvent event) {
        Iterator var2 = Bukkit.getWorlds().iterator();

        while(var2.hasNext()) {
            World world = (World)var2.next();
            List<Entity> armorStands = (List)world.getEntities().stream().filter((entity) -> {
                return entity instanceof ArmorStand && entity.getScoreboardTags().contains("dmg.unsaved");
            }).collect(Collectors.toList());
            armorStands.forEach(Entity::remove);
        }

    }
}