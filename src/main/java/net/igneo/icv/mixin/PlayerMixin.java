package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity{

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public void disableShield(boolean pBecauseOfAxe) {
    }

    @Shadow public abstract void resetAttackStrengthTicker();

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
        if (pTarget instanceof LivingEntity) {
            System.out.println(pTarget);
            AtomicReference<Float> tempf1 = new AtomicReference<>((float) 0);
            if (EnchantmentHelper.getEnchantments(this.getMainHandItem()).containsKey(ModEnchantments.SKEWERING.get()) && !pTarget.onGround() && !pTarget.isInFluidType() && !pTarget.isPassenger()) {
                tempf = 1.2F;
            } else if (EnchantmentHelper.getEnchantments(this.getMainHandItem()).containsKey(ModEnchantments.KINETIC.get())) {
                this.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                    float speedDamage = (float) (((Math.abs(enchVar.getKinX()) + Math.abs(enchVar.getKinZ()))));
                    if (speedDamage >= 20) {
                        speedDamage = 20;
                    }
                    tempf1.set(speedDamage / 3);
                    //enchVar.setKinX(0);
                    //enchVar.setKinZ(0);
                });
            }

            AtomicReference<Float> tempf2 = new AtomicReference<>((float) 0);
            this.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if (enchVar.getAcrobatBonus()) {
                    tempf2.set(0.2F);
                }
            });
            tempf += tempf1.get() + tempf2.get();
            if (tempf > 1.6) {
                tempf = 1.6F;
            }
        }
        return tempf;
    }
}
