package iskallia.vault.network.message;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

// From Server to Client
// "Hey dude, your usable abilities are those!"
public class AbilityKnownOnesMessage {


    public AbilityKnownOnesMessage() { }

    public static void encode(AbilityKnownOnesMessage message, PacketBuffer buffer) {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT abilities = new ListNBT();
        nbt.put("LearnedAbilities", abilities);
        buffer.writeCompoundTag(nbt);
    }

    public static AbilityKnownOnesMessage decode(PacketBuffer buffer) {
        AbilityKnownOnesMessage message = new AbilityKnownOnesMessage();
        CompoundNBT nbt = buffer.readCompoundTag();
        ListNBT learnedAbilities = nbt.getList("LearnedAbilities", Constants.NBT.TAG_COMPOUND);
        return message;
    }

    public static void handle(AbilityKnownOnesMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.setPacketHandled(true);
    }

}
