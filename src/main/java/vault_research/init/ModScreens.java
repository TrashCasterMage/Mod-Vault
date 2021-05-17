package vault_research.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vault_research.client.gui.overlay.*;
import vault_research.client.gui.screen.*;

public class ModScreens {

    public static void register(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainers.SKILL_TREE_CONTAINER, SkillTreeScreen::new);
    }

    public static void registerOverlays() {
        MinecraftForge.EVENT_BUS.register(VaultBarOverlay.class);
    }

}
