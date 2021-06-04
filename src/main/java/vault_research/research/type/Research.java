package vault_research.research.type;

import com.google.gson.annotations.Expose;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import vault_research.research.Restrictions;

public abstract class Research {

    @Expose protected String name;
    @Expose protected int cost;
    @Expose protected String gatedBy;

    public Research(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public boolean isGated() {
        return gatedBy != null;
    }

    public String gatedBy() {
        return gatedBy;
    }

    public abstract boolean restricts(Item item, Restrictions.Type restrictionType);

    public abstract boolean restricts(Block block, Restrictions.Type restrictionType);

    public abstract boolean restricts(EntityType<?> entityType, Restrictions.Type restrictionType);
    
    public abstract boolean restricts(ResourceLocation dim, Restrictions.Type restrictionType);

}
