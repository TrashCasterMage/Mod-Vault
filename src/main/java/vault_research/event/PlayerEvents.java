package vault_research.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
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
import vault_research.init.ModNetwork;
import vault_research.world.data.PlayerVaultStatsData;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {

    public static boolean NATURAL_REGEN_OLD_VALUE = false;
    public static boolean MODIFIED_GAMERULE = false;
    
	// Gain vault levels from XP, code modified from PlayerEx mod
	@SubscribeEvent
	public static void onExperiencePickup(final net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp event) {
		PlayerEntity player = event.getPlayer();
		
		if(player.world.isRemote) return;
		
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
		
		int xpAmount = event.getOrb().getXpValue();
		
		PlayerVaultStatsData.get(serverPlayer.getServerWorld()).addVaultExp(serverPlayer, xpAmount);
	}


}
