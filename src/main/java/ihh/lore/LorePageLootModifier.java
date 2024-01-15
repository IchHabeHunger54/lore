package ihh.lore;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ihh.lore.item.LorePageItem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LorePageLootModifier extends LootModifier {
    public static final Supplier<Codec<LorePageLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(Codec.FLOAT.fieldOf("chance").forGetter(o -> o.chance)).apply(inst, LorePageLootModifier::new)));
    private final float chance;

    public LorePageLootModifier(LootItemCondition[] conditions, float chance) {
        super(conditions);
        this.chance = chance;
    }

    @Override
    @NotNull
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return list;
        BlockEntity blockEntity = context.getLevel().getBlockEntity(new BlockPos((int) origin.x, (int) origin.y, (int) origin.z));
        if (blockEntity instanceof BaseContainerBlockEntity && context.getRandom().nextFloat() < chance) {
            list.add(LorePageItem.makePage(LorePageManager.instance().getRandomPage()));
        }
        return list;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
