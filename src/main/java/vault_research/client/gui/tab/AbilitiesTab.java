package vault_research.client.gui.tab;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;
import vault_research.client.gui.screen.SkillTreeScreen;
import vault_research.init.ModConfigs;

import java.util.LinkedList;
import java.util.List;

public class AbilitiesTab extends SkillTab {

    public AbilitiesTab(SkillTreeScreen parentScreen) {
        super(parentScreen, new StringTextComponent("Abilities Tab"));
    }

    public void refresh() {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean mouseClicked = super.mouseClicked(mouseX, mouseY, button);

        Vector2f midpoint = parentScreen.getContainerBounds().midpoint();
        int containerMouseX = (int) ((mouseX - midpoint.x) / viewportScale - viewportTranslation.x);
        int containerMouseY = (int) ((mouseY - midpoint.y) / viewportScale - viewportTranslation.y);

        return mouseClicked;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();

        Vector2f midpoint = parentScreen.getContainerBounds().midpoint();

        matrixStack.push();
        matrixStack.translate(midpoint.x, midpoint.y, 0);
        matrixStack.scale(viewportScale, viewportScale, 1);
        matrixStack.translate(viewportTranslation.x, viewportTranslation.y, 0);

        int containerMouseX = (int) ((mouseX - midpoint.x) / viewportScale - viewportTranslation.x);
        int containerMouseY = (int) ((mouseY - midpoint.y) / viewportScale - viewportTranslation.y);

        matrixStack.pop();
    }

}