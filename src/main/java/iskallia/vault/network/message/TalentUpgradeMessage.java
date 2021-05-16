package iskallia.vault.network.message;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// From Client to Server
// "Hey dude, I want to upgrade dis talent o' mine. May I?"
public class TalentUpgradeMessage {

    public String talentName;

    public TalentUpgradeMessage() { }

    public TalentUpgradeMessage(String talentName) {
        this.talentName = talentName;
    }

    public static void encode(TalentUpgradeMessage message, PacketBuffer buffer) {
        buffer.writeString(message.talentName, 32767);
    }

    public static TalentUpgradeMessage decode(PacketBuffer buffer) {
        TalentUpgradeMessage message = new TalentUpgradeMessage();
        message.talentName = buffer.readString(32767);
        return message;
    }

    public static void handle(TalentUpgradeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
