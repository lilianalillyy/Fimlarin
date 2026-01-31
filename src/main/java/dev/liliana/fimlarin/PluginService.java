package dev.liliana.fimlarin;

import org.bukkit.event.Listener;

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
