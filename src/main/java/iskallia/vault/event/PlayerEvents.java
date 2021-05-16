package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.FighterSizeMessage;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.Entity;
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

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {

    public static boolean NATURAL_REGEN_OLD_VALUE = false;
    public static boolean MODIFIED_GAMERULE = false;

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target.world.isRemote)return;

        ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.CLIENT) return;

        RegistryKey<World> dimensionKey = event.player.world.getDimensionKey();
        GameRules gameRules = event.player.world.getGameRules();

        if (MODIFIED_GAMERULE && dimensionKey != Vault.VAULT_KEY) {
            gameRules.get(GameRules.NATURAL_REGENERATION).set(NATURAL_REGEN_OLD_VALUE, event.player.getServer());
            MODIFIED_GAMERULE = false;
            return;
        }

        if (dimensionKey != Vault.VAULT_KEY) return;

        if (event.phase == TickEvent.Phase.START) {
            NATURAL_REGEN_OLD_VALUE = gameRules.getBoolean(GameRules.NATURAL_REGENERATION);
            gameRules.get(GameRules.NATURAL_REGENERATION).set(false, event.player.getServer());
            MODIFIED_GAMERULE = true;

        } else if (event.phase == TickEvent.Phase.END) {
            gameRules.get(GameRules.NATURAL_REGENERATION).set(NATURAL_REGEN_OLD_VALUE, event.player.getServer());
            MODIFIED_GAMERULE = false;
        }
    }

}
