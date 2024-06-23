package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity{

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public void disableShield(boolean pBecauseOfAxe) {
    }

    /**
     * @author Igneo
     * @reason changing shield breaking
     */
    @Overwrite
    protected void blockUsingShield(LivingEntity pEntity) {
        if (EnchantmentHelper.getEnchantments(pEntity.getMainHandItem()).containsKey(ModEnchantments.BREAKTHROUGH.get())) {
            disableShield(true);
        }
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE"), index = 4)
    private float attack(float f1, Entity pTarget) {
        float tempf = f1;
        if (EnchantmentHelper.getEnchantments(this.getMainHandItem()).containsKey(ModEnchantments.SKEWERING.get()) && !pTarget.onGround() && !pTarget.isInFluidType() && !pTarget.isPassenger()) {
            tempf = 1.4F;
        }
        return tempf;
    }
}
