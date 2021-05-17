package iskallia.vault.init;

import iskallia.vault.client.gui.overlay.*;
import iskallia.vault.client.gui.screen.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModScreens {

    public static void register(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainers.SKILL_TREE_CONTAINER, SkillTreeScreen::new);
    }

    public static void registerOverlays() {
        MinecraftForge.EVENT_BUS.register(VaultBarOverlay.class);
    }

}
