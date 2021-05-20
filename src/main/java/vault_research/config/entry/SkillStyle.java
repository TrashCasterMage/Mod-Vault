package vault_research.config.entry;

import com.google.gson.annotations.Expose;

import vault_research.client.gui.helper.SkillFrame;

public class SkillStyle {

    @Expose public int x, y;
    @Expose public SkillFrame frameType;
    @Expose public String texture;

    public SkillStyle() {}

    public SkillStyle(int x, int y, String texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

}