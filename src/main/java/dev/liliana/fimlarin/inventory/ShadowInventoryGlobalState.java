package dev.liliana.fimlarin.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ShadowInventoryGlobalState {

  private final Map<UUID, ShadowInventory> inventories = new HashMap<>();

  public ShadowInventory getInventory(UUID uuid) {
    return inventories.get(uuid);
  }

  public ShadowInventory getInventory(Entity entity) {
    return getInventory(entity.getUniqueId());
  }

  public ShadowInventory removeInventory(UUID uuid) {
    return inventories.remove(uuid);
  }

  public ShadowInventory removeInventory(Entity entity) {
    return removeInventory(entity.getUniqueId());
  }

  public ShadowInventory createInventory(UUID uuid) {
    ShadowInventory inventory = new ShadowInventory();
    inventories.put(uuid, inventory);

    return inventory;
  }

  public ShadowInventory createInventory(Entity entity) {
    return createInventory(entity.getUniqueId());
  }

  public void flush(Player p, ShadowInventory shadow) {
    // Apply pending adds (as before)
    for (ItemStack add : shadow.getPendingAdds()) {
      Map<Integer, ItemStack> leftovers = p.getInventory().addItem(add);
      leftovers.values().forEach(it ->
          p.getWorld().dropItemNaturally(p.getLocation(), it)
      );
    }

    for (ItemStack drop : shadow.getPendingDrops()) {
      p.getWorld().dropItemNaturally(p.getLocation(), drop);
    }

    p.updateInventory();
  }

  public void flush(Player p) {
    ShadowInventory shadow = inventories.remove(p.getUniqueId());
    if (shadow == null) return;
    flush(p, shadow);
  }

  public void flushAll(JavaPlugin plugin) {
    inventories.entrySet().forEach((entry) -> {
      UUID uuid = entry.getKey();
      ShadowInventory shadow = entry.getValue();
      Player player = plugin.getServer().getPlayer(uuid);

      if (player == null) {
        // This should not happen, as we flush the inventory when player leaves the server.
        plugin.getLogger().severe("Failed to flush inventory for player " + uuid + " as player is not online. This should not happen.");
        return;
      }

      flush(player, shadow);
    });
  }

}
