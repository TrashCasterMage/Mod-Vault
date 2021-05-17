package vault_research.network.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import vault_research.init.ModConfigs;
import vault_research.skill.PlayerVaultStats;
import vault_research.world.data.PlayerVaultStatsData;

import java.util.function.Supplier;

public class AbilityUpgradeMessage {

    public String abilityName;

    public AbilityUpgradeMessage() { }

    public AbilityUpgradeMessage(String abilityName) {
        this.abilityName = abilityName;
    }

    public static void encode(AbilityUpgradeMessage message, PacketBuffer buffer) {
        buffer.writeString(message.abilityName, 32767);
    }

    public static AbilityUpgradeMessage decode(PacketBuffer buffer) {
        AbilityUpgradeMessage message = new AbilityUpgradeMessage();
        message.abilityName = buffer.readString(32767);
        return message;
    }

    public static void handle(AbilityUpgradeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) return;


            PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld) sender.world);

            PlayerVaultStats stats = statsData.getVaultStats(sender);
        });
        context.setPacketHandled(true);
    }

}
