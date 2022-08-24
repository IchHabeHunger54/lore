package ihh.lore;

import com.mojang.serialization.Codec;
import ihh.lore.crafting.AddLorePageRecipe;
import ihh.lore.crafting.LoreBookRecipe;
import ihh.lore.item.LoreBookItem;
import ihh.lore.item.LorePageItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public interface LoreRegistration {
    CreativeModeTab TAB = new CreativeModeTab(Lore.MOD_ID) {
        @Override
        @NotNull
        public ItemStack makeIcon() {
            return new ItemStack(LORE_BOOK.get());
        }
    };

    DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Lore.MOD_ID);
    RegistryObject<Item> LORE_BOOK = ITEMS.register("lore_book", LoreBookItem::new);
    RegistryObject<Item> LORE_PAGE = ITEMS.register("lore_page", LorePageItem::new);

    DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Lore.MOD_ID);
    RegistryObject<RecipeSerializer<?>> LORE_BOOK_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_special_lore_book", LoreBookRecipe.Serializer::new);
    RegistryObject<RecipeSerializer<?>> ADD_LORE_PAGE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_special_add_lore_page", AddLorePageRecipe.Serializer::new);

    DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Lore.MOD_ID);
    RegistryObject<Codec<? extends IGlobalLootModifier>> LORE_PAGE_LOOT_MODIFIER_SERIALIZER = LOOT_MODIFIER_SERIALIZERS.register("lore_page", LorePageLootModifier.CODEC);
}
