package ihh.lore;

import com.github.minecraftschurlimods.simplenetlib.IPacket;
import ihh.lore.client.ClientHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public record OpenLoreBookGuiInLecternPacket(ItemStack stack, BlockPos pos, int page) implements IPacket {
    public OpenLoreBookGuiInLecternPacket(FriendlyByteBuf buf) {
        this(buf.readItem(), buf.readBlockPos(), buf.readInt());
    }

    @Override
    public void serialize(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeItem(stack);
        friendlyByteBuf.writeBlockPos(pos);
        friendlyByteBuf.writeInt(page);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> ClientHelper.setLoreBookScreen(stack, false, page, pos));
    }
}
