package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.enchantment.weapon.WeaponEnchantment;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = Player.class,priority = 999999999)
public abstract class PlayerMixin extends LivingEntity{

    private int checkTicks = 0;

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

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    public void tick(Player instance) {

    }

    @Inject(method = "attack", at = @At("HEAD"),cancellable = true)
    private void attackHead(Entity pTarget, CallbackInfo ci) {
        this.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            for (Enchantment firstEnchantment : this.getMainHandItem().getAllEnchantments().keySet()) {
                if (firstEnchantment instanceof WeaponEnchantment enchantment && this.level() instanceof ServerLevel level) {
                    enchantment.currentLevel = level;
                    if (enchVar.getManager(4) != null && enchVar.getManager(4) instanceof WeaponEnchantManager manager) {
                        manager.target = pTarget;
                    }
                    if (enchVar.getManager(5) != null && enchVar.getManager(5) instanceof WeaponEnchantManager manager) {
                        manager.target = pTarget;
                    }
                }
            }
        });
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE"), index = 4)
    private float attack(float f1, Entity pTarget) {
        float tempf = f1;
        if (pTarget instanceof LivingEntity) {
            AtomicReference<Float> tempf1 = new AtomicReference<>((float) 0);
            if (EnchantmentHelper.getEnchantments(this.getMainHandItem()).containsKey(ModEnchantments.SKEWERING.get()) && !pTarget.onGround() && !pTarget.isInFluidType() && !pTarget.isPassenger()) {
                tempf = 1.35F;
            } else if (EnchantmentHelper.getEnchantments(this.getMainHandItem()).containsKey(ModEnchantments.KINETIC.get())) {
                this.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                    float speedDamage = (float) (((Math.abs(enchVar.getKinX()) + Math.abs(enchVar.getKinZ()))));
                    if (speedDamage >= 0.35F) {
                        speedDamage = 0.35F;
                    }
                    tempf1.set(speedDamage/2);
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
            if (tempf > 1.4) {
                tempf = 1.4F;
            }
        }
        return tempf;
    }

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    public void hurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        this.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (System.currentTimeMillis() <= enchVar.getParryTime() + 100) {
                cir.setReturnValue(false);
            }
        });
    }



}
