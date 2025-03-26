package net.igneo.icv.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.igneo.icv.ICV;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Keybindings {
    private static final String ICV_CATEGORY = "key.categories.icv.enchantments";

    public static final KeyMapping helmet = new KeyMapping(
            "key.icv.helmet",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y,
            ICV_CATEGORY
    );
    public static final KeyMapping chestplate = new KeyMapping(
            "key.icv.chestplate",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U,
            ICV_CATEGORY
    );
    public static final KeyMapping leggings = new KeyMapping(
            "key.icv.leggings",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I,
            ICV_CATEGORY
    );
    public static final KeyMapping boots = new KeyMapping(
            "key.icv.boots",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O,
            ICV_CATEGORY
    );
}
