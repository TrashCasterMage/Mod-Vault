package iskallia.vault.skill;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.type.Research;

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

        return false;
    }


    public static class Entry {
        @Expose private List<String> dependsOn;
        @Expose private List<String> lockedBy;

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
    }

}
