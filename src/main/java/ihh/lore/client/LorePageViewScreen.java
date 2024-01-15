package ihh.lore.client;

import ihh.lore.Lore;
import ihh.lore.LorePageManager;
import ihh.lore.item.LorePageItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LorePageViewScreen extends LoreViewScreen {
    private static final ResourceLocation LOCATION = new ResourceLocation(Lore.MOD_ID, "textures/gui/lore_page.png");
    private final FormattedText text;

    public LorePageViewScreen(ItemStack stack) {
        LorePageManager.LorePageData data = LorePageItem.getPage(stack);
        text = data == null ? FormattedText.of(Component.translatable("item.lore.lore_page.invalid").getString(), Style.EMPTY.withColor(ChatFormatting.RED)) : FormattedText.of(Component.translatable("item.lore.lore_page." + data.book() + "." + data.number() + ".text").getString());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackgroundGuiAndText(graphics, LOCATION, text, 0);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }
}
