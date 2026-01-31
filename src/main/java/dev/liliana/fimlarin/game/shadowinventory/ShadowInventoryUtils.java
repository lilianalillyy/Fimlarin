package dev.liliana.fimlarin.game.shadowinventory;

import dev.liliana.fimlarin.entity.player.inventory.PlayerInventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShadowInventoryUtils {

  /**
   * Creates an array of inventory contents for the given player, combining their inventory with the
   * pending stacks.
   *
   * @param player the player to simulate the inventory for
   * @param shadow the shadow inventory to simulate
   * @return combined inventory contents
   */
  public static ItemStack[] createCombinedInventoryContents(Player player, ShadowInventory shadow) {
    ItemStack[] contents = PlayerInventoryUtils.cloneContents(player.getInventory().getContents());

    for (ItemStack pendingStack : shadow.getPendingStacks()) {
      PlayerInventoryUtils.tryInsertIntoContents(contents, pendingStack);
    }

    return contents;
  }


}
