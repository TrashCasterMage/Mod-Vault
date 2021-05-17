package vault_research.config;

import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;

public class SkillDescriptionsConfig extends Config {

	@Expose
	private HashMap<String, JsonElement> descriptions;

	@Override
	public String getName() {
		return "skill_descriptions";
	}

	public IFormattableTextComponent getDescriptionFor(String skillName) {
		JsonElement element = descriptions.get(skillName);
		if (element == null) {
			return StringTextComponent.Serializer
					.getComponentFromJsonLenient("[" + "{text:'No description for ', color:'#192022'}," + "{text: '"
							+ skillName + "', color: '#fcf5c5'}," + "{text: ', yet', color: '#192022'}" + "]");
		}
		return StringTextComponent.Serializer.getComponentFromJson(element);
	}

	@Override
    protected void reset() {
        this.descriptions = new HashMap<>();

        this.descriptions.put("Backpacks!", StringTextComponent.Serializer.toJsonTree(
        		new StringTextComponent("Unlocks the Backpacks mod.")));
        
        this.descriptions.put("Locked Until Decorator Unlocked", StringTextComponent.Serializer.toJsonTree(
        		new StringTextComponent("This research was locked for you until you unlocked Decorator.")));
        
        
        // Complex descriptions (with colors) start here
        JsonArray components = new JsonArray();
        JsonObject ob = new JsonObject();
        
        ob.add("text", toJson("Unlocks the "));
        components.add(ob);
        ob = new JsonObject();
        ob.add("text", toJson("Macaw's Windows / Doors / Bridges / Roofs "));
        ob.add("color", toJson("yellow"));
        components.add(ob);
        ob = new JsonObject();
        ob.add("text", toJson("mods."));
        components.add(ob);
        this.descriptions.put("Decorator", components);
        
        components = new JsonArray();
        ob = new JsonObject();
        ob.add("text", toJson("Unlocks various "));
        components.add(ob);
        ob = new JsonObject();
        ob.add("text", toJson("autocrafters"));
        ob.add("color", toJson("blue"));
        ob.add("italic", new JsonPrimitive(true));
        components.add(ob);
        ob = new JsonObject();
        ob.add("text", toJson(" from different mods.\n\nAlso unlocks the "));
        components.add(ob);
        ob = new JsonObject();
        ob.add("text", toJson("diamond sword"));
        ob.add("bold", new JsonPrimitive(true));
        components.add(ob);
        this.descriptions.put("Custom Research Example", components);
        
        
    }
	
	private JsonPrimitive toJson(String text) {
		return new JsonPrimitive(text);
	}

}
