package ihh.lore.item;

import ihh.lore.Lore;
import ihh.lore.LorePageManager;
import ihh.lore.LoreRegistration;
import ihh.lore.client.ClientHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LoreBookItem extends Item {
    public static final String BOOK = "book";
    public static final String PAGES = "pages";
    public static final String INVALID = "item.lore.lore_book.invalid";
    public static final String NAME = "item.lore.lore_book.name.";
    public static final String TOOLTIP = "item.lore.lore_book.tooltip";

    public LoreBookItem() {
        super(new Properties().stacksTo(1).tab(LoreRegistration.TAB));
    }

    public static void setBook(ItemStack stack, String book) {
        stack.getOrCreateTagElement(Lore.MOD_ID).putString(BOOK, book);
    }

    public static String getBook(ItemStack stack) {
        return stack.isEmpty() ? null : stack.getOrCreateTagElement(Lore.MOD_ID).getString(BOOK);
    }

    public static ItemStack makeBook(String book) {
        ItemStack stack = new ItemStack(LoreRegistration.LORE_BOOK.get());
        setBook(stack, book);
        return stack;
    }

    public static void addPage(ItemStack stack, int page) {
        int[] tag = stack.getOrCreateTagElement(Lore.MOD_ID).getIntArray(PAGES);
        List<Integer> list = intArrayToList(tag);
        list.add(page);
        Collections.sort(list);
        stack.getOrCreateTagElement(Lore.MOD_ID).putIntArray(PAGES, intListToArray(list));
    }

    public static void addPages(ItemStack stack, List<Integer> pages) {
        for (int i : pages) {
            addPage(stack, i);
        }
    }

    public static List<LorePageManager.LorePageData> getAllPages(ItemStack stack) {
        String book = getBook(stack);
        return intArrayToList(stack.getOrCreateTagElement(Lore.MOD_ID).getIntArray(PAGES)).stream()
                .map(e -> LorePageManager.instance().getByBookAndNumber(book, e))
                .distinct()
                .sorted()
                .toList();
    }

    public static boolean hasPage(ItemStack stack, int page) {
        return getAllPages(stack).stream()
                .map(LorePageManager.LorePageData::number)
                .anyMatch(e -> e == page);
    }

    public static boolean placeInLectern(ItemStack stack, Player player, Level level, BlockPos pos) {
        return getBook(stack) != null && level.getBlockEntity(pos) instanceof LecternBlockEntity && LecternBlock.tryPlaceBook(player, level, pos, level.getBlockState(pos), stack);
    }

    public static void takeFromLectern(Player player, Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof LecternBlockEntity lectern && state.getValue(LecternBlock.HAS_BOOK)) {
            ItemStack stack = lectern.getBook();
            lectern.setBook(ItemStack.EMPTY);
            LecternBlock.resetBookState(level, pos, state, false);
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TranslatableComponent(TOOLTIP, getAllPages(pStack).size(), LorePageManager.instance().getMaxPagesForBook(getBook(pStack))));
    }

    @Override
    @NotNull
    public Component getName(@NotNull ItemStack pStack) {
        String book = getBook(pStack);
        return new TranslatableComponent(Objects.equals(book, "") ? INVALID : NAME + book);
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab pCategory, @NotNull NonNullList<ItemStack> pItems) {
        if (pCategory != LoreRegistration.TAB) return;
        LorePageManager.instance().values().stream()
                .map(LorePageManager.LorePageData::book)
                .distinct()
                .sorted()
                .forEach(book -> {
                    pItems.add(makeBook(book));
                    ItemStack stack = makeBook(book);
                    addPages(stack, LorePageManager.instance().values().stream()
                            .filter(e -> e.book().equals(book))
                            .map(LorePageManager.LorePageData::number)
                            .distinct()
                            .sorted()
                            .toList());
                    pItems.add(stack);
                });
    }

    @Override
    @NotNull
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        return placeInLectern(pContext.getItemInHand(), pContext.getPlayer(), level, pContext.getClickedPos()) ? InteractionResult.sidedSuccess(level.isClientSide()) : super.useOn(pContext);
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel.isClientSide()) {
            if (stack.getOrCreateTagElement(Lore.MOD_ID).getIntArray(PAGES).length == 0) return InteractionResultHolder.fail(stack);
            ClientHelper.setLoreBookScreen(stack, true, 0, null);
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, true);
    }

    private static List<Integer> intArrayToList(int[] array) {
        List<Integer> list = new ArrayList<>(array.length);
        for (int i : array) {
            list.add(i);
        }
        return list;
    }

    private static int[] intListToArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
