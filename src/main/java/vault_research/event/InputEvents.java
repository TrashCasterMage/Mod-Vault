package vault_research.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vault_research.init.ModKeybinds;
import vault_research.init.ModNetwork;
import vault_research.network.message.AbilityKeyMessage;
import vault_research.network.message.OpenSkillTreeMessage;

import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class InputEvents {

    private static boolean isShiftDown;

    public static boolean isShiftDown() {
        return isShiftDown;
    }

    @SubscribeEvent
    public static void onShiftKey(InputEvent.KeyInputEvent event) {
        if (event.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT) {
            if (event.getAction() == GLFW.GLFW_PRESS) {
                isShiftDown = true;
            } else if (event.getAction() == GLFW.GLFW_RELEASE) {
                isShiftDown = false;
            }
        }
    }

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.world == null) return;
        onInput(minecraft, event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMouse(InputEvent.MouseInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.world == null) return;
        onInput(minecraft, event.getButton(), event.getAction());
    }

    private static void onInput(Minecraft minecraft, int key, int action) {
        if (minecraft.currentScreen == null && ModKeybinds.openAbilityTree.isPressed()) {
            ModNetwork.CHANNEL.sendToServer(new OpenSkillTreeMessage());

        }
    }


}
