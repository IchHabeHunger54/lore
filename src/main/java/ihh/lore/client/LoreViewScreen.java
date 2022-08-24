package ihh.lore.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;

public abstract class LoreViewScreen extends Screen {
    public LoreViewScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    protected void init() {
        addRenderableWidget(new Button(width / 2 - 100, 196, 200, 20, CommonComponents.GUI_DONE, e -> minecraft.setScreen(null)));
    }

    protected void renderBackgroundGuiAndText(PoseStack stack, ResourceLocation location, FormattedText text, int yOffset) {
        renderBackground(stack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, location);
        int x = (width - 192) / 2;
        blit(stack, x, 2, 0, 0, 192, 192);
        font.drawWordWrap(text, x + 36, yOffset + 24, 114, 0);
    }
}
