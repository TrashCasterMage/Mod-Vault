package vault_research.research;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class Restrictions {

    @Expose protected Map<Type, Boolean> restricts;

    private Restrictions() {
        this.restricts = new HashMap<>();
    }

    public Restrictions set(Type type, boolean restricts) {
        this.restricts.put(type, restricts);
        return this;
    }

    public boolean restricts(Type type) {
        return this.restricts.getOrDefault(type, false);
    }

    public static Restrictions forMods() {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.USABILITY, false);
        restrictions.restricts.put(Type.CRAFTABILITY, false);
        restrictions.restricts.put(Type.BLOCK_HITTABILITY, false);
        restrictions.restricts.put(Type.ENTITY_HITTABILITY, false);
        restrictions.restricts.put(Type.BLOCK_INTERACTABILITY, false);
        restrictions.restricts.put(Type.ENTITY_INTERACTABILITY, false);
        restrictions.restricts.put(Type.DIMENSION_TRAVEL, false);
        return restrictions;
    }

    public static Restrictions forItems(boolean restricted) {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.USABILITY, restricted);
        restrictions.restricts.put(Type.CRAFTABILITY, restricted);
        restrictions.restricts.put(Type.BLOCK_HITTABILITY, restricted);
        restrictions.restricts.put(Type.ENTITY_HITTABILITY, restricted);
        //restrictions.restricts.put(Type.PICKUP, restricted);
        return restrictions;
    }

    public static Restrictions forBlocks(boolean restricted) {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.BLOCK_HITTABILITY, restricted);
        restrictions.restricts.put(Type.BLOCK_INTERACTABILITY, restricted);
        return restrictions;
    }

    public static Restrictions forEntities(boolean restricted) {
        Restrictions restrictions = new Restrictions();
        restrictions.restricts.put(Type.ENTITY_HITTABILITY, restricted);
        restrictions.restricts.put(Type.ENTITY_INTERACTABILITY, restricted);
        return restrictions;
    }
    
    public static Restrictions forDimensions(boolean restricted) {
    	Restrictions restrictions = new Restrictions();
    	restrictions.restricts.put(Type.DIMENSION_TRAVEL, restricted);
    	return restrictions;
    }

    public enum Type {
        USABILITY, // Right click with an item, or placement with blocks
        CRAFTABILITY, // Crafting an item using Craft-Matrix
        BLOCK_HITTABILITY, // Left click on a block in the world
        ENTITY_HITTABILITY, // Damage an entity
        BLOCK_INTERACTABILITY, // Right click on a block in the world
        ENTITY_INTERACTABILITY, // Right click on an entity in the world
        DIMENSION_TRAVEL, // Travel to a dimension
        PICKUP, // Pickup an item and keep it in your inventory
    }

}
