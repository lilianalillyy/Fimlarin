package dev.liliana.fimlarin.game.shadowinventory;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class ShadowInventory {

  private final List<ItemStack> pendingStacks = new ArrayList<>();

  public List<ItemStack> getPendingStacks() {
    return pendingStacks;
  }

}
