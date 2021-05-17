package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.research.Restrictions;
import iskallia.vault.research.type.CustomResearch;
import iskallia.vault.research.type.ModResearch;
import iskallia.vault.research.type.Research;

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
        this.CUSTOM_RESEARCHES.add(customResearch);
    }

}
