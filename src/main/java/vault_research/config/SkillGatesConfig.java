package vault_research.config;

import com.google.gson.annotations.Expose;

import vault_research.init.ModConfigs;
import vault_research.skill.SkillGates;

public class SkillGatesConfig extends Config {

    @Expose private SkillGates SKILL_GATES;

    @Override
    public String getName() {
        return "skill_gates";
    }

    public SkillGates getGates() {
        return SKILL_GATES;
    }

    @Override
    protected void reset() {
        SKILL_GATES = new SkillGates();
        SkillGates.Entry gateEntry;

        // Researches
        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn("Decorator");
        SKILL_GATES.addEntry("Locked Until Decorator Unlocked", gateEntry);
        
        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn("Backpacks!", "Organisation");
        SKILL_GATES.addEntry("Double Locked", gateEntry);

    }

}
