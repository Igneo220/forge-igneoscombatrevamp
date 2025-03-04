package net.igneo.icv.client.animation;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import org.jetbrains.annotations.NotNull;

public class EnchantAnimationPlayer extends KeyframeAnimationPlayer {
    public EnchantAnimationPlayer(KeyframeAnimation emote, int t, boolean mutable) {
        super(emote, t, mutable);
    }

    public EnchantAnimationPlayer(KeyframeAnimation emote) {
        super(emote, 0, false);
    }

    public boolean isWindingDown(float tickDelta) {
        int windDownStart = getData().endTick + ((getData().stopTick - getData().endTick) / 4);
        return ((getTick() + tickDelta) > (windDownStart + 0.5F)); // + 0.5 for smoother transition
    }

    @Override
    public @NotNull FirstPersonMode getFirstPersonMode(float tickDelta) {
        return FirstPersonMode.THIRD_PERSON_MODEL;
    }
}
