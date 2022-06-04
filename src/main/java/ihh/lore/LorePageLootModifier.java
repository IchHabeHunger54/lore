package ihh.lore;

import com.google.gson.JsonObject;
import ihh.lore.item.LorePageItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class LorePageLootModifier extends LootModifier {
    private static final Random RANDOM = new Random();
    private final float chance;

    public LorePageLootModifier(LootItemCondition[] conditions, float chance) {
        super(conditions);
        this.chance = chance;
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> list, LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return list;
        BlockEntity blockEntity = context.getLevel().getBlockEntity(new BlockPos(origin));
        if (blockEntity instanceof BaseContainerBlockEntity && RANDOM.nextFloat() < chance) {
            list.add(LorePageItem.makePage(LorePageManager.instance().getRandomPage()));
        }
        return list;
    }

    public static class Serializer extends GlobalLootModifierSerializer<LorePageLootModifier> {
        @Override
        public LorePageLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
            return new LorePageLootModifier(conditions, GsonHelper.getAsFloat(object, "chance"));
        }

        @Override
        public JsonObject write(LorePageLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
