package ihh.lore.client;

import com.mojang.blaze3d.vertex.PoseStack;
import ihh.lore.Lore;
import ihh.lore.LorePageManager;
import ihh.lore.packet.SetLecternPagePacket;
import ihh.lore.packet.TakeLoreBookFromLecternPacket;
import ihh.lore.item.LoreBookItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class LoreBookViewScreen extends LoreViewScreen {
    private static final ResourceLocation LOCATION = new ResourceLocation(Lore.MOD_ID, "textures/gui/lore_book.png");
    private final List<FormattedText> text = new ArrayList<>();
    private final boolean playTurnSound;
    private final int startPage;
    private final BlockPos lecternPos;
    private int currentPage = -1;
    private int cachedPage = -1;
    private Component pageMsg = TextComponent.EMPTY;
    private PageButton forwardButton;
    private PageButton backButton;

    public LoreBookViewScreen(ItemStack stack, boolean playTurnSound, int startPage, BlockPos lecternPos) {
        this.playTurnSound = playTurnSound;
        this.startPage = startPage;
        this.lecternPos = lecternPos;
        for (LorePageManager.LorePageData data : LoreBookItem.getAllPages(stack)) {
            text.add(data == null ? FormattedText.of(new TranslatableComponent("item.lore.lore_page.invalid").getString(), Style.EMPTY.withColor(ChatFormatting.RED)) : FormattedText.of(new TranslatableComponent("item.lore.lore_page." + data.book() + "." + data.number() + ".text").getString()));
        }
    }

    @Override
    protected void init() {
        if (lecternPos == null) {
            super.init();
        } else {
            addRenderableWidget(new Button(width / 2 - 100, 196, 98, 20, CommonComponents.GUI_DONE, e -> minecraft.setScreen(null)));
            addRenderableWidget(new Button(this.width / 2 + 2, 196, 98, 20, new TranslatableComponent("lectern.take_book"), e -> {
                Lore.NETWORK_HANDLER.sendToServer(new TakeLoreBookFromLecternPacket(lecternPos));
                minecraft.setScreen(null);
            }));
        }
        forwardButton = addRenderableWidget(new PageButton((width - 192) / 2 + 116, 159, true, p -> setPage(currentPage + 1), playTurnSound));
        backButton = addRenderableWidget(new PageButton((width - 192) / 2 + 43, 159, false, p -> setPage(currentPage - 1), playTurnSound));
        setPage(startPage);
    }

    @Override
    public boolean handleComponentClicked(Style pStyle) {
        if (pStyle == null) return false;
        ClickEvent clickevent = pStyle.getClickEvent();
        if (clickevent == null) return false;
        if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            try {
                return setPage(Integer.parseInt(clickevent.getValue()) - 1);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return super.handleComponentClicked(pStyle);
    }

    @Override
    public boolean isPauseScreen() {
        return lecternPos == null;
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) return true;
        if (pKeyCode == GLFW.GLFW_KEY_PAGE_UP) {
            backButton.onPress();
            return true;
        }
        if (pKeyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
            forwardButton.onPress();
            return true;
        }
        return false;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackgroundGuiAndText(pPoseStack, LOCATION, text.get(currentPage), 8);
        if (cachedPage != currentPage) {
            pageMsg = new TranslatableComponent("book.pageIndicator", currentPage + 1, Math.max(text.size(), 1));
            cachedPage = currentPage;
        }
        font.draw(pPoseStack, pageMsg, (float) ((width - 192) / 2 - font.width(pageMsg) + 192 - 44), 18, 0);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    private boolean setPage(int pPageNum) {
        int i = Mth.clamp(pPageNum, 0, text.size() - 1);
        if (i == currentPage) return false;
        currentPage = i;
        cachedPage = -1;
        forwardButton.visible = currentPage < text.size() - 1;
        backButton.visible = currentPage > 0;
        if (lecternPos != null) {
            Lore.NETWORK_HANDLER.sendToServer(new SetLecternPagePacket(lecternPos, currentPage));
        }
        return true;
    }
}
