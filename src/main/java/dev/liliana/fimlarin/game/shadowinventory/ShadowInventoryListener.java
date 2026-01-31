package dev.liliana.fimlarin.game.shadowinventory;

import dev.liliana.fimlarin.Main;
import org.bukkit.Material;
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

  private final Main plugin;

  public ShadowInventoryListener(ShadowInventoryService shadowInventoryService, Main plugin) {
    this.shadowInventoryService = shadowInventoryService;
    this.plugin = plugin;
  }

  @EventHandler
  public void onCraftingTableOpen(InventoryOpenEvent event) {
    if (!(event.getPlayer() instanceof Player player)) {
      return;
    }

    shadowInventoryService.flush(player);
  }

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

  @EventHandler(ignoreCancelled = true)
  public void onItemDrop(PlayerDropItemEvent e) {
    Player player = e.getPlayer();

    if (!shadowInventoryService.isPlayerInventoryShadowed(player)) {
      return;
    }

    e.setCancelled(true);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    // Ensure that the inventory is updated when the player leaves the server.
    shadowInventoryService.flush(e.getPlayer());
  }
}
