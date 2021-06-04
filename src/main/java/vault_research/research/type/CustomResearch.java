package vault_research.research.type;

import com.google.gson.annotations.Expose;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import vault_research.research.Restrictions;
import vault_research.research.Restrictions.Type;

import java.util.HashMap;
import java.util.Map;

public class CustomResearch extends Research {

    @Expose protected Map<String, Restrictions> itemRestrictions;
    @Expose protected Map<String, Restrictions> blockRestrictions;
    @Expose protected Map<String, Restrictions> entityRestrictions;
    @Expose protected Map<String, Restrictions> dimensionRestrictions;

    public CustomResearch(String name, int cost) {
        super(name, cost);
        this.itemRestrictions = new HashMap<>();
        this.blockRestrictions = new HashMap<>();
        this.entityRestrictions = new HashMap<>();
        this.dimensionRestrictions = new HashMap<>();
    }

    public Map<String, Restrictions> getItemRestrictions() {
        return itemRestrictions;
    }

    public Map<String, Restrictions> getBlockRestrictions() {
        return blockRestrictions;
    }

    public Map<String, Restrictions> getEntityRestrictions() {
        return entityRestrictions;
    }
    
    public Map<String, Restrictions> getDimensionRestrictions() {
    	return dimensionRestrictions;
    }

    @Override
    public boolean restricts(Item item, Restrictions.Type restrictionType) {
        ResourceLocation registryName = item.getRegistryName();
        if (registryName == null) return false;
        String sid = registryName.getNamespace() + ":" + registryName.getPath();
        Restrictions restrictions = itemRestrictions.get(sid);
        if (restrictions == null) return false;
        return restrictions.restricts(restrictionType);
    }

    @Override
    public boolean restricts(Block block, Restrictions.Type restrictionType) {
        ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) return false;
        String sid = registryName.getNamespace() + ":" + registryName.getPath();
        Restrictions restrictions = blockRestrictions.get(sid);
        if (restrictions == null) return false;
        return restrictions.restricts(restrictionType);
    }

    @Override
    public boolean restricts(EntityType<?> entityType, Restrictions.Type restrictionType) {
        ResourceLocation registryName = entityType.getRegistryName();
        if (registryName == null) return false;
        String sid = registryName.getNamespace() + ":" + registryName.getPath();
        Restrictions restrictions = entityRestrictions.get(sid);
        if (restrictions == null) return false;
        return restrictions.restricts(restrictionType);
    }

	@Override
	public boolean restricts(ResourceLocation dim, Type restrictionType) {
		if (dim == null) return false;
		String sid = dim.getNamespace() + ":" + dim.getPath();
		Restrictions restrictions = dimensionRestrictions.get(sid);
		if (restrictions == null) return false;
		return restrictions.restricts(restrictionType);
	}

}
