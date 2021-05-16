package iskallia.vault.network.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// From Client to Server
// "Hey dude, I pressed/unpressed the ability key, could you handle it pls?"
public class AbilityKeyMessage {
    // XXX: Turn into OpcodeMessage<OPC> extension someday

    public boolean keyUp;
    public boolean keyDown;
    public boolean scrollUp;
    public boolean scrollDown;
    public boolean shouldCancelDown;
    public int abilityIndex = -1;

    public AbilityKeyMessage() { }

    public AbilityKeyMessage(boolean keyUp, boolean keyDown, boolean scrollUp, boolean scrollDown) {
        this.keyUp = keyUp;
        this.keyDown = keyDown;
        this.scrollUp = scrollUp;
        this.scrollDown = scrollDown;
    }

    public AbilityKeyMessage(boolean shouldCancelDown) {
        this.shouldCancelDown = shouldCancelDown;
    }

    public AbilityKeyMessage(int selectAbilityIndex) {
        this.abilityIndex = selectAbilityIndex;
    }

    public static void encode(AbilityKeyMessage message, PacketBuffer buffer) {
        buffer.writeBoolean(message.keyUp);
        buffer.writeBoolean(message.keyDown);
        buffer.writeBoolean(message.scrollUp);
        buffer.writeBoolean(message.scrollDown);
        buffer.writeBoolean(message.shouldCancelDown);
        buffer.writeInt(message.abilityIndex);
    }

    public static AbilityKeyMessage decode(PacketBuffer buffer) {
        AbilityKeyMessage message = new AbilityKeyMessage();
        message.keyUp = buffer.readBoolean();
        message.keyDown = buffer.readBoolean();
        message.scrollUp = buffer.readBoolean();
        message.scrollDown = buffer.readBoolean();
        message.shouldCancelDown = buffer.readBoolean();
        message.abilityIndex = buffer.readInt();
        return message;
    }

    public static void handle(AbilityKeyMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) return;

        });
        context.setPacketHandled(true);
    }

}
