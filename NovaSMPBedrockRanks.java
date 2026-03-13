package xyz.novasmp.novasmpbedrockranks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import me.neznamy.tab.api.TabAPI;

public class NovaSMPBedrockRanks extends JavaPlugin {

    private static NovaSMPBedrockRanks instance;
    private RankManager rankManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if (!setupFloodgate()) {
            getLogger().severe("Floodgate nije pronađen! Plugin se gasi.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupTAB()) {
            getLogger().severe("TAB plugin nije pronađen! Plugin se gasi.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        rankManager = new RankManager(this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                rankManager.updatePlayerPrefix(player);
            }
        }, 20L);

        getLogger().info("NovaSMPBedrockRanks uspjesno pokrenut!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NovaSMPBedrockRanks ugasen.");
    }

    private boolean setupFloodgate() {
        if (Bukkit.getPluginManager().getPlugin("floodgate") == null) return false;
        try { FloodgateApi.getInstance(); return true; } catch (Exception e) { return false; }
    }

    private boolean setupTAB() {
        if (Bukkit.getPluginManager().getPlugin("TAB") == null) return false;
        try { TabAPI.getInstance(); return true; } catch (Exception e) { return false; }
    }

    public static NovaSMPBedrockRanks getInstance() { return instance; }
    public RankManager getRankManager() { return rankManager; }

    public boolean isBedrockPlayer(Player player) {
        try {
            return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        } catch (Exception e) {
            return false;
        }
    }
}
