package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.config.*;

public class ModConfigs {

    public static ResearchConfig RESEARCHES;
    public static ResearchesGUIConfig RESEARCHES_GUI;
    public static SkillDescriptionsConfig SKILL_DESCRIPTIONS;
    public static SkillGatesConfig SKILL_GATES;
    public static VaultLevelsConfig LEVELS_META;
    public static VaultItemsConfig VAULT_ITEMS;
    public static VaultGeneralConfig VAULT_GENERAL;
    public static KeyPressRecipesConfig KEY_PRESS;
    public static OverLevelEnchantConfig OVERLEVEL_ENCHANT;
    public static PlayerExpConfig PLAYER_EXP;

    public static void register() {
        RESEARCHES = (ResearchConfig) new ResearchConfig().readConfig();
        RESEARCHES_GUI = (ResearchesGUIConfig) new ResearchesGUIConfig().readConfig();
        SKILL_DESCRIPTIONS = (SkillDescriptionsConfig) new SkillDescriptionsConfig().readConfig();
        SKILL_GATES = (SkillGatesConfig) new SkillGatesConfig().readConfig();
        LEVELS_META = (VaultLevelsConfig) new VaultLevelsConfig().readConfig();
        VAULT_ITEMS = (VaultItemsConfig) new VaultItemsConfig().readConfig();
        VAULT_GENERAL = (VaultGeneralConfig) new VaultGeneralConfig().readConfig();
        KEY_PRESS = (KeyPressRecipesConfig) new KeyPressRecipesConfig().readConfig();
        OVERLEVEL_ENCHANT = (OverLevelEnchantConfig) new OverLevelEnchantConfig().readConfig();
        PLAYER_EXP = (PlayerExpConfig) new PlayerExpConfig().readConfig();
        Vault.LOGGER.info("Vault Configs are loaded successfully!");
    }

}
