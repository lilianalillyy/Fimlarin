package dev.liliana.fimlarin;

import org.bukkit.event.Listener;

/**
 * Base class for services that are registered with the plugin.
 */
public abstract class PluginService {

  protected final Main plugin;

  public PluginService(Main plugin) {
    this.plugin = plugin;
  }

  public void onEnable() {
  }

  public void onDisable() {
  }

  public void registerListeners(Listener...listeners) {
    for (Listener listener : listeners) {
      plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
  }
}
