package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.init.*;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {
	
	// Gain vault levels, code modified from PlayerEx mod
	// and add_xp vault command
	@SubscribeEvent
	public static void onExperiencePickup(final net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp event) {
		PlayerEntity player = event.getPlayer();
		
		if(player.world.isRemote) return;
		
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
		
		int xpAmount = event.getOrb().getXpValue();
		
		PlayerVaultStatsData.get(serverPlayer.getServerWorld()).addVaultExp(serverPlayer, xpAmount);
	}

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (event.getEntity().world.isRemote) return;
        if (event.getEntity().world.getDimensionKey() != Vault.VAULT_KEY) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntitySpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.getEntity().getEntityWorld().getDimensionKey() == Vault.VAULT_KEY && !event.isSpawner()) {
            event.setCanceled(true);
        }
    }

	@SubscribeEvent
	public static void onPlayerDeathInVaults(LivingDeathEvent event) {
		LivingEntity entityLiving = event.getEntityLiving();

		if(entityLiving.world.isRemote)return;
		if(!(entityLiving instanceof ServerPlayerEntity))return;
		if(entityLiving.world.getDimensionKey() != Vault.VAULT_KEY)return;

		ServerPlayerEntity player = (ServerPlayerEntity)entityLiving;
        Vector3d position = player.getPositionVec();
		player.getServerWorld().playSound(null, position.x, position.y, position.z,
                ModSounds.TIMER_KILL_SFX, SoundCategory.MASTER, 0.75F, 1F);

	}

	@SubscribeEvent
	public static void onPlayerHurt(LivingDamageEvent event) {
		if(!(event.getEntity() instanceof PlayerEntity) || event.getEntity().world.isRemote) return;
		ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();

	}

	@SubscribeEvent
	public static void onLivingHurtCrit(LivingHurtEvent event) {
		if(!(event.getSource().getTrueSource() instanceof LivingEntity))return;
		LivingEntity source = (LivingEntity)event.getSource().getTrueSource();
		if(source.world.isRemote)return;

		if(source.getAttributeManager().hasAttributeInstance(ModAttributes.CRIT_CHANCE)) {
			double chance = source.getAttributeValue(ModAttributes.CRIT_CHANCE);

			if(source.getAttributeManager().hasAttributeInstance(ModAttributes.CRIT_MULTIPLIER)) {
				double multiplier = source.getAttributeValue(ModAttributes.CRIT_MULTIPLIER);

				if(source.world.rand.nextDouble() < chance) {
					source.world.playSound(null, source.getPosX(), source.getPosY(), source.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, source.getSoundCategory(), 1.0F, 1.0F);
					event.setAmount((float)(event.getAmount() * multiplier));
				}
			}
		}
	}

    @SubscribeEvent
    public static void onLivingHurtTp(LivingHurtEvent event) {
        if (event.getEntityLiving().world.isRemote) return;

        boolean direct = event.getSource().getImmediateSource() == event.getSource().getTrueSource();

        if (direct && event.getEntityLiving().getAttributeManager().hasAttributeInstance(ModAttributes.TP_CHANCE)) {
            double chance = event.getEntityLiving().getAttributeValue(ModAttributes.TP_CHANCE);

            if (event.getEntityLiving().getAttributeManager().hasAttributeInstance(ModAttributes.TP_RANGE)) {
                double range = event.getEntityLiving().getAttributeValue(ModAttributes.TP_RANGE);

                if (event.getEntityLiving().world.rand.nextDouble() < chance) {
                    for (int i = 0; i < 64; ++i) {
                        if (teleportRandomly(event.getEntityLiving(), range)) {
                            event.getEntityLiving().world.playSound(null,
                                    event.getEntityLiving().prevPosX,
                                    event.getEntityLiving().prevPosY,
                                    event.getEntityLiving().prevPosZ,
                                    ModSounds.BOSS_TP_SFX, event.getEntityLiving().getSoundCategory(), 1.0F, 1.0F);
                            event.setCanceled(true);
                            return;
                        }
                    }
                }
            }
        } else if (!direct && event.getEntityLiving().getAttributeManager().hasAttributeInstance(ModAttributes.TP_INDIRECT_CHANCE)) {
            double chance = event.getEntityLiving().getAttributeValue(ModAttributes.TP_INDIRECT_CHANCE);

            if (event.getEntityLiving().getAttributeManager().hasAttributeInstance(ModAttributes.TP_RANGE)) {
                double range = event.getEntityLiving().getAttributeValue(ModAttributes.TP_RANGE);

                if (event.getEntityLiving().world.rand.nextDouble() < chance) {
                    for (int i = 0; i < 64; ++i) {
                        if (teleportRandomly(event.getEntityLiving(), range)) {
                            event.getEntityLiving().world.playSound(null,
                                    event.getEntityLiving().prevPosX,
                                    event.getEntityLiving().prevPosY,
                                    event.getEntityLiving().prevPosZ,
                                    ModSounds.BOSS_TP_SFX, event.getEntityLiving().getSoundCategory(), 1.0F, 1.0F);
                            event.setCanceled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    private static boolean teleportRandomly(LivingEntity entity, double range) {
        if (!entity.world.isRemote() && entity.isAlive()) {
            double d0 = entity.getPosX() + (entity.world.rand.nextDouble() - 0.5D) * (range * 2.0D);
            double d1 = entity.getPosY() + (entity.world.rand.nextInt((int) (range * 2.0D)) - range);
            double d2 = entity.getPosZ() + (entity.world.rand.nextDouble() - 0.5D) * (range * 2.0D);
            return entity.attemptTeleport(d0, d1, d2, true);
        }

        return false;
    }

}
