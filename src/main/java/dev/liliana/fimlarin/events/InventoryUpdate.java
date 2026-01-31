package dev.liliana.fimlarin.events;

import dev.liliana.fimlarin.inventory.ShadowInventory;
import dev.liliana.fimlarin.inventory.ShadowInventoryGlobalState;
import dev.liliana.fimlarin.inventory.ShadowInventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryUpdate implements Listener {

  private final ShadowInventoryGlobalState shadowInventoryState;

  private final JavaPlugin plugin;

  public InventoryUpdate(ShadowInventoryGlobalState shadowInventoryState, JavaPlugin plugin) {
    this.shadowInventoryState = shadowInventoryState;
    this.plugin = plugin;
  }

  @EventHandler
  public void onCraftingTableOpen(InventoryOpenEvent event) {
    if (!(event.getPlayer() instanceof Player player)) {
      return;
    }

    shadowInventoryState.flush(player);
    updateInventoryInBackground(player);
  }

  @EventHandler(ignoreCancelled = true)
  public void onItemPickup(EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player player) || isInventoryUnlocked(player)) {
      return;
    }

    ItemStack stack = event.getItem().getItemStack().clone();

    boolean hasFullInventory = player.getInventory().firstEmpty() == -1;

    // If the player inventory is already full, just act naturally as the item is not supposed to get picked up.
    if (hasFullInventory) {
      return;
    }

    ItemStack incoming = event.getItem().getItemStack().clone();

    ShadowInventory shadow = shadowInventoryState.getInventory(player);

    // If we already have a shadow inventory, simulate player's inventory with applied shadow inventory
    // to check if the incoming item fits.
    if (shadow != null) {
      ItemStack[] simulatedInv = ShadowInventoryUtils.createSimulatedInventory(player, shadow);
      boolean fitsWithShadow = ShadowInventoryUtils.canFit(simulatedInv, incoming);

      if (!fitsWithShadow) {
        // Can't accept into shadow without overflow:
        // keep illusion: don't pick up, keep item on ground
        event.setCancelled(true);
        return;
      }
    } else {
      shadow = shadowInventoryState.createInventory(player);
    }

    shadow.getPendingAdds().add(stack);

    event.setCancelled(true);
    event.getItem().remove();
    updateInventoryInBackground(player);
  }

  @EventHandler(ignoreCancelled = true)
  public void onItemDrop(PlayerDropItemEvent e) {
    Player player = e.getPlayer();

    if (isInventoryUnlocked(player)) {
      return;
    }

    ItemStack dropped = e.getItemDrop().getItemStack().clone();

    ShadowInventory shadow = shadowInventoryState.getInventory(player);

    if (shadow == null) {
      shadow = shadowInventoryState.createInventory(player);
    }

    shadow.getPendingDrops().add(dropped);

    e.setCancelled(true);
    updateInventoryInBackground(player);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    // Ensure that the inventory is updated when the player leaves the server.
    shadowInventoryState.flush(e.getPlayer());
  }

  private void updateInventoryInBackground(Player player) {
    plugin.getServer().getScheduler().runTask(plugin, player::updateInventory);
  }

  private boolean isInventoryUnlocked(Player player) {
    return !player.getInventory().contains(Material.CRAFTING_TABLE);
  }
}
