package vault_research.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import vault_research.Vault;
import vault_research.init.ModConfigs;
import vault_research.research.type.Research;
import vault_research.world.data.PlayerResearchesData;
import vault_research.world.data.PlayerVaultStatsData;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;

public class ItemResearchUnlockOrb extends Item {
	
	public static final String NAMENBT = Vault.MOD_ID + ":researchName";

	public ItemResearchUnlockOrb(ItemGroup group) {
		super(new Properties()
                .group(group)
                .maxStackSize(16));
		
        this.setRegistryName(Vault.id("research_key"));

	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		if (stack.getTag() == null || !stack.getTag().contains(NAMENBT)) return super.getDisplayName(stack);
		
		
		TranslationTextComponent modName = (TranslationTextComponent) super.getDisplayName(stack);
		
		modName.appendString(" (" + stack.getTag().get(NAMENBT).getString() + ")");
		
		
		return modName;
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		
		ItemStack heldItemStack = player.getHeldItem(hand);
		
		CompoundNBT heldNBT = heldItemStack.getTag();
		
		if (heldNBT == null || !heldNBT.contains(NAMENBT)) return ActionResult.resultFail(heldItemStack);
		
		String researchName = heldNBT.get(NAMENBT).getString();
		
		Research research = ModConfigs.RESEARCHES.getByName(researchName);
		
		if (research == null) return ActionResult.resultFail(heldItemStack);
		
		boolean success = false;
				
		if (!world.isRemote) {
            PlayerResearchesData researchData = PlayerResearchesData.get((ServerWorld) world);
            
            if (researchData.getResearches(player).getResearchesDone().contains(researchName)) {
            	success = false;
            } else {
                success = true;
                researchData.research((ServerPlayerEntity) player, research);
            }
            
        }
		
		if (success) {
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(),
	                SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.NEUTRAL,
	                0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

	        player.addStat(Stats.ITEM_USED.get(this));
	        if (!player.abilities.isCreativeMode) {
	            heldItemStack.shrink(1);
	        }

	        return ActionResult.func_233538_a_(heldItemStack, world.isRemote());

		} else {
        	world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(),
                    SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.NEUTRAL,
                    0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        	
        	return ActionResult.resultFail(heldItemStack);

		}
		
		
	}
	

}
