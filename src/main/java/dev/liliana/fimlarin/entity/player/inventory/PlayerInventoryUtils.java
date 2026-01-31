package dev.liliana.fimlarin.entity.player.inventory;

import org.bukkit.inventory.ItemStack;

public class PlayerInventoryUtils {

  /**
   * Attempts to insert as much of the given stack as possible into the given inventory contents.
   *
   * @param contents      the inventory to insert into
   * @param stackToInsert the item stack to insert
   * @return true if the entire stack was inserted; false if some amount could not fit.
   */
  public static boolean tryInsertIntoContents(ItemStack[] contents, ItemStack stackToInsert) {
    ItemStack remaining = stackToInsert.clone();

    // First, let's try to top off existing stacks.
    for (ItemStack currentStack : contents) {
      if (isEmptySlot(currentStack)) {
        continue;
      }

      if (!currentStack.isSimilar(remaining)) {
        continue;
      }

      int max = currentStack.getMaxStackSize();
      int space = max - currentStack.getAmount();

      if (space <= 0) {
        // The current stack is full.
        continue;
      }

      // The amount of space left in the current stack to fit at least part of the remaining stack.
      int move = Math.min(space, remaining.getAmount());

      // Increment the amount of the current stack and remove it from the remaining stack.
      currentStack.setAmount(currentStack.getAmount() + move);
      remaining.setAmount(remaining.getAmount() - move);

      if (remaining.getAmount() <= 0) {
        // There's nothing left to insert, so the item can fit.
        return true;
      }
    }

    // Next, let's iterate over the contents again and try to fill empty slots.
    for (int i = 0; i < contents.length; i++) {
      ItemStack currentStack = contents[i];

      if (!isEmptySlot(currentStack)) {
        continue;
      }

      int max = remaining.getMaxStackSize();
      int move = Math.min(max, remaining.getAmount());

      // Create a new stack, place it in the inventory and set the amount of the new stack to the amount we can remove from the remaining stack.
      ItemStack placed = remaining.clone();
      placed.setAmount(move);
      contents[i] = placed;

      // Decrement the amount of the remaining stack.
      remaining.setAmount(remaining.getAmount() - move);

      if (remaining.getAmount() <= 0) {
        // There's nothing left to insert, so the item can fit.
        return true;
      }
    }

    return false;
  }

  /**
   * Creates a new array with clones of the stacks in the given array.
   *
   * @param contents the array to clone
   * @return a clone of the given array
   */
  public static ItemStack[] cloneContents(ItemStack[] contents) {
    ItemStack[] clone = new ItemStack[contents.length];

    for (int i = 0; i < contents.length; i++) {
      ItemStack it = contents[i];
      clone[i] = (it == null) ? null : it.clone();
    }

    return clone;
  }

  /**
   * Checks if the stack represents an empty slot.
   *
   * @param stack the stack to check
   * @return true if the stack is null, air or has an amount of 0; false otherwise
   */
  public static boolean isEmptySlot(ItemStack stack) {
    return stack == null || stack.getType().isAir() || stack.getAmount() <= 0;
  }

}
