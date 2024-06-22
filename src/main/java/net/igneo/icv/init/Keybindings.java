package net.igneo.icv.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.igneo.icv.ICV;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class Keybindings {
    public static final Keybindings INSTANCE = new Keybindings();

    private Keybindings() {}

    private static final String CATEGORY = "key.categories." + ICV.MOD_ID;

    private int pScanCode;
    public final KeyMapping incapacitate = new KeyMapping(
            "key." + ICV.MOD_ID + ".incapacitate",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_V,-1),
            CATEGORY
    );
    public final KeyMapping trainDash = new KeyMapping(
            "key." + ICV.MOD_ID + ".train_dash",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_Z,-1),
            CATEGORY
    );
    public final KeyMapping judgement = new KeyMapping(
            "key." + ICV.MOD_ID + ".judgement",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_Z,-1),
            CATEGORY
    );
    public final KeyMapping siphon = new KeyMapping(
            "key." + ICV.MOD_ID + ".siphon",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_H,-1),
            CATEGORY
    );
    public final KeyMapping flare = new KeyMapping(
            "key." + ICV.MOD_ID + ".flare",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_H,-1),
            CATEGORY
    );
    public final KeyMapping parry = new KeyMapping(
            "key." + ICV.MOD_ID + ".parry",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_R,-1),
            CATEGORY
    );
    public final KeyMapping wardenspine = new KeyMapping(
            "key." + ICV.MOD_ID + ".wardenspine",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_B,-1),
            CATEGORY
    );
    public final KeyMapping flamethrower = new KeyMapping(
            "key." + ICV.MOD_ID + ".flamethrower",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_B,-1),
            CATEGORY
    );
    public final KeyMapping blizzard = new KeyMapping(
            "key." + ICV.MOD_ID + ".blizzard",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_B,-1),
            CATEGORY
    );
    public final KeyMapping wardenscream = new KeyMapping(
            "key." + ICV.MOD_ID + ".wardenscream",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_B,-1),
            CATEGORY
    );
    public final KeyMapping concussion = new KeyMapping(
            "key." + ICV.MOD_ID + ".concussion",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_Z,-1),
            CATEGORY
    );
    public final KeyMapping smite = new KeyMapping(
            "key." + ICV.MOD_ID + ".smite",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_Z,-1),
            CATEGORY
    );
    public final KeyMapping black_hole = new KeyMapping(
            "key." + ICV.MOD_ID + ".black_hole",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_B,-1),
            CATEGORY
    );
}
