package vault_research.skill;

import com.google.gson.annotations.Expose;

import vault_research.client.gui.overlay.VaultBarOverlay;
import vault_research.init.ModConfigs;
import vault_research.research.ResearchTree;
import vault_research.research.type.Research;

import java.util.*;

public class SkillGates {

    @Expose private Map<String, Entry> entries;

    public SkillGates() {
        this.entries = new HashMap<>();
    }

    public void addEntry(String skillName, Entry entry) {
        this.entries.put(skillName, entry);
    }


    public List<Research> getDependencyResearches(String researchName) {
        List<Research> researches = new LinkedList<>();
        Entry entry = entries.get(researchName);
        if (entry == null) return researches;
        entry.dependsOn.forEach(dependencyName -> {
            Research dependency = ModConfigs.RESEARCHES.getByName(dependencyName);
            researches.add(dependency);
        });
        return researches;
    }

    public List<Research> getLockedByResearches(String researchName) {
        List<Research> researches = new LinkedList<>();
        Entry entry = entries.get(researchName);
        if (entry == null) return researches;
        entry.lockedBy.forEach(dependencyName -> {
            Research dependency = ModConfigs.RESEARCHES.getByName(dependencyName);
            researches.add(dependency);
        });
        return researches;
    }
    
    public int getLockedByPoints(String researchName) {
    	Entry entry = entries.get(researchName);
    	if (entry == null) return 0;
    	return entry.pointsGate;
    }

    public boolean isLocked(String researchName, ResearchTree researchTree) {
        SkillGates gates = ModConfigs.SKILL_GATES.getGates();

        List<String> researchesDone = researchTree.getResearchesDone();

        for (Research dependencyResearch : gates.getDependencyResearches(researchName)) {
            if (!researchesDone.contains(dependencyResearch.getName()))
                return true;
        }

        for (Research lockedByResearch : gates.getLockedByResearches(researchName)) {
            if (researchesDone.contains(lockedByResearch.getName()))
                return true;
        }
        
        if (VaultBarOverlay.spentSkillPoints < getLockedByPoints(researchName)) {
        	return true;
        }
        
        return false;
    }
    
    public boolean isLocked(PlayerVaultStats stats, String researchName, ResearchTree researchTree) {
    	SkillGates gates = ModConfigs.SKILL_GATES.getGates();

        List<String> researchesDone = researchTree.getResearchesDone();

        for (Research dependencyResearch : gates.getDependencyResearches(researchName)) {
            if (!researchesDone.contains(dependencyResearch.getName()))
                return true;
        }

        for (Research lockedByResearch : gates.getLockedByResearches(researchName)) {
            if (researchesDone.contains(lockedByResearch.getName()))
                return true;
        }
        
        if (stats.getSpentSkillPts() < getLockedByPoints(researchName)) {
        	return true;
        }
        
        return false;
    }


    public static class Entry {
        @Expose private List<String> dependsOn;
        @Expose private List<String> lockedBy;
        @Expose private int pointsGate;

        public Entry() {
            this.dependsOn = new LinkedList<>();
            this.lockedBy = new LinkedList<>();
        }

        public void setDependsOn(String... skills) {
            dependsOn.addAll(Arrays.asList(skills));
        }

        public void setLockedBy(String... skills) {
            lockedBy.addAll(Arrays.asList(skills));
        }
        
        public void setPointsGate(int points) {
        	this.pointsGate = points;
        }
    }

}
