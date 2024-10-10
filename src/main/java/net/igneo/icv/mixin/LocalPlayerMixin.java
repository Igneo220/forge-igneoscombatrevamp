package net.igneo.icv.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LocalPlayer.class,priority = 999999999)
public class LocalPlayerMixin extends AbstractClientPlayer {

    @Shadow
    public Input input;

    public LocalPlayerMixin(ClientLevel pClientLevel, GameProfile pGameProfile) {
        super(pClientLevel, pGameProfile);
    }


    /**
     * @author Igneo220
     * @reason Making it so you can sprint while blind
     */
    @Overwrite
    private boolean canStartSprinting() {
        return !this.isSprinting() && this.hasEnoughImpulseToStartSprinting() && this.hasEnoughFoodToStartSprinting() && !this.isUsingItem() && (!this.isPassenger() || this.vehicleCanSprint(this.getVehicle())) && !this.isFallFlying();
    }

    private boolean vehicleCanSprint(Entity pVehicle) {
        return pVehicle.canSprint() && pVehicle.isControlledByLocalInstance();
    }

    private boolean hasEnoughFoodToStartSprinting() {
        return this.isPassenger() || (float)this.getFoodData().getFoodLevel() > 6.0F || this.getAbilities().mayfly;
    }

    public FoodData getFoodData() {
        return this.foodData;
    }

    private boolean hasEnoughImpulseToStartSprinting() {
        double d0 = 0.8D;
        return this.isUnderWater() ? this.input.hasForwardImpulse() : (double)this.input.forwardImpulse >= 0.8D;
    }

    @Inject(method = "aiStep" , at= @At("TAIL"))
    public void aiStep(CallbackInfo ci) {
        if (!this.isSprinting() && (!(this.isInWater() || this.isInFluidType((fluidType, height) -> this.canSwimInFluidType(fluidType))) || (this.isUnderWater() || this.canStartSwimming())) && this.hasEnoughImpulseToStartSprinting() && this.canStartSprinting() && !this.isUsingItem() && Minecraft.getInstance().options.keySprint.isDown()) {
            this.setSprinting(true);
        }
    }
}
