package ihh.lore.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ClientHelper {
    public static void setLorePageScreen(ItemStack stack) {
        Minecraft.getInstance().setScreen(new LorePageViewScreen(stack));
    }

    public static void setLoreBookScreen(ItemStack stack) {
        Minecraft.getInstance().setScreen(new LoreBookViewScreen(stack, true));
    }
}
