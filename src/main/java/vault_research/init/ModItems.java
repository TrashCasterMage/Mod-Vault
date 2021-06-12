package vault_research.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import vault_research.Vault;
import vault_research.item.BasicItem;
import vault_research.item.ItemSkillOrb;
import vault_research.item.ItemSkillOrbFrame;
import vault_research.item.ItemSkillShard;

public class ModItems {

	public static ItemGroup RESEARCH_MOD_GROUP = new ItemGroup(Vault.MOD_ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(RESEARCH_ORB);
		}
	};
	
	public static ItemSkillOrb RESEARCH_ORB = new ItemSkillOrb(RESEARCH_MOD_GROUP);
	public static ItemSkillOrbFrame RESEARCH_ORB_FRAME = new ItemSkillOrbFrame(RESEARCH_MOD_GROUP, Vault.id("orb_frame"));
	public static ItemSkillShard RESEARCH_SHARD = new ItemSkillShard(RESEARCH_MOD_GROUP, Vault.id("research_shard"));
	
	public static BasicItem RESEARCH_ESSENCE = new BasicItem(Vault.id("research_essence"), new Item.Properties().group(RESEARCH_MOD_GROUP));

	public static void registerItems(RegistryEvent.Register<Item> event ) {
		IForgeRegistry<Item> registry = event.getRegistry();
				
		registry.register(RESEARCH_ORB);
		registry.register(RESEARCH_ORB_FRAME);
		registry.register(RESEARCH_SHARD);
		registry.register(RESEARCH_ESSENCE);
	}
}
