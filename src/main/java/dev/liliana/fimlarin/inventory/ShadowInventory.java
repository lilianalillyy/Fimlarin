package dev.liliana.fimlarin.inventory;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class ShadowInventory {

  private final List<ItemStack> pendingAdds = new ArrayList<>();

  private final List<ItemStack> pendingDrops = new ArrayList<>();

  public List<ItemStack> getPendingAdds() {
    return pendingAdds;
  }

  public List<ItemStack> getPendingDrops() {
    return pendingDrops;
  }
}
