package vault_research.init;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vault_research.Vault;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class ModKeybinds {

    public static KeyBinding openAbilityTree;

    public static void register(final FMLClientSetupEvent event) {
        openAbilityTree = createKeyBinding("open_ability_tree", KeyEvent.VK_H);

        ClientRegistry.registerKeyBinding(openAbilityTree);
    }

    private static KeyBinding createKeyBinding(String name, int key) {
        return new KeyBinding(
                "key." + Vault.MOD_ID + "." + name,
                key,
                "key.category." + Vault.MOD_ID
        );
    }
}
