package ihh.lore.packet;

import com.github.minecraftschurlimods.simplenetlib.IPacket;
import ihh.lore.Lore;
import ihh.lore.client.ClientHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nonnull;

public record OpenLoreBookGuiInLecternPacket(ItemStack stack, BlockPos pos, int page) implements IPacket {
    public static final ResourceLocation ID = new ResourceLocation(Lore.MOD_ID, "open_lore_book_gui_in_lectern");

    public OpenLoreBookGuiInLecternPacket(FriendlyByteBuf buf) {
        this(buf.readItem(), buf.readBlockPos(), buf.readInt());
    }

    @Override
    @Nonnull
    public ResourceLocation id() {
        return ID;
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
