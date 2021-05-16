package iskallia.vault.init;

import iskallia.vault.Vault;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModAttributes {


	public static Attribute CRIT_CHANCE, CRIT_MULTIPLIER;
	public static Attribute TP_CHANCE, TP_INDIRECT_CHANCE, TP_RANGE;
	public static Attribute POTION_RESISTANCE;

	//Attribute Modifiers
	public static void register(RegistryEvent.Register<Attribute> event) {
		CRIT_CHANCE = register(event.getRegistry(), "generic.crit_chance", new RangedAttribute("attribute.name.generic.crit_chance", 0.0D, 0.0D, 1.0D)).setShouldWatch(true);
		CRIT_MULTIPLIER = register(event.getRegistry(), "generic.crit_multiplier", new RangedAttribute("attribute.name.generic.crit_multiplier", 0.0D, 0.0D, 1024.0D)).setShouldWatch(true);
		TP_CHANCE = register(event.getRegistry(), "generic.tp_chance", new RangedAttribute("attribute.name.generic.tp_chance", 0.0D, 0.0D, 1.0D)).setShouldWatch(true);
		TP_INDIRECT_CHANCE = register(event.getRegistry(), "generic.indirect_tp_chance", new RangedAttribute("attribute.name.generic.indirect_tp_chance", 0.0D, 0.0D, 1.0D)).setShouldWatch(true);
		TP_RANGE = register(event.getRegistry(), "generic.tp_range", new RangedAttribute("attribute.name.generic.tp_range", 32.0D, 0.0D, 1024.0D)).setShouldWatch(true);
		POTION_RESISTANCE = register(event.getRegistry(), "generic.potion_resistance", new RangedAttribute("attribute.name.generic.potion_resistance", 0.0D, 0.0D, 1.0D)).setShouldWatch(true);


	}

	/* ------------------------------------------- */

	private static Attribute register(IForgeRegistry<Attribute> registry, String name, Attribute attribute) {
		registry.register(attribute.setRegistryName(Vault.id(name)));
		return attribute;
	}


}
