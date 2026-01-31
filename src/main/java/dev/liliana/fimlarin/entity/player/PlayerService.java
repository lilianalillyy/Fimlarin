package dev.liliana.fimlarin.entity.player;

import dev.liliana.fimlarin.Main;
import dev.liliana.fimlarin.PluginService;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerService extends PluginService {

  public PlayerService(Main plugin) {
    super(plugin);
  }

  /**
   * Checks if a player's inventory is full.
   *
   * @param player Player to check
   * @return true if the player's inventory is full; false otherwise
   */
  public boolean isInventoryFull(Player player) {
    return player.getInventory().firstEmpty() == -1;
  }

  /**
   * Adds a task to the server's scheduler to update the player's inventory in the background.
   *
   * @param player Player to update inventory for
   */
  public void updateInventoryInBackground(Player player) {
    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, player::updateInventory);
  }

  /**
   * Checks if a player has a specific item in their inventory.
   *
   * @param player   Player to check
   * @param material Material to check for
   * @return true if the player has the item; false otherwise
   */
  public boolean isItemInInventory(Player player, Material material) {
    return player.getInventory().contains(material);
  }
}
