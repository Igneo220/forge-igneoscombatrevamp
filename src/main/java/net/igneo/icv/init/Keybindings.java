package net.igneo.icv.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.igneo.icv.ICV;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Keybindings {
    private static final String ICV_CATEGORY = "key.categories.icv.enchantments";

    private int pScanCode;
    public static final KeyMapping incapacitate = new KeyMapping("key.icv.incapacitate", KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, ICV_CATEGORY);
    public static final KeyMapping train_dash = new KeyMapping(
            "key.icv.train_dash",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z,
            ICV_CATEGORY
    );
    public static final KeyMapping judgement = new KeyMapping(
            "key.icv.judgement",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z,
            ICV_CATEGORY
    );
    public static final KeyMapping siphon = new KeyMapping(
            "key.icv.siphon",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H,
            ICV_CATEGORY
    );
    public static final KeyMapping flare = new KeyMapping(
            "key.icv.flare",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H,
            ICV_CATEGORY
    );
    public static final KeyMapping parry = new KeyMapping(
            "key.icv.parry",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R,
            ICV_CATEGORY
    );
    public static final KeyMapping wardenspine = new KeyMapping(
            "key.icv.wardenspine",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            ICV_CATEGORY
    );
    public static final KeyMapping flamethrower = new KeyMapping(
            "key.icv.flamethrower",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            ICV_CATEGORY
    );
    public static final KeyMapping blizzard = new KeyMapping(
            "key.icv.blizzard",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            ICV_CATEGORY
    );
    public static final KeyMapping wardenscream = new KeyMapping(
            "key.icv.wardenscream",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            ICV_CATEGORY
    );
    public static final KeyMapping concussion = new KeyMapping(
            "key.icv.concussion",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z,
            ICV_CATEGORY
    );
    public static final KeyMapping smite = new KeyMapping(
            "key.icv.smite",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z,
            ICV_CATEGORY
    );
    public static final KeyMapping black_hole = new KeyMapping(
            "key.icv.black_hole",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            ICV_CATEGORY
    );
    public static final KeyMapping comet_strike = new KeyMapping(
            "key.icv.comet_strike",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_SHIFT,
            ICV_CATEGORY
    );
    //public static final KeyMapping scroll_up = new KeyMapping(
    //        "key.icv.black_hole",
    //        KeyConflictContext.GUI,
    //        InputConstants.Type.MOUSE, InputEvent.MouseScrollingEvent,
    //        ICV_CATEGORY
    //);
}
