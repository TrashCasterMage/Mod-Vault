package iskallia.vault.init;

import iskallia.vault.container.*;
import iskallia.vault.research.ResearchTree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.network.IContainerFactory;

import java.util.Optional;
import java.util.UUID;

public class ModContainers {

    public static ContainerType<SkillTreeContainer> SKILL_TREE_CONTAINER;
    public static void register(RegistryEvent.Register<ContainerType<?>> event) {
        SKILL_TREE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            UUID uniqueID = inventory.player.getUniqueID();
            ResearchTree researchTree = new ResearchTree(uniqueID);
            researchTree.deserializeNBT(Optional.ofNullable(buffer.readCompoundTag()).orElse(new CompoundNBT()));
            return new SkillTreeContainer(windowId, researchTree);
        });

        event.getRegistry().registerAll(
                SKILL_TREE_CONTAINER.setRegistryName("ability_tree")
        );


    }

    private static <T extends Container> ContainerType<T> createContainerType(IContainerFactory<T> factory) {
        return new ContainerType<T>(factory);
    }

}