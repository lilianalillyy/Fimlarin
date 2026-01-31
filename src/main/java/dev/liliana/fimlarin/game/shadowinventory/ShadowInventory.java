package dev.liliana.fimlarin.game.shadowinventory;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class ShadowInventory {

  // Pending stacks are stacks that are not yet in the inventory, but will be once the inventory is updated.
  private final List<ItemStack> pendingStacks = new ArrayList<>();

  public List<ItemStack> getPendingStacks() {
    return pendingStacks;
  }

}
