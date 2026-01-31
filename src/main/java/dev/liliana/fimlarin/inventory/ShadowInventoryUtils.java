package dev.liliana.fimlarin.inventory;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShadowInventoryUtils {
  public static ItemStack[] createSimulatedInventory(Player player, ShadowInventory shadow) {
    ItemStack[] sim = cloneContents(player.getInventory().getContents());
    ShadowInventoryUtils.applyRemovals(sim, shadow.getPendingDrops());
    ShadowInventoryUtils.applyAdds(sim, shadow.getPendingAdds());

    return sim;
  }

  private static void applyAdds(ItemStack[] contents, List<ItemStack> adds) {
    for (ItemStack add : adds) {
      // We don't care if it "overflows" here if you enforce canFit before accepting adds.
      canFit(contents, add);
    }
  }

  private static void applyRemovals(ItemStack[] contents, List<ItemStack> removes) {
    for (ItemStack rem : removes) {
      int need = rem.getAmount();
      for (int i = 0; i < contents.length && need > 0; i++) {
        ItemStack cur = contents[i];
        if (cur == null || cur.getType().isAir()) continue;
        if (!cur.isSimilar(rem)) continue;

        int take = Math.min(cur.getAmount(), need);
        cur.setAmount(cur.getAmount() - take);
        need -= take;

        if (cur.getAmount() <= 0) contents[i] = null;
      }
    }
  }

  public static boolean canFit(ItemStack[] contents, ItemStack toAdd) {
    ItemStack remaining = toAdd.clone();

    // 1) top off similar stacks
    for (int i = 0; i < contents.length; i++) {
      ItemStack cur = contents[i];
      if (cur == null || cur.getType().isAir()) continue;
      if (!cur.isSimilar(remaining)) continue;

      int max = cur.getMaxStackSize();
      int space = max - cur.getAmount();
      if (space <= 0) continue;

      int move = Math.min(space, remaining.getAmount());
      cur.setAmount(cur.getAmount() + move);
      remaining.setAmount(remaining.getAmount() - move);

      if (remaining.getAmount() <= 0) return true;
    }

    // 2) place into empty slots
    for (int i = 0; i < contents.length; i++) {
      ItemStack cur = contents[i];
      if (cur != null && !cur.getType().isAir()) continue;

      int max = remaining.getMaxStackSize();
      int move = Math.min(max, remaining.getAmount());
      ItemStack placed = remaining.clone();
      placed.setAmount(move);
      contents[i] = placed;

      remaining.setAmount(remaining.getAmount() - move);
      if (remaining.getAmount() <= 0) return true;
    }

    return false;
  }

  private static ItemStack[] cloneContents(ItemStack[] contents) {
    ItemStack[] out = new ItemStack[contents.length];
    for (int i = 0; i < contents.length; i++) {
      ItemStack it = contents[i];
      out[i] = (it == null) ? null : it.clone();
    }
    return out;
  }

}
