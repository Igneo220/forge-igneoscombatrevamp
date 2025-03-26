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
}
