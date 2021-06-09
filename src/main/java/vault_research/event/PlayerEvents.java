package vault_research.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import vault_research.Vault;
import vault_research.client.experiencelimiter.ExperienceLimiter;
import vault_research.config.MiscConfig;
import vault_research.init.ModNetwork;
import vault_research.world.data.PlayerVaultStatsData;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {

    public static boolean NATURAL_REGEN_OLD_VALUE = false;
    public static boolean MODIFIED_GAMERULE = false;
    
	// Gain vault levels from XP, code modified from PlayerEx mod
	@SubscribeEvent
	public static void onExperiencePickup(final net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp event) {

		// Do nothing if xpGain is disabled in the config
		if (!MiscConfig.xpGain) return;
		
		PlayerEntity player = event.getPlayer();
		
		if(player.world.isRemote) return;
		
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
		
		int xpAmount = event.getOrb().getXpValue();
		
		// Amount of given XP should decrease over time
		xpAmount = ExperienceLimiter.fix(xpAmount, player);
		if (xpAmount == 0) return;
		PlayerVaultStatsData.get(serverPlayer.getServerWorld()).addVaultExp(serverPlayer, xpAmount);
	}
	
	
	// Give player their initial research points, if any
	@SubscribeEvent
	public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getPlayer().world.isRemote) return;
		if (MiscConfig.playerStartingPoints == 0) return;
		
		CompoundNBT playerData = event.getPlayer().getPersistentData();
		CompoundNBT data;
		
		if (!playerData.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
			data = new CompoundNBT();
		} else {
			data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
		}
		
		if (!data.getBoolean("vault_research:receivedStartingPoints") && event.getPlayer().isServerWorld()) {
			PlayerVaultStatsData.get((ServerWorld) event.getPlayer().world)
				.addSkillPoint((ServerPlayerEntity) event.getPlayer(), MiscConfig.playerStartingPoints);
			
			data.putBoolean("vault_research:receivedStartingPoints", true);
			playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
		}
		
	}


}
