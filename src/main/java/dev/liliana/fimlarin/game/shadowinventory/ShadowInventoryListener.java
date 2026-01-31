package dev.liliana.fimlarin.game.shadowinventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class ShadowInventoryListener implements Listener {

  private final ShadowInventoryService shadowInventoryService;

  public ShadowInventoryListener(ShadowInventoryService shadowInventoryService) {
    this.shadowInventoryService = shadowInventoryService;
  }

  /**
   * Flushes the player's shadow inventory when they open a crafting table.
   */
  @EventHandler
  public void onCraftingTableOpen(InventoryOpenEvent event) {
    if (!(event.getPlayer() instanceof Player player)) {
      return;
    }

    shadowInventoryService.flush(player);
  }

  /**
   * Adds the picked-up item to the player's shadow inventory if it fits.
   */
  @EventHandler(ignoreCancelled = true)
  public void onItemPickup(EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player player)
        || !shadowInventoryService.isPlayerInventoryShadowed(player)) {
      return;
    }

    ItemStack stack = event.getItem().getItemStack().clone();

    if (!shadowInventoryService.hasCombinedInventorySpace(player, stack)) {
      return;
    }

    shadowInventoryService.addItemToShadowInventory(player, stack);

    event.setCancelled(true);
    event.getItem().remove();
  }

  /**
   * Prevents the player from dropping items if their inventory is shadowed.
   */
  @EventHandler(ignoreCancelled = true)
  public void onItemDrop(PlayerDropItemEvent e) {
    Player player = e.getPlayer();

    if (!shadowInventoryService.isPlayerInventoryShadowed(player)) {
      return;
    }

    e.setCancelled(true);
  }

  /**
   * Flushes the player's shadow inventory when they leave the server to ensure no items are actually lost.
   */
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    // Ensure that the inventory is updated when the player leaves the server.
    shadowInventoryService.flush(e.getPlayer());
  }
}
