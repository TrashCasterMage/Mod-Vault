package iskallia.vault.init;

import iskallia.vault.Vault;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;

import java.awt.*;

public class ModEffects {

    public static void register(RegistryEvent.Register<Effect> event) {
    }

    /* --------------------------------------------- */

    private static <T extends Effect> void register(T effect, RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(effect);
    }

}
