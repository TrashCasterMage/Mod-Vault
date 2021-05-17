package vault_research.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import vault_research.Vault;

public class ModRecipes {

	public static class Serializer {
		public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		}

		private static <T extends IRecipe<?>> SpecialRecipeSerializer<T> register(RegistryEvent.Register<IRecipeSerializer<?>> event, String name, SpecialRecipeSerializer<T> serializer) {
			serializer.setRegistryName(Vault.id(name));
			event.getRegistry().register(serializer);
			return serializer;
		}
	}

}
