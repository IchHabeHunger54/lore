package ihh.lore;

import com.github.minecraftschurlimods.simplenetlib.IPacket;
import ihh.lore.item.LoreBookItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record TakeLoreBookFromLecternPacket(BlockPos pos) implements IPacket {
    public TakeLoreBookFromLecternPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    @Override
    public void serialize(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(pos);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> LoreBookItem.takeFromLectern(context.getSender(), context.getSender().getLevel(), pos, context.getSender().getLevel().getBlockState(pos)));
    }
}
