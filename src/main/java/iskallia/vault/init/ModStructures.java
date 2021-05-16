package iskallia.vault.init;

import com.mojang.serialization.Codec;
import iskallia.vault.Vault;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModStructures {


    public static void register(RegistryEvent.Register<Structure<?>> event) {
        PoolElements.register(event);
    }

    private static <T extends Structure<?>> T register(IForgeRegistry<Structure<?>> registry, String name, T structure) {
        Structure.field_236365_a_.put(name, structure);
        structure.setRegistryName(Vault.id(name));
        registry.register(structure);
        return structure;
    }

    public static class PoolElements {

        //No event for registering IJigsawDeserializer?
        public static void register(RegistryEvent.Register<Structure<?>> event) {
        }

        static <P extends JigsawPiece> IJigsawDeserializer<P> register(String name, Codec<P> codec) {
            return Registry.register(Registry.STRUCTURE_POOL_ELEMENT, Vault.id(name), () -> codec);
        }
    }

}
