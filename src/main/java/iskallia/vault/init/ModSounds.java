package iskallia.vault.init;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;

public class ModSounds {

	public static SoundEvent CONFETTI_SFX;
	public static SoundEvent VAULT_EXP_SFX;
    public static SoundEvent VAULT_LEVEL_UP_SFX;
    public static SoundEvent SKILL_TREE_LEARN_SFX;
    public static SoundEvent SKILL_TREE_UPGRADE_SFX;

    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        CONFETTI_SFX = registerSound(event, "confetti");
        VAULT_EXP_SFX = registerSound(event, "vault_exp");
        VAULT_LEVEL_UP_SFX = registerSound(event, "vault_level_up");
        SKILL_TREE_LEARN_SFX = registerSound(event, "skill_tree_learn");
        SKILL_TREE_UPGRADE_SFX = registerSound(event, "skill_tree_upgrade");
    }

    /* ---------------------------- */

    private static SoundEvent registerSound(RegistryEvent.Register<SoundEvent> event, String soundName) {
        ResourceLocation location = Vault.id(soundName);
        SoundEvent soundEvent = new SoundEvent(location);
        soundEvent.setRegistryName(location);
        event.getRegistry().register(soundEvent);
        return soundEvent;
    }

}
