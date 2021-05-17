package vault_research.world.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
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

    private Map<UUID, ResearchTree> playerMap = new HashMap<>();

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
        return this.playerMap.computeIfAbsent(uuid, ResearchTree::new);
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
        ListNBT playerList = nbt.getList("PlayerEntries", Constants.NBT.TAG_STRING);
        ListNBT researchesList = nbt.getList("ResearchEntries", Constants.NBT.TAG_COMPOUND);

        if (playerList.size() != researchesList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            this.getResearches(playerUUID).deserializeNBT(researchesList.getCompound(i));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        ListNBT playerList = new ListNBT();
        ListNBT researchesList = new ListNBT();

        this.playerMap.forEach((uuid, researchTree) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));
            researchesList.add(researchTree.serializeNBT());
        });

        nbt.put("PlayerEntries", playerList);
        nbt.put("ResearchEntries", researchesList);

        return nbt;
    }

    public static PlayerResearchesData get(ServerWorld world) {
        return world.getServer().func_241755_D_()
                .getSavedData().getOrCreate(PlayerResearchesData::new, DATA_NAME);
    }

}
