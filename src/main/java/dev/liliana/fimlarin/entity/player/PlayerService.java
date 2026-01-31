package dev.liliana.fimlarin.entity.player;

import dev.liliana.fimlarin.Main;
import dev.liliana.fimlarin.PluginService;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerService extends PluginService {

  public PlayerService(Main plugin) {
    super(plugin);
  }

  public boolean isInventoryFull(Player player) {
    return player.getInventory().firstEmpty() == -1;
  }

  public void updateInventoryInBackground(Player player) {
    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, player::updateInventory);
  }

  public boolean isItemInInventory(Player player, Material material) {
    return player.getInventory().contains(material);
  }
}
