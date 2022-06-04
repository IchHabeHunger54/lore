package ihh.lore;

import com.github.minecraftschurlimods.simplenetlib.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Lore.MOD_ID)
public class Lore {
    public static final String MOD_ID = "lore";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final NetworkHandler NETWORK_HANDLER = NetworkHandler.create(MOD_ID, "main", 1);

    public Lore() {
        LoreRegistration.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LoreRegistration.LOOT_MODIFIER_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LorePageManager.instance();
        MinecraftForge.EVENT_BUS.addListener(Lore::addReloadListener);
    }

    private static void addReloadListener(AddReloadListenerEvent e) {
        e.addListener(LorePageManager.instance());
    }
}
