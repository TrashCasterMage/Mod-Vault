package iskallia.vault.network.message;

import iskallia.vault.util.RenameType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RenameUIMessage {

    public RenameType renameType;
    public CompoundNBT payload;

    public RenameUIMessage() { }

    public static void encode(RenameUIMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.renameType.ordinal());
        buffer.writeCompoundTag(message.payload);
    }

    public static RenameUIMessage decode(PacketBuffer buffer) {
        RenameUIMessage message = new RenameUIMessage();
        message.renameType = RenameType.values()[buffer.readInt()];
        message.payload = buffer.readCompoundTag();
        return message;
    }

    public static void handle(RenameUIMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            CompoundNBT data = message.payload.getCompound("Data");
            ServerPlayerEntity sender = context.getSender();
            if (message.renameType == RenameType.PLAYER_STATUE) {
                BlockPos statuePos = new BlockPos(data.getInt("x"), data.getInt("y"), data.getInt("z"));
                TileEntity te = sender.getEntityWorld().getTileEntity(statuePos);
            } else if (message.renameType == RenameType.TRADER_CORE) {
                sender.inventory.mainInventory.set(sender.inventory.currentItem, ItemStack.read(data));
            } else if(message.renameType == RenameType.CRYO_CHAMBER) {
                BlockPos pos = NBTUtil.readBlockPos(data.getCompound("BlockPos"));
                String name = data.getString("EternalName");
                TileEntity te = sender.getEntityWorld().getTileEntity(pos);
            }
        });
        context.setPacketHandled(true);
    }

    public static RenameUIMessage updateName(RenameType type, CompoundNBT nbt) {
        RenameUIMessage message = new RenameUIMessage();
        message.renameType = type;
        message.payload = nbt;
        return message;
    }

}
