package net.igneo.icv.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LocalPlayer.class,priority = 999999999)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    @Shadow
    public Input input;

    @Shadow protected abstract boolean hasEnoughFoodToStartSprinting();

    public LocalPlayerMixin(ClientLevel pClientLevel, GameProfile pGameProfile) {
        super(pClientLevel, pGameProfile);
    }


    private boolean canStartSprintingmine() {
        return this.hasEnoughImpulseToStartSprinting() && !this.isUsingItem() && (!this.isPassenger() || this.vehicleCanSprint(this.getVehicle())) && !this.isFallFlying();
    }

    private boolean vehicleCanSprint(Entity pVehicle) {
        return pVehicle.canSprint() && pVehicle.isControlledByLocalInstance();
    }

    @Inject(at = @At("TAIL"), method = "hasEnoughFoodToStartSprinting", cancellable = true)
    public void canStartSprinting(CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(!this.isSprinting() && this.hasEnoughImpulseToStartSprinting() && !this.isUsingItem() && (!this.isPassenger() || this.vehicleCanSprint(this.getVehicle())) && !this.isFallFlying());
        cir.cancel();
    }

    private boolean hasEnoughImpulseToStartSprinting() {
        double d0 = 0.8D;
        return this.isUnderWater() ? this.input.hasForwardImpulse() : (double)this.input.forwardImpulse >= 0.8D;
    }

    @Inject(method = "aiStep" , at= @At("TAIL"))
    public void aiStep(CallbackInfo ci) {
        if ((!(this.isInWater() || this.isInFluidType((fluidType, height) -> this.canSwimInFluidType(fluidType))) || (this.isUnderWater() || this.canStartSwimming())) && this.hasEnoughImpulseToStartSprinting() && this.canStartSprintingmine() && !this.isUsingItem() && Minecraft.getInstance().options.keySprint.isDown()) {
            this.setSprinting(true);
        }
    }
}
