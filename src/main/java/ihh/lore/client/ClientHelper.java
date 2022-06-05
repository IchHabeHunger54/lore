package ihh.lore.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class ClientHelper {
    public static void setLorePageScreen(ItemStack stack) {
        Minecraft.getInstance().setScreen(new LorePageViewScreen(stack));
    }

    public static void setLoreBookScreen(ItemStack stack, boolean playTurnSound, int startPage, BlockPos lecternPos) {
        Minecraft.getInstance().setScreen(new LoreBookViewScreen(stack, playTurnSound, startPage, lecternPos));
    }
}
