package ihh.lore.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ihh.lore.LorePageManager;
import ihh.lore.LoreRegistration;
import ihh.lore.item.LoreBookItem;
import ihh.lore.item.LorePageItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LoreBookRecipe extends CustomRecipe {
    private final List<Ingredient> ingredients;

    public LoreBookRecipe(ResourceLocation pId, List<Ingredient> ingredients) {
        super(pId, CraftingBookCategory.MISC);
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(@NotNull CraftingContainer pContainer, @NotNull Level pLevel) {
        boolean page = false;
        List<Ingredient> list = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);
        loop: for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack stack = pContainer.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof LorePageItem) {
                if (!page) {
                    page = true;
                    continue;
                }
                return false;
            }
            for (int j = 0; j < ingredients.size(); j++) {
                Ingredient ingredient = ingredients.get(j);
                if (ingredient.test(stack) && list.get(j) == Ingredient.EMPTY) {
                    list.set(j, Ingredient.of(stack));
                    continue loop;
                }
            }
            return false;
        }
        return page && list.stream()
                .noneMatch(e -> e == Ingredient.EMPTY);
    }

    @Override
    @NotNull
    public ItemStack assemble(CraftingContainer pContainer, @NotNull RegistryAccess registryAccess) {
        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack stack = pContainer.getItem(i);
            if (stack.getItem() instanceof LorePageItem) {
                LorePageManager.LorePageData data = LorePageItem.getPage(stack);
                ItemStack book = LoreBookItem.makeBook(data.book());
                LoreBookItem.addPage(book, data.number());
                return book;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= ingredients.size() + 1;
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return LoreRegistration.LORE_BOOK_RECIPE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<LoreBookRecipe> {
        @Override
        @NotNull
        public LoreBookRecipe fromJson(@NotNull ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            List<Ingredient> list = new ArrayList<>();
            JsonArray ingredients = pSerializedRecipe.getAsJsonArray("ingredients");
            for (JsonElement element : ingredients) {
                list.add(Ingredient.fromJson(element));
            }
            return new LoreBookRecipe(pRecipeId, list);
        }

        @Override
        public LoreBookRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            List<Ingredient> list = NonNullList.withSize(pBuffer.readVarInt(), Ingredient.EMPTY);
            for (int i = 0; i < list.size(); ++i) {
                list.set(i, Ingredient.fromNetwork(pBuffer));
            }
            return new LoreBookRecipe(pRecipeId, list);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, LoreBookRecipe pRecipe) {
            pBuffer.writeVarInt(pRecipe.ingredients.size());
            for (Ingredient ingredient : pRecipe.ingredients) {
                ingredient.toNetwork(pBuffer);
            }
        }
    }
}
