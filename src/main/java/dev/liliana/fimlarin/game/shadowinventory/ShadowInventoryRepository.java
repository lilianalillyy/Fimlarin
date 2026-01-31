package dev.liliana.fimlarin.game.shadowinventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Entity;

public class ShadowInventoryRepository {

  private final Map<UUID, ShadowInventory> inventories = new HashMap<>();

  public ShadowInventory get(UUID uuid) {
    return inventories.get(uuid);
  }

  public ShadowInventory get(Entity entity) {
    return get(entity.getUniqueId());
  }

  public ShadowInventory remove(UUID uuid) {
    return inventories.remove(uuid);
  }

  public ShadowInventory remove(Entity entity) {
    return remove(entity.getUniqueId());
  }

  public ShadowInventory create(UUID uuid) {
    ShadowInventory inventory = new ShadowInventory();
    inventories.put(uuid, inventory);

    return inventory;
  }

  public ShadowInventory create(Entity entity) {
    return create(entity.getUniqueId());
  }

  protected Map<UUID, ShadowInventory> getAllInventories() {
    return inventories;
  }

}
