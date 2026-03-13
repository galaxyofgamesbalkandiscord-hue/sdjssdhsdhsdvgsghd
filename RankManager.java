package xyz.novasmp.novasmpbedrockranks;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RankManager {

    private final NovaSMPBedrockRanks plugin;
    private final Map<String, String> bedrockPrefixes = new HashMap<>();
    private final Map<String, String> javaPrefixes = new HashMap<>();

    public RankManager(NovaSMPBedrockRanks plugin) {
        this.plugin = plugin;
        loadPrefixes();
    }

    private void loadPrefixes() {
        if (plugin.getConfig().contains("ranks")) {
            for (String group : plugin.getConfig().getConfigurationSection("ranks").getKeys(false)) {
                String bedrock = plugin.getConfig().getString("ranks." + group + ".bedrock", "&7[&f" + group + "&7] ");
                String java = plugin.getConfig().getString("ranks." + group + ".java", "%img_rank_" + group + "% ");
                bedrockPrefixes.put(group.toLowerCase(), bedrock);
                javaPrefixes.put(group.toLowerCase(), java);
            }
        }
    }

    public void updatePlayerPrefix(Player player) {
        try {
            TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());
            if (tabPlayer == null) return;

            String group = getPlayerGroup(player);
            boolean isBedrock = plugin.isBedrockPlayer(player);

            String prefix = isBedrock
                    ? bedrockPrefixes.getOrDefault(group, "&7[&fMember&7] ")
                    : javaPrefixes.getOrDefault(group, "%img_rank_igrac% ");

            TabAPI.getInstance().getTablistFormatManager().setPrefix(tabPlayer, prefix);
            TabAPI.getInstance().getTeamManager().setPrefix(tabPlayer, prefix);

        } catch (Exception e) {
            plugin.getLogger().warning("Greska pri azuriranju prefiksa za " + player.getName() + ": " + e.getMessage());
        }
    }

    private String getPlayerGroup(Player player) {
        try {
            User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
            if (user != null) return user.getPrimaryGroup().toLowerCase();
        } catch (Exception e) {
            plugin.getLogger().warning("Ne mogu dobiti grupu za " + player.getName());
        }
        return "default";
    }

    public void reloadPrefixes() {
        bedrockPrefixes.clear();
        javaPrefixes.clear();
        plugin.reloadConfig();
        loadPrefixes();
    }
}
