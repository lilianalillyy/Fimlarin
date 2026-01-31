package dev.liliana.fimlarin;

import dev.liliana.fimlarin.events.InventoryUpdate;
import dev.liliana.fimlarin.inventory.ShadowInventoryGlobalState;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

  private final ShadowInventoryGlobalState shadowInventoryState = new ShadowInventoryGlobalState();

  @Override
  public void onEnable() {
    InventoryUpdate inventoryUpdate = new InventoryUpdate(shadowInventoryState, this);
    getServer().getPluginManager().registerEvents(inventoryUpdate, this);
  }

  @Override
  public void onDisable() {
    shadowInventoryState.flushAll(this);
  }
}
