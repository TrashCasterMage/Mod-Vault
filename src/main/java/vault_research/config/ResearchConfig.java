package vault_research.config;

import com.google.gson.annotations.Expose;

import vault_research.research.Restrictions;
import vault_research.research.type.CustomResearch;
import vault_research.research.type.ModResearch;
import vault_research.research.type.Research;

import java.util.LinkedList;
import java.util.List;

public class ResearchConfig extends Config {

    @Expose public List<ModResearch> MOD_RESEARCHES;
    @Expose public List<CustomResearch> CUSTOM_RESEARCHES;

    @Override
    public String getName() {
        return "researches";
    }

    public List<Research> getAll() {
        List<Research> all = new LinkedList<>();
        all.addAll(MOD_RESEARCHES);
        all.addAll(CUSTOM_RESEARCHES);
        return all;
    }

    public Research getByName(String name) {
        for (Research research : getAll()) {
            if (research.getName().equals(name))
                return research;
        }
        return null;
    }

    @Override
    protected void reset() {
        this.MOD_RESEARCHES = new LinkedList<>();
        this.MOD_RESEARCHES.add(new ModResearch("Backpacks!", 2, "simplybackpacks").withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add(new ModResearch("Organisation", 3, "trashcans", "dankstorage", "pickletweaks").withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add(new ModResearch("Decorator", 1, "decorative_blocks", "camera", "masonry").withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add(new ModResearch("Locked Until Decorator Unlocked", 2, "mcwbridges", "mcwdoors", "mcwroofs", "mcwwindows", "enviromats", "blockcarpentry", "platforms").withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add(new ModResearch("Double Locked", 3, "create", "quark").withRestrictions(false, false, false, false, true));
        
        this.CUSTOM_RESEARCHES = new LinkedList<>();
        CustomResearch customResearch = new CustomResearch("Custom Research Example", 100);
        customResearch.getItemRestrictions().put("refinedstorage:crafter", Restrictions.forItems(true));
        customResearch.getItemRestrictions().put("rftoolsutility:crafter1", Restrictions.forItems(true));
        customResearch.getItemRestrictions().put("appliedenergistics2:molecular_assembler", Restrictions.forItems(true));
        customResearch.getItemRestrictions().put("mekanism:formulaic_assemblicator", Restrictions.forItems(true));
        customResearch.getItemRestrictions().put("minecraft:diamond_sword", Restrictions.forItems(true));
        
        customResearch.getEntityRestrictions().put("minecraft:villager", Restrictions.forEntities(true));
        
        customResearch.getBlockRestrictions().put("minecraft:furnace", Restrictions.forBlocks(true));
        this.CUSTOM_RESEARCHES.add(customResearch);
    }

}
