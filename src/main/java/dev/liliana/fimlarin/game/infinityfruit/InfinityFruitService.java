package dev.liliana.fimlarin.game.infinityfruit;

import dev.liliana.fimlarin.Main;
import dev.liliana.fimlarin.PluginService;
import dev.liliana.fimlarin.game.shadowinventory.ShadowInventoryService;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InfinityFruitService extends PluginService {

  private final ShadowInventoryService shadowInventoryService;

  private final NamespacedKey fruitRolledKey;

  private final NamespacedKey fruitBlockedKey;

  public InfinityFruitService(Main plugin, ShadowInventoryService shadowInventoryService) {
    super(plugin);

    this.shadowInventoryService = shadowInventoryService;
    this.fruitRolledKey = new NamespacedKey(plugin, "infinity_fruit");
    this.fruitBlockedKey = new NamespacedKey(plugin, "infinity_fruit_blocked");
  }

  @Override
  public void onEnable() {
    InfinityFruitListener infinityFruitListener = new InfinityFruitListener(this);
    registerListeners(infinityFruitListener);
  }

  public void tagPlayerInventory(Player player) {
    Inventory inventory = player.getInventory();

    boolean isInventoryShadowed = shadowInventoryService.isPlayerInventoryShadowed(player);
    double chance = InfinityFruitUtils.getFruitChance(isInventoryShadowed);

    for (ItemStack stack : inventory.getContents()) {
      if (!InfinityFruitUtils.isItemFruit(stack)) {
        continue;
      }

      rollFruit(player, stack, chance);
    }
  }

  public void tagPlayerInventoryInBackground(Player player) {
    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> tagPlayerInventory(player));
  }

  public void rollFruit(Player player, ItemStack stack, double chance) {
    if (isFruitRolled(stack)) {
      return;
    }

    boolean blocked = InfinityFruitUtils.roll(chance);

    setByteKey(stack, fruitRolledKey, (byte) 1);
    if (blocked) {
      setByteKey(stack, fruitBlockedKey, (byte) 1);
    }
  }

  public boolean isFruitRolled(ItemStack stack) {
    return hasByteKey(stack, fruitRolledKey);
  }

  public boolean isFruitBlocked(ItemStack stack) {
    return hasByteKey(stack, fruitBlockedKey);
  }

  private boolean hasByteKey(ItemStack stack, NamespacedKey key) {
    ItemMeta meta = stack.getItemMeta();

    if (meta == null) {
      return false;
    }

    return meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
  }

  private boolean setByteKey(ItemStack stack, NamespacedKey key, byte value) {
    ItemMeta meta = stack.getItemMeta();

    if (meta == null) {
      return false;
    }

    meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, value);
    stack.setItemMeta(meta);

    return true;
  }
}
