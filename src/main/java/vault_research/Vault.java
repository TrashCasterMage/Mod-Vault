package vault_research;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import vault_research.init.ModCommands;
import vault_research.world.data.PlayerResearchesData;
import vault_research.world.data.PlayerVaultStatsData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Vault.MOD_ID)
public class Vault {

    public static final String MOD_ID = "vault_research";
    public static final Logger LOGGER = LogManager.getLogger();

    public static RegistryKey<World> VAULT_KEY = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, Vault.id("vault"));

    public Vault() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onCommandRegister);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onPlayerLoggedIn);
    }
    
    public void onCommandRegister(RegisterCommandsEvent event) {
        ModCommands.registerCommands(event.getDispatcher(), event.getEnvironment());
    }

    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ServerWorld serverWorld = player.getServerWorld();
        MinecraftServer server = player.getServer();
        //PlayerVaultStatsData.get(serverWorld).getVaultStats(player).sync(server);
        PlayerResearchesData.get(serverWorld).getResearches(player).sync(server);
        PlayerVaultStatsData.get(serverWorld).getVaultStats(player).sync(server);
    }


    public static String sId(String name) {
        return MOD_ID + ":" + name;
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

}