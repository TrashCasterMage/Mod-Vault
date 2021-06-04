package vault_research.research.type;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import vault_research.research.Restrictions;
import vault_research.research.Restrictions.Type;

public class MinimapResearch extends Research {

    public MinimapResearch(String name, int cost) {
        super(name, cost);

        // TODO: Implement this bad boi
    }

    @Override
    public boolean restricts(Item item, Restrictions.Type restrictionType) {
        return false;
    }

    @Override
    public boolean restricts(Block block, Restrictions.Type restrictionType) {
        return false;
    }

    @Override
    public boolean restricts(EntityType<?> entityType, Restrictions.Type restrictionType) {
        return false;
    }

	@Override
	public boolean restricts(ResourceLocation dim, Type restrictionType) {
		// TODO Auto-generated method stub
		return false;
	}

}
