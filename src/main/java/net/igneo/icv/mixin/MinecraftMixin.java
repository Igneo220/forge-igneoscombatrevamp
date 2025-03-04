package net.igneo.icv.mixin;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.igneo.icv.ICV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Minecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "handleKeybinds", at = @At("HEAD"), cancellable = true, remap = false)
    private void handleKeybinds(CallbackInfo ci) {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) Minecraft.getInstance().player).get(new ResourceLocation(ICV.MOD_ID, "enchant_animator"));
        if (animation != null) {
            if (animation.isActive()) ci.cancel();
        }
    }

}
