package vault_research.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import vault_research.Vault;
import vault_research.item.BasicItem;
import vault_research.item.ItemResearchUnlockOrb;
import vault_research.item.ItemSkillOrb;
import vault_research.item.ItemSkillOrbFrame;
import vault_research.item.ItemSkillShard;
import vault_research.research.type.Research;

public class ModItems {
	
	public static final ResourceLocation GROUP_TEXTURE = new ResourceLocation(Vault.MOD_ID, "textures/gui/research-creative-tab.png");

	public static ItemGroup RESEARCH_MOD_GROUP = (new ItemGroup(Vault.MOD_ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(RESEARCH_ORB);
		}
				
		@Override
		public void fill(net.minecraft.util.NonNullList<ItemStack> items) {
			super.fill(items);

			for (Research research : ModConfigs.RESEARCHES.getAll()) {
				CompoundNBT itemTag;
				itemTag = ModItems.UNLOCK_ORB.getDefaultInstance().getTag();
				
				if (itemTag == null) itemTag = new CompoundNBT();
				
				StringNBT nameNBT = StringNBT.valueOf(research.getName());
				
				itemTag.put(ItemResearchUnlockOrb.NAMENBT, nameNBT);
				
				ItemStack unlockOrb = ModItems.UNLOCK_ORB.getDefaultInstance();
				unlockOrb.setTag(itemTag);
				
				items.add(unlockOrb);
			}
		}
		
		@Override
		public boolean hasSearchBar() {
			return true;
		}
		
		@Override
		public int getSearchbarWidth() {
			return 79;
		}
	}).setBackgroundImage(GROUP_TEXTURE);

		
	public static ItemSkillOrb RESEARCH_ORB = new ItemSkillOrb(RESEARCH_MOD_GROUP);
	public static ItemSkillOrbFrame RESEARCH_ORB_FRAME = new ItemSkillOrbFrame(RESEARCH_MOD_GROUP, Vault.id("orb_frame"));
	public static ItemSkillShard RESEARCH_SHARD = new ItemSkillShard(RESEARCH_MOD_GROUP, Vault.id("research_shard"));
	
	public static BasicItem RESEARCH_ESSENCE = new BasicItem(Vault.id("research_essence"), new Item.Properties().group(RESEARCH_MOD_GROUP));

	public static ItemResearchUnlockOrb UNLOCK_ORB = new ItemResearchUnlockOrb(RESEARCH_MOD_GROUP);
	
	
	public static void registerItems(RegistryEvent.Register<Item> event ) {
		IForgeRegistry<Item> registry = event.getRegistry();
						
		registry.register(RESEARCH_ORB);
		registry.register(RESEARCH_ORB_FRAME);
		registry.register(RESEARCH_SHARD);
		registry.register(RESEARCH_ESSENCE);
		registry.register(UNLOCK_ORB);
	}
}
