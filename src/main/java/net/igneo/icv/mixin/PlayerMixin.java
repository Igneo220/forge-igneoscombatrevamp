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
        float tempf;
        if (EnchantmentHelper.getEnchantments(this.getMainHandItem()).containsKey(ModEnchantments.SKEWERING.get()) && !pTarget.onGround() && !pTarget.isInFluidType() && !pTarget.isPassenger()) {
            tempf = 1.4F;
        } else {
            tempf = f1;
        }
        AtomicReference<Float> tempf2 = new AtomicReference<>((float) 0);
        this.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (enchVar.getAcrobatBonus()) {
                tempf2.set(0.4F);
            }
        });
        tempf += tempf2.get();
        if (tempf > 1.6) {
            tempf = 1.6F;
        }
        return tempf;
    }
}
