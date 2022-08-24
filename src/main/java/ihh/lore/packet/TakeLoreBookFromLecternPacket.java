package ihh.lore.packet;

import com.github.minecraftschurlimods.simplenetlib.IPacket;
import ihh.lore.Lore;
import ihh.lore.item.LoreBookItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nonnull;

public record TakeLoreBookFromLecternPacket(BlockPos pos) implements IPacket {
    public static final ResourceLocation ID = new ResourceLocation(Lore.MOD_ID, "take_lore_book_from_lectern");

    public TakeLoreBookFromLecternPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    @Override
    @Nonnull
    public ResourceLocation id() {
        return ID;
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
