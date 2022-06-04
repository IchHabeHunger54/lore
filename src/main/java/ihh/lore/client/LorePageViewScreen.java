package ihh.lore.client;

import com.mojang.blaze3d.vertex.PoseStack;
import ihh.lore.Lore;
import ihh.lore.LorePageManager;
import ihh.lore.item.LorePageItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class LorePageViewScreen extends LoreViewScreen {
    private static final ResourceLocation LOCATION = new ResourceLocation(Lore.MOD_ID, "textures/gui/lore_page.png");
    private final FormattedText text;

    public LorePageViewScreen(ItemStack stack) {
        LorePageManager.LorePageData data = LorePageItem.getPage(stack);
        text = data == null ? FormattedText.of(new TranslatableComponent("item.lore.lore_page.invalid").getString(), Style.EMPTY.withColor(ChatFormatting.RED)) : FormattedText.of(new TranslatableComponent("item.lore.lore_page." + data.book() + "." + data.number() + ".text").getString());
    }

    @Override
    public void render(@Nonnull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackgroundGuiAndText(pPoseStack, LOCATION, text, 0);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}
