package ihh.lore;

import ihh.lore.item.LoreBookItem;
import ihh.lore.item.LorePageItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public interface LoreRegistration {
    CreativeModeTab TAB = new CreativeModeTab(Lore.MOD_ID) {
        @Override
        @Nonnull
        public ItemStack makeIcon() {
            return new ItemStack(LORE_BOOK.get());
        }
    };

    DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Lore.MOD_ID);
    RegistryObject<Item> LORE_BOOK = ITEMS.register("lore_book", LoreBookItem::new);
    RegistryObject<Item> LORE_PAGE = ITEMS.register("lore_page", LorePageItem::new);

    DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, Lore.MOD_ID);
    RegistryObject<GlobalLootModifierSerializer<?>> LORE_PAGE_LOOT_MODIFIER_SERIALIZER = LOOT_MODIFIER_SERIALIZERS.register("lore_page", LorePageLootModifier.Serializer::new);
}
