package dev.liliana.fimlarin.game.shadowinventory;

import dev.liliana.fimlarin.Main;
import dev.liliana.fimlarin.PluginService;
import dev.liliana.fimlarin.entity.player.PlayerService;
import dev.liliana.fimlarin.entity.player.inventory.PlayerInventoryUtils;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ShadowInventoryService extends PluginService {

  private final ShadowInventoryRepository shadowInventoryCollection;

  private final PlayerService playerService;

  public static final Material SHADOW_INVENTORY_TRIGGER = Material.CRAFTING_TABLE;

  public ShadowInventoryService(Main plugin, ShadowInventoryRepository shadowInventoryCollection,
      PlayerService playerService) {
    super(plugin);
    this.shadowInventoryCollection = shadowInventoryCollection;
    this.playerService = playerService;
  }

  /**
   * Registers the listener that handles shadow inventory events.
   */
  @Override
  public void onEnable() {
    ShadowInventoryListener shadowInventoryListener = new ShadowInventoryListener(this);
    registerListeners(shadowInventoryListener);
  }

  /**
   * Flushes all shadow inventories for all players when the plugin is disabled (= reload/server shutdown).
   */
  @Override
  public void onDisable() {
    flushAll(plugin);
  }

  /**
   * Adds an item to the player's shadow inventory.
   *
   * @param player Player to add item to
   * @param itemStack Item to add
   */
  public void addItemToShadowInventory(Player player, ItemStack itemStack) {
    ShadowInventory shadow = shadowInventoryCollection.get(player);

    if (shadow == null) {
      shadow = shadowInventoryCollection.create(player);
    }

    shadow.getPendingStacks().add(itemStack);
  }

  /**
   * Checks if the incoming item fits in the player's inventory, taking into account the shadow inventory's state.
   *
   * @param player Player to check inventory space for
   * @param incoming Incoming item to check
   * @return true if the item fits; false otherwise
   */
  public boolean hasCombinedInventorySpace(Player player, ItemStack incoming) {
    // If the player inventory is already full, just act naturally as the item is not supposed to get picked up.
    if (playerService.isInventoryFull(player)) {
      return false;
    }

    ShadowInventory shadow = shadowInventoryCollection.get(player);

    // If there is not a shadow inventory yet, we go just by the fact that there is space in the player's inventory.
    if (shadow == null) {
      return true;
    }

    // Simulate the player's inventory with applied shadow inventory to check if the incoming item fits.
    ItemStack[] combinedInventoryContents = ShadowInventoryUtils.createCombinedInventoryContents(
        player, shadow);
    return PlayerInventoryUtils.tryInsertIntoContents(combinedInventoryContents, incoming);
  }

  /**
   * Checks if the player's inventory is shadowed.
   *
   * @param player Player to check
   * @return true if the player's inventory is shadowed; false otherwise
   */
  public boolean isPlayerInventoryShadowed(Player player) {
    return playerService.isItemInInventory(player, SHADOW_INVENTORY_TRIGGER);
  }

  /**
   * Flushes the pending items in the shadow inventory into their actual inventory.
   *
   * @param player Player whose inventory to flush
   * @param shadow Shadow inventory to flush
   */
  public void flush(Player player, ShadowInventory shadow) {
    for (ItemStack pendingStack : shadow.getPendingStacks()) {
      Map<Integer, ItemStack> leftovers = player.getInventory().addItem(pendingStack);
      leftovers.values().forEach(it ->
          player.getWorld().dropItemNaturally(player.getLocation(), it)
      );
    }

    playerService.updateInventoryInBackground(player);
  }

  public void flush(Player player) {
    ShadowInventory shadow = shadowInventoryCollection.remove(player.getUniqueId());
    if (shadow == null) {
      return;
    }
    flush(player, shadow);
  }

  /**
   * Flushes all shadow inventories for all players.
   */
  public void flushAll(JavaPlugin plugin) {
    shadowInventoryCollection.getAllInventories().forEach((uuid, shadow) -> {
      Player player = plugin.getServer().getPlayer(uuid);

      if (player == null) {
        // This should not happen, as we flush the inventory when player leaves the server.
        plugin.getLogger().severe("Failed to flush inventory for player " + uuid
            + " as player is not online. This should not happen.");
        return;
      }

      flush(player, shadow);
    });
  }
}
