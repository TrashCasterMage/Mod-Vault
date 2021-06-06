package vault_research.research;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;
import vault_research.Vault;
import vault_research.init.ModConfigs;
import vault_research.init.ModNetwork;
import vault_research.network.message.ResearchTreeMessage;
import vault_research.research.type.Research;
import vault_research.util.NetcodeUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public class ResearchTree implements INBTSerializable<CompoundNBT> {

	// HashMap<Player, Team>
	protected static Map<UUID, UUID> teamMap = new HashMap<>();
    protected UUID teamUUID;
    protected List<String> researchesDone;

    public ResearchTree(UUID playerUUID) {
    	this.teamUUID = teamMap.computeIfAbsent(playerUUID, k -> UUID.randomUUID());
        //this.playerUUID = playerUUID;
        this.researchesDone = new LinkedList<>();
    }
    
    public ResearchTree(String ID) {
    	this.teamUUID = UUID.fromString(ID);
    	//this.teamUUID = teamUUID;
    	//teamMap.put(playerUUID, teamUUID);
    	
    	this.researchesDone = new LinkedList<>();
    }
    
    public static Set<Entry<UUID, UUID>> getTeamMapSet() {
    	return teamMap.entrySet();
    }
    
    public static Map<UUID, UUID> requestTeamMap() {
    	if (teamMap.isEmpty()) return null;
    	
    	return teamMap;
    }
    
    public static boolean offerTeamMap(Map<UUID, UUID> map) {  	
    	teamMap = map;
    	return true;
    }
    
    public static UUID getOrCreateTeam(UUID playerUUID) {
    	return teamMap.computeIfAbsent(playerUUID, k -> UUID.randomUUID());
    }

    public List<String> getResearchesDone() {
        return researchesDone;
    }

    public boolean isResearched(String researchName) {
        return this.researchesDone.contains(researchName);
    }

    public void research(String researchName) {
        this.researchesDone.add(researchName);
    }

    public void resetAll() {
        this.researchesDone.clear();
    }
    
    public String restrictedBy(RegistryKey<World> dim, Restrictions.Type restrictionType) {
    	ResourceLocation dimLoc = dim.getLocation();
    	for (Research research : ModConfigs.RESEARCHES.getAll()) {
    		if (researchesDone.contains(research.getName())) continue;
    		if (research.restricts(dimLoc, restrictionType)) return research.getName();
    	}
    	return null;
    }

    public String restrictedBy(Item item, Restrictions.Type restrictionType) {
        for (Research research : ModConfigs.RESEARCHES.getAll()) {
            if (researchesDone.contains(research.getName())) continue;
            if (research.restricts(item, restrictionType)) return research.getName();
        }
        return null;
    }

    public String restrictedBy(Block block, Restrictions.Type restrictionType) {
        for (Research research : ModConfigs.RESEARCHES.getAll()) {
            if (researchesDone.contains(research.getName())) continue;
            if (research.restricts(block, restrictionType)) return research.getName();
        }
        return null;
    }

    public String restrictedBy(EntityType<?> entityType, Restrictions.Type restrictionType) {
        for (Research research : ModConfigs.RESEARCHES.getAll()) {
            if (researchesDone.contains(research.getName())) continue;
            if (research.restricts(entityType, restrictionType)) return research.getName();
        }
        return null;
    }

    public void sync(MinecraftServer server) {
    	//throw new ArrayIndexOutOfBoundsException();
    	for(Entry<UUID, UUID> pair : teamMap.entrySet()) {
    		if (pair.getValue().equals(this.teamUUID)) {
    			NetcodeUtils.runIfPresent(server, pair.getKey(), player -> {
    	            ModNetwork.CHANNEL.sendTo(
    	                    new ResearchTreeMessage(this, player.getUniqueID()),
    	                    player.connection.netManager,
    	                    NetworkDirection.PLAY_TO_CLIENT
    	            );
    	        });
    		}
    	}
        
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putUniqueId("teamUUID", this.teamUUID);

        ListNBT researches = new ListNBT();
        for (int i = 0; i < researchesDone.size(); i++) {
            CompoundNBT research = new CompoundNBT();
            research.putString("name", researchesDone.get(i));
            researches.add(i, research);
        }
        nbt.put("researches", researches);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.teamUUID = nbt.getUniqueId("teamUUID");

        ListNBT researches = nbt.getList("researches", Constants.NBT.TAG_COMPOUND);
        this.researchesDone = new LinkedList<>();
        for (int i = 0; i < researches.size(); i++) {
            CompoundNBT researchNBT = researches.getCompound(i);
            String name = researchNBT.getString("name");
            this.researchesDone.add(name);
        }
    }
}
