package ihh.lore.client;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;

public abstract class LoreViewScreen extends Screen {
    public LoreViewScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, e -> minecraft.setScreen(null)).pos(width / 2 - 100, 196).size(200, 20).build());
    }

    protected void renderBackgroundGuiAndText(GuiGraphics graphics, ResourceLocation location, FormattedText text, int yOffset) {
        renderBackground(graphics);
        int x = (width - 192) / 2;
        graphics.blit(location, x, 2, 0, 0, 192, 192);
        graphics.drawString(font, text.getString(), x + 36, yOffset + 24, 114);
    }
}
