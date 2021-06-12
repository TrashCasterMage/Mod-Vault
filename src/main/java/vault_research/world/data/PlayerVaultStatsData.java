package vault_research.world.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import vault_research.Vault;
import vault_research.research.ResearchTree;
import vault_research.skill.PlayerVaultStats;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerVaultStatsData extends WorldSavedData {

    protected static final String DATA_NAME = Vault.MOD_ID + "_PlayerVaultLevels";

    private Map<UUID, PlayerVaultStats> teamMap = new HashMap<>();

    public PlayerVaultStatsData() {
        super(DATA_NAME);
    }

    public PlayerVaultStatsData(String name) {
        super(name);
    }
    
    public void syncAll(MinecraftServer server) {
    	PlayerList players = server.getPlayerList();
    	for (ServerPlayerEntity player: players.getPlayers()) {
    		this.getVaultStats(player).sync(server);
    	}
    }

    public PlayerVaultStats getVaultStats(PlayerEntity player) {
        PlayerVaultStats stats = getVaultStats(player.getUniqueID());
    	getVaultStats(player.getUniqueID()).sync(player.getServer());
    	return stats;
    }

    public PlayerVaultStats getVaultStats(UUID uuid) {
    	UUID teamId = ResearchTree.getOrCreateTeam(uuid);
        return this.teamMap.computeIfAbsent(teamId, PlayerVaultStats::new);
    }
    
    public PlayerVaultStats getVaultStats(String teamUUID) {
    	return this.teamMap.computeIfAbsent(UUID.fromString(teamUUID), PlayerVaultStats::new);
    }
    
    public PlayerVaultStats cloneTeam(UUID from, UUID to) {
    	teamMap.put(to, new PlayerVaultStats(to, teamMap.get(from)));
    	return teamMap.get(to);
    }
    
    public void teamDeleted(UUID teamId) {
    	teamMap.remove(teamId);
    }

    /* ------------------------------- */

    public PlayerVaultStatsData setVaultLevel(ServerPlayerEntity player, int level) {
        this.getVaultStats(player).setVaultLevel(player.getServer(), level);

        markDirty();
        return this;
    }

    public PlayerVaultStatsData addVaultExp(ServerPlayerEntity player, int exp) {
        this.getVaultStats(player).addVaultExp(player.getServer(), exp);

        markDirty();
        return this;
    }

    public PlayerVaultStatsData spendSkillPts(ServerPlayerEntity player, int amount) {
        this.getVaultStats(player).spendSkillPoints(player.getServer(), amount).sync(player.getServerWorld().getServer());

        markDirty();
        return this;
    }

    public PlayerVaultStatsData addSkillPoint(ServerPlayerEntity player, int amount) {
        this.getVaultStats(player)
                .addSkillPoints(amount)
                .sync(player.getServerWorld().getServer());

        markDirty();
        return this;
    }

    public PlayerVaultStatsData reset(ServerPlayerEntity player) {
        this.getVaultStats(player).reset(player.getServer());

        markDirty();
        return this;
    }
    
    public int getSpentSkillPts(ServerPlayerEntity player) {
    	return this.getVaultStats(player).getSpentSkillPts();
    }

    /* ------------------------------- */

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT teamList = nbt.getList("TeamEntries", Constants.NBT.TAG_STRING);
        ListNBT statEntries = nbt.getList("StatEntries", Constants.NBT.TAG_COMPOUND);

        if (teamList.size() != statEntries.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < teamList.size(); i++) {
            UUID teamUUID = UUID.fromString(teamList.getString(i));
            this.getVaultStats(teamUUID.toString()).deserializeNBT(statEntries.getCompound(i));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        ListNBT teamList = new ListNBT();
        ListNBT statsList = new ListNBT();

        this.teamMap.forEach((uuid, stats) -> {
            teamList.add(StringNBT.valueOf(uuid.toString()));
            statsList.add(stats.serializeNBT());
        });

        nbt.put("TeamEntries", teamList);
        nbt.put("StatEntries", statsList);

        return nbt;
    }

    public static PlayerVaultStatsData get(ServerWorld world) {
        return world.getServer().func_241755_D_()
                .getSavedData().getOrCreate(PlayerVaultStatsData::new, DATA_NAME);
    }

}
