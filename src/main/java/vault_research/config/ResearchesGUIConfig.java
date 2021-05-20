package vault_research.config;

import com.google.gson.annotations.Expose;

import vault_research.client.gui.helper.SkillFrame;
import vault_research.config.entry.SkillStyle;

import java.util.HashMap;

public class ResearchesGUIConfig extends Config {

	@Expose
	private HashMap<String, SkillStyle> styles;

	@Override
	public String getName() {
		return "researches_gui_styles";
	}

	public HashMap<String, SkillStyle> getStyles() {
		return styles;
	}

	@Override
	protected void reset() {
		SkillStyle style;
		this.styles = new HashMap<>();

		style = new SkillStyle(0, 0, "backpacks");
		style.frameType = SkillFrame.RECTANGULAR;
		styles.put("Backpacks!", style);

		style = new SkillStyle(50, 50, "organisation");
		style.frameType = SkillFrame.RECTANGULAR;
		styles.put("Organisation", style);

		style = new SkillStyle(100, 0, "decorator");
		style.frameType = SkillFrame.RECTANGULAR;
		styles.put("Decorator", style);

		style = new SkillStyle(150, 50, "ludu");
		style.frameType = SkillFrame.RECTANGULAR;
		styles.put("Locked Until Decorator Unlocked", style);

		style = new SkillStyle(50 * 4, 0, "cre");
		style.frameType = SkillFrame.STAR;
		styles.put("Custom Research Example", style);

		style = new SkillStyle(50 * 5, 50, "double_locked");
		style.frameType = SkillFrame.RECTANGULAR;
		styles.put("Double Locked", style);

	}

}
