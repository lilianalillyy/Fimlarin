package dev.liliana.fimlarin.game.infinityfruit;

import dev.liliana.fimlarin.entity.player.inventory.PlayerInventoryUtils;
import java.security.SecureRandom;
import java.util.EnumSet;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InfinityFruitUtils {
  private static final Set<Material> FRUITS = EnumSet.of(
      Material.APPLE,
      Material.GOLDEN_APPLE,
      Material.ENCHANTED_GOLDEN_APPLE,
      Material.MELON_SLICE,
      Material.SWEET_BERRIES,
      Material.GLOW_BERRIES,
      Material.CHORUS_FRUIT
  );

  private static final double FRUIT_BASE_CHANCE = 0.005; // 0.5%

  private static final double FRUIT_SHADOWED_INVENTORY_MULTIPLIER = 5.0; // x5 => 2.5%

  private static final double FRUIT_MAX_CHANCE = 0.25;

  private static final SecureRandom rng = new SecureRandom();

  public static double getFruitChance(boolean isInventoryShadowed) {
    double chance = FRUIT_BASE_CHANCE;

    if (isInventoryShadowed) {
      chance *= FRUIT_SHADOWED_INVENTORY_MULTIPLIER;
    }

    return Math.min(chance, FRUIT_MAX_CHANCE);
  }

  public static boolean roll(double chance) {
    return rng.nextDouble() < chance;
  }

  public static boolean isItemFruit(ItemStack stack) {
    if (PlayerInventoryUtils.isNonExistentItem(stack)) {
      return false;
    }

    return isItemFruit(stack.getType());
  }

  public static boolean isItemFruit(Material material) {
    return FRUITS.contains(material);
  }
}
