package vault_research.config;

import vault_research.Vault;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

@EventBusSubscriber(modid = Vault.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MiscConfig {
	
	
	public static class Common {
		public final ForgeConfigSpec.BooleanValue teamsEnabled;
		public final ForgeConfigSpec.BooleanValue xpGain;
		public final ForgeConfigSpec.IntValue playerStartingPoints;
		public final ForgeConfigSpec.BooleanValue noResearchFarming;
		
		public Common(ForgeConfigSpec.Builder builder) {
			
			//GENERAL
			teamsEnabled = builder
					.comment("Should players be able to invite other players to their team? (Allows sharing of research)",
							"Default: true")
					.define("general.teamsEnabled", true);
			
			xpGain = builder
					.comment("Should players gain Vault XP by picking up XP orbs?",
							"Default: true")
					.define("general.xpGain", true);
			
			playerStartingPoints = builder
					.comment("How many Research Points should a player receive when they join a server/load a world for the first time?",
							"Default: 0")
					.defineInRange("general.playerStartingPoints", 0, 0, Integer.MAX_VALUE);
			
			noResearchFarming = builder
					.comment("Enabling this slows (and eventually stops) Vault XP gain via mob farms.", 
							"Default: true")
					.define("general.noResearchFarming", true);
			
		}
	}
	
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}
	
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
		if (event.getConfig().getSpec() == MiscConfig.COMMON_SPEC) {
			bakeCommonConfig();
		}
	}
	
	public static boolean teamsEnabled, xpGain, noResearchFarming;
	public static int playerStartingPoints;
	
	public static void bakeCommonConfig() {
		teamsEnabled = COMMON.teamsEnabled.get();
		xpGain = COMMON.teamsEnabled.get();
		noResearchFarming = COMMON.noResearchFarming.get();
		playerStartingPoints = COMMON.playerStartingPoints.get();
	}

}
