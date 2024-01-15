package ihh.lore;

import com.github.minecraftschurlimods.simplenetlib.NetworkHandler;
import ihh.lore.item.LoreBookItem;
import ihh.lore.packet.OpenLoreBookGuiInLecternPacket;
import ihh.lore.packet.SetLecternPagePacket;
import ihh.lore.packet.TakeLoreBookFromLecternPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Lore.MOD_ID)
public class Lore {
    public static final String MOD_ID = "lore";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final NetworkHandler NETWORK_HANDLER = NetworkHandler.create(MOD_ID, "main", 1);

    public Lore() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        LoreRegistration.ITEMS.register(bus);
        LoreRegistration.CREATIVE_MODE_TABS.register(bus);
        LoreRegistration.LOOT_MODIFIER_SERIALIZERS.register(bus);
        LoreRegistration.RECIPE_SERIALIZERS.register(bus);
        LorePageManager.instance();
        NETWORK_HANDLER.register(OpenLoreBookGuiInLecternPacket.ID, OpenLoreBookGuiInLecternPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        NETWORK_HANDLER.register(SetLecternPagePacket.ID, SetLecternPagePacket.class, NetworkDirection.PLAY_TO_SERVER);
        NETWORK_HANDLER.register(TakeLoreBookFromLecternPacket.ID, TakeLoreBookFromLecternPacket.class, NetworkDirection.PLAY_TO_SERVER);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(Lore::buildCreativeModeTabContents);
        }
        MinecraftForge.EVENT_BUS.addListener(Lore::addReloadListener);
        MinecraftForge.EVENT_BUS.addListener(Lore::rightClickBlock);
    }

    private static void buildCreativeModeTabContents(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == LoreRegistration.TAB.getKey()) {
            e.accept(LoreRegistration.LORE_PAGE);
        }
    }

    private static void addReloadListener(AddReloadListenerEvent e) {
        e.addListener(LorePageManager.instance());
    }

    private static void rightClickBlock(PlayerInteractEvent.RightClickBlock e) {
        if (e.getSide() == LogicalSide.CLIENT) return;
        Player player = e.getEntity();
        Level level = e.getLevel();
        BlockPos pos = e.getHitVec().getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (level.getBlockEntity(pos) instanceof LecternBlockEntity lectern && state.getValue(LecternBlock.HAS_BOOK)) {
            ItemStack stack = lectern.getBook();
            if (player.isSecondaryUseActive()) {
                LoreBookItem.takeFromLectern(player, level, pos, state);
            } else if (stack.getItem() instanceof LoreBookItem) {
                lectern.pageCount = LoreBookItem.getAllPages(stack).size();
                NETWORK_HANDLER.sendToPlayer(new OpenLoreBookGuiInLecternPacket(stack, pos, lectern.getPage()), player);
                player.awardStat(Stats.INTERACT_WITH_LECTERN);
                e.setCanceled(true);
            }
        }
    }
}
