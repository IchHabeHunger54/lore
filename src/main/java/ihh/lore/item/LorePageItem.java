package ihh.lore.item;

import ihh.lore.Lore;
import ihh.lore.LorePageManager;
import ihh.lore.LoreRegistration;
import ihh.lore.client.ClientHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class LorePageItem extends Item {
    public static final String BOOK = "item.lore.lore_page.tooltip.book";
    public static final String NUMBER = "item.lore.lore_page.tooltip.number";
    public static final String INVALID = "item.lore.lore_page.tooltip.invalid";

    public LorePageItem() {
        super(new Properties().stacksTo(1).tab(LoreRegistration.TAB));
    }

    public static LorePageManager.LorePageData getPage(ItemStack stack) {
        return stack.isEmpty() ? null : LorePageManager.fromNbt(stack.getOrCreateTagElement(Lore.MOD_ID), null);
    }

    public static void setPage(ItemStack stack, LorePageManager.LorePageData page) {
        stack.getOrCreateTag().put(Lore.MOD_ID, LorePageManager.toNbt(page, new CompoundTag()));
    }

    public static ItemStack makePage(LorePageManager.LorePageData page) {
        ItemStack stack = new ItemStack(LoreRegistration.LORE_PAGE.get());
        setPage(stack, page);
        return stack;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        LorePageManager.LorePageData data = getPage(pStack);
        if (data == null || Objects.equals(data.book(), "")) {
            pTooltipComponents.add(Component.translatable(INVALID, Style.EMPTY.withColor(ChatFormatting.RED)));
        } else {
            pTooltipComponents.add(Component.translatable(BOOK, Component.translatable(LoreBookItem.NAME + data.book()).getString()));
            pTooltipComponents.add(Component.translatable(NUMBER, data.number(), LorePageManager.instance().getMaxPagesForBook(data.book())));
        }
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab pCategory, @NotNull NonNullList<ItemStack> pItems) {
        if (pCategory != LoreRegistration.TAB) return;
        LorePageManager.instance().values().stream()
                .distinct()
                .sorted()
                .map(LorePageItem::makePage)
                .forEach(pItems::add);
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel.isClientSide()) {
            ClientHelper.setLorePageScreen(stack);
        }
        return InteractionResultHolder.sidedSuccess(stack, true);
    }
}
