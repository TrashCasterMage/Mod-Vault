package vault_research.client.experiencelimiter;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vault_research.Vault;
import vault_research.config.MiscConfig;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExperienceLimiter {
	
	private static Map<ChunkPos, Integer> killMap = new HashMap<>();
	private static final int decreaseThreshold = 1;
	private static final int decreaseAmount = 1;
	
	@SubscribeEvent
	public static void onChunkDataLoadEvent(ChunkDataEvent.Load event) {
		if (event.getWorld().isRemote()) return;
		
		CompoundNBT data = event.getData();
		String key = Vault.MOD_ID + ":chunkKillCount";
		if (data.contains(key)) {
			ChunkPos pos = event.getChunk().getPos();
			killMap.put(pos,  data.getInt(key));
		}
	}
	
	@SubscribeEvent
	public static void onChunkDataSaveEvent(ChunkDataEvent.Save event) {
		if (event.getWorld().isRemote()) return;
		
		int kills = killMap.getOrDefault(event.getChunk().getPos(), 0);
		
		if (kills > 0) {
			CompoundNBT data = event.getData();
			String key = Vault.MOD_ID + ":chunkKillCount";
			data.putInt(key, kills);
		}
	}
	
	@SubscribeEvent
	public static void onMobDeath(net.minecraftforge.event.entity.living.LivingExperienceDropEvent event) {
		//Vault.LOGGER.debug("original: " + event.getOriginalExperience());
		//Vault.LOGGER.debug("dropped: " + event.getDroppedExperience());
		if (event.getEntityLiving().world.isRemote) return;
		if (event.getOriginalExperience() == 0) return;
		
		World world = event.getEntityLiving().getEntityWorld();
		Chunk chunk = world.getChunkAt(event.getEntityLiving().getPosition());
		
		int kills = killMap.getOrDefault(chunk.getPos(), 0) + 1;
		killMap.put(chunk.getPos(), kills);

	}
	
	public static int fix(int xpAmount, PlayerEntity player) {
		if (!MiscConfig.noResearchFarming) return xpAmount;
		
		World world = player.getEntityWorld();
		Chunk chunk = world.getChunkAt(player.getPosition());
		
		int kills = killMap.getOrDefault(chunk.getPos(), 0);
		int multiplier = kills / decreaseThreshold;
		int decrease = decreaseAmount * multiplier;
		
		return Math.max(xpAmount - decrease, 0);
	}

}
