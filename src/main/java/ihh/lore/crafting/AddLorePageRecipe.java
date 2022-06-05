package ihh.lore.crafting;

import com.google.gson.JsonObject;
import ihh.lore.LoreRegistration;
import ihh.lore.item.LoreBookItem;
import ihh.lore.item.LorePageItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

public class AddLorePageRecipe extends CustomRecipe {
    public AddLorePageRecipe(ResourceLocation pId) {
        super(pId);
    }

    @Override
    public boolean matches(CraftingContainer pContainer, @NotNull Level pLevel) {
        ItemStack book = ItemStack.EMPTY, page = ItemStack.EMPTY;
        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack stack = pContainer.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof LoreBookItem) {
                if (book == ItemStack.EMPTY && (page == ItemStack.EMPTY || LorePageItem.getPage(page).book().equals(LoreBookItem.getBook(stack)))) {
                    book = stack;
                    continue;
                }
                return false;
            }
            if (stack.getItem() instanceof LorePageItem) {
                if (page == ItemStack.EMPTY && (book == ItemStack.EMPTY || LorePageItem.getPage(stack).book().equals(LoreBookItem.getBook(book)))) {
                    page = stack;
                    continue;
                }
                return false;
            }
            return false;
        }
        return book != ItemStack.EMPTY && page != ItemStack.EMPTY && !LoreBookItem.hasPage(book, LorePageItem.getPage(page).number());
    }

    @Override
    @NotNull
    public ItemStack assemble(CraftingContainer pContainer) {
        ItemStack book = ItemStack.EMPTY, page = ItemStack.EMPTY;
        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack stack = pContainer.getItem(i);
            if (stack.getItem() instanceof LoreBookItem) {
                book = stack;
            } else if (stack.getItem() instanceof LorePageItem) {
                page = stack;
            }
        }
        ItemStack result = book.copy();
        LoreBookItem.addPage(result, LorePageItem.getPage(page).number());
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return LoreRegistration.ADD_LORE_PAGE_RECIPE_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AddLorePageRecipe> {
        @Override
        @NotNull
        public AddLorePageRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
            return new AddLorePageRecipe(pRecipeId);
        }

        @Override
        public AddLorePageRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
            return new AddLorePageRecipe(pRecipeId);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull AddLorePageRecipe pRecipe) {
        }
    }
}
