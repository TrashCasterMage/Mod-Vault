package vault_research.world.data;

import net.minecraft.client.Minecraft;
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
import vault_research.research.type.Research;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerResearchesData extends WorldSavedData {

    protected static final String DATA_NAME = Vault.MOD_ID + "_PlayerResearches";

    private Map<UUID, ResearchTree> teamMap = new HashMap<>();

    public PlayerResearchesData() {
        super(DATA_NAME);
    }

    public PlayerResearchesData(String name) {
        super(name);
    }

    public ResearchTree getResearches(PlayerEntity player) {
        return getResearches(player.getUniqueID());
    }

    public ResearchTree getResearches(UUID uuid) {
    	UUID teamId = ResearchTree.getOrCreateTeam(uuid);
    	//Vault.LOGGER.debug("Got team id: " + teamId);
        return this.teamMap.computeIfAbsent(teamId, id -> new ResearchTree(id.toString()));
    }
    
    public void syncAll(MinecraftServer server) {
    	PlayerList players = server.getPlayerList();
    	for (ServerPlayerEntity player: players.getPlayers()) {
    		this.getResearches(player).sync(server);
    	}
    }
    
    public ResearchTree forceAddResearchTree(UUID team, ResearchTree researchTree) {
    	teamMap.put(team, researchTree);
    	return researchTree;
    }
    
    public ResearchTree cloneTeam(UUID from, UUID to) {
    	teamMap.put(to, new ResearchTree(to, teamMap.get(from).getResearchesDone()));
    	return teamMap.get(to);
    }
    
    public ResearchTree getExistingResearch(UUID teamId) {
    	return teamMap.get(teamId);
    }
    
    public void teamDeleted(UUID teamId) {
    	teamMap.remove(teamId);
    }

    /* ------------------------------- */

    public PlayerResearchesData research(ServerPlayerEntity player, Research research) {
        ResearchTree researchTree = getResearches(player);
        researchTree.research(research.getName());

        researchTree.sync(player.getServer());

        markDirty();
        return this;
    }

    public PlayerResearchesData resetResearchTree(ServerPlayerEntity player) {
        ResearchTree researchTree = getResearches(player);
        researchTree.resetAll();

        researchTree.sync(player.getServer());

        markDirty();
        return this;
    }

    /* ------------------------------- */

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT teamsList = nbt.getList("TeamEntries", Constants.NBT.TAG_STRING);
        ListNBT researchesList = nbt.getList("ResearchEntries", Constants.NBT.TAG_COMPOUND);
        ListNBT playerTeamList = nbt.getList("PlayerTeamEntries", Constants.NBT.TAG_COMPOUND);

        if (teamsList.size() != researchesList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }
                
        Map<UUID, UUID> playerTeams = new HashMap<>();
        for (int i = 0; i < playerTeamList.size(); i++) {
        	CompoundNBT pair = playerTeamList.getCompound(i);
        	playerTeams.put(UUID.fromString(pair.get("player").getString()), UUID.fromString(pair.get("team").getString()));
        }
        
        ResearchTree.offerTeamMap(playerTeams);

        for (int i = 0; i < playerTeamList.size(); i++) {
        	UUID playerUUID = UUID.fromString(playerTeamList.getCompound(i).getString("player"));
            //UUID teamUUID = UUID.fromString(teamsList.getString(i));
            this.getResearches(playerUUID).deserializeNBT(researchesList.getCompound(i));
            
        }
                
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        ListNBT teamsList = new ListNBT();
        ListNBT researchesList = new ListNBT();
        ListNBT playerTeamList = new ListNBT();
        
        Map<UUID, UUID> teams = ResearchTree.requestTeamMap();

        this.teamMap.forEach((uuid, researchTree) -> {
            teamsList.add(StringNBT.valueOf(uuid.toString()));
            researchesList.add(researchTree.serializeNBT());
        });
        
        teams.forEach((player, team) -> {
        	CompoundNBT pair = new CompoundNBT();
        	pair.put("player", StringNBT.valueOf(player.toString()));
        	pair.put("team", StringNBT.valueOf(team.toString()));
        	playerTeamList.add(pair);
        });

        nbt.put("TeamEntries", teamsList);
        nbt.put("ResearchEntries", researchesList);
        nbt.put("PlayerTeamEntries", playerTeamList);

        return nbt;
    }

    public static PlayerResearchesData get(ServerWorld world) {
        return world.getServer().func_241755_D_()
                .getSavedData().getOrCreate(PlayerResearchesData::new, DATA_NAME);
    }

}
