package dev.liliana.fimlarin;

import dev.liliana.fimlarin.entity.player.PlayerService;
import dev.liliana.fimlarin.game.infinityfruit.InfinityFruitService;
import dev.liliana.fimlarin.game.shadowinventory.ShadowInventoryRepository;
import dev.liliana.fimlarin.game.shadowinventory.ShadowInventoryService;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

  private final Map<Class<? extends PluginService>, PluginService> services = new HashMap<>();

  public Main() {
    ShadowInventoryRepository shadowInventoryCollection = new ShadowInventoryRepository();

    PlayerService playerService = new PlayerService(this);
    ShadowInventoryService shadowInventoryService = new ShadowInventoryService(
        this, shadowInventoryCollection,
        playerService);

    InfinityFruitService infinityFruitService = new InfinityFruitService(this, shadowInventoryService);

    registerServices(playerService, shadowInventoryService, infinityFruitService);
  }

  @Override
  public void onEnable() {
    services.values().forEach(PluginService::onEnable);
  }

  @Override
  public void onDisable() {
    services.values().forEach(PluginService::onDisable);
  }

  private void registerServices(PluginService...services) {
    for (PluginService service : services) {
      this.services.put(service.getClass(), service);
    }
  }
}
