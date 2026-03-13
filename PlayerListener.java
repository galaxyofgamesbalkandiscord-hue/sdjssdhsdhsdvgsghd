package xyz.novasmp.novasmpbedrockranks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final NovaSMPBedrockRanks plugin;

    public PlayerListener(NovaSMPBedrockRanks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joined = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getRankManager().updatePlayerPrefix(joined);
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.equals(joined)) {
                    plugin.getRankManager().updatePlayerPrefix(online);
                }
            }
        }, 40L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player online : Bukkit.getOnlinePlayers()) {
                plugin.getRankManager().updatePlayerPrefix(online);
            }
        }, 5L);
    }
}
