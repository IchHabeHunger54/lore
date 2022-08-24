package ihh.lore;

import ihh.lore.item.LoreBookItem;
import ihh.lore.item.LorePageItem;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEICompat implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(Lore.MOD_ID, "jei_plugin");

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, LorePageManager.instance().values().stream()
                .distinct()
                .sorted()
                .map(LorePageItem::makePage)
                .toList());
        List<ItemStack> books = new ArrayList<>();
        LorePageManager.instance().values().stream()
                .map(LorePageManager.LorePageData::book)
                .distinct()
                .sorted()
                .forEach(book -> {
                    books.add(LoreBookItem.makeBook(book));
                    ItemStack stack = LoreBookItem.makeBook(book);
                    LoreBookItem.addPages(stack, LorePageManager.instance().values().stream()
                            .filter(e -> e.book().equals(book))
                            .map(LorePageManager.LorePageData::number)
                            .distinct()
                            .sorted()
                            .toList());
                    books.add(stack);
                });
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, books);
    }
}
