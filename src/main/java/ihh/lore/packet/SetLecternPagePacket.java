package ihh.lore.packet;

import com.github.minecraftschurlimods.simplenetlib.IPacket;
import ihh.lore.Lore;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nonnull;

public record SetLecternPagePacket(BlockPos pos, int page) implements IPacket {
    public static final ResourceLocation ID = new ResourceLocation(Lore.MOD_ID, "set_lectern_page");

    public SetLecternPagePacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt());
    }

    @Override
    @Nonnull
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void serialize(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(pos);
        friendlyByteBuf.writeInt(page);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            BlockEntity blockEntity = context.getSender().level().getBlockEntity(pos);
            if (blockEntity instanceof LecternBlockEntity lectern) {
                lectern.setPage(page);
            }
        });
    }
}
