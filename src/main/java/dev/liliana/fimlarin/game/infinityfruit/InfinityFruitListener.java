package dev.liliana.fimlarin.game.infinityfruit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public final class InfinityFruitListener implements Listener {

  private final InfinityFruitService infinityFruitService;

  public InfinityFruitListener(InfinityFruitService infinityFruitService) {
    this.infinityFruitService = infinityFruitService;
  }

  @EventHandler
  public void onConsume(PlayerItemConsumeEvent event) {
    ItemStack stack = event.getItem();

    if (!InfinityFruitUtils.isItemFruit(stack)) {
      return;
    }

    if (infinityFruitService.isFruitBlocked(stack)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPickup(EntityPickupItemEvent event) {
    handleInventoryEvent(event.getEntity());
  }

  @EventHandler
  public void onInvClick(InventoryClickEvent event) {
    handleInventoryEvent(event.getWhoClicked());
  }

  @EventHandler
  public void onInvDrag(InventoryDragEvent event) {
    handleInventoryEvent(event.getWhoClicked());
  }

  private void handleInventoryEvent(Entity entity) {
    if (!(entity instanceof Player player)) {
      return;
    }

    infinityFruitService.tagPlayerInventoryInBackground(player);
  }
}
