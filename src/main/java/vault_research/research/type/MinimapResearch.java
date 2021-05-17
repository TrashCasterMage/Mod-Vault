package vault_research.research.type;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import vault_research.research.Restrictions;

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

}
