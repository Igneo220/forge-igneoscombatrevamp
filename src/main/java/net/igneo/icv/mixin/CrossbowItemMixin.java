package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    /**
     * @author Igneo221
     * @reason enchant behaviors
     */
    @Overwrite
    private static void shootProjectile(Level pLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pCrossbowStack, ItemStack pAmmoStack, float pSoundPitch, boolean pIsCreativeMode, float pVelocity, float pInaccuracy, float pProjectileAngle) {
        if (!pLevel.isClientSide) {
            boolean flag = pAmmoStack.is(Items.FIREWORK_ROCKET);
            Projectile projectile;
            Projectile scatterProjectile = null;
            if (flag) {
                projectile = new FireworkRocketEntity(pLevel, pAmmoStack, pShooter, pShooter.getX(), pShooter.getEyeY() - (double)0.15F, pShooter.getZ(), true);
            } else {
                projectile = getArrow(pLevel, pShooter, pCrossbowStack, pAmmoStack);
                if (pIsCreativeMode || pProjectileAngle != 0.0F) {
                    ((AbstractArrow)projectile).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
            }

            if (pShooter instanceof CrossbowAttackMob) {
                CrossbowAttackMob crossbowattackmob = (CrossbowAttackMob)pShooter;
                crossbowattackmob.shootCrossbowProjectile(crossbowattackmob.getTarget(), pCrossbowStack, projectile, pProjectileAngle);
            } else {
                Vec3 vec31 = pShooter.getUpVector(1.0F);
                Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(pProjectileAngle * ((float)Math.PI / 180F)), vec31.x, vec31.y, vec31.z);
                Vec3 vec3 = pShooter.getViewVector(1.0F);
                Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
                System.out.println(EnchantmentHelper.getEnchantments(pCrossbowStack));
                if (EnchantmentHelper.getEnchantments(pCrossbowStack).containsKey(ModEnchantments.SCATTER.get())) {
                    ServerLevel level = (ServerLevel) pLevel;
                    level.playSound(null,pShooter.blockPosition(),ModSounds.SCATTER.get(), SoundSource.PLAYERS);
                    System.out.println("shootin!");
                    scatterProjectile = createArrow(pLevel,pShooter,pCrossbowStack,pAmmoStack);
                    System.out.println(projectile);
                    System.out.println(scatterProjectile);
                    projectile.shoot((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), pVelocity/4, (float) (Math.random() * 50));
                    scatterProjectile.shoot((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), pVelocity/4, (float) (Math.random() * 25));
                } else if (EnchantmentHelper.getEnchantments(pCrossbowStack).containsKey(ModEnchantments.MITOSIS.get())){
                    if (pLevel instanceof ServerLevel) {
                        ServerLevel level = (ServerLevel) pLevel;
                        level.playSound(null,pShooter.blockPosition(), ModSounds.MITOSIS.get(),SoundSource.PLAYERS);
                    }
                    System.out.println("mitosisin'");
                    projectile = createArrow(pLevel,pShooter,pCrossbowStack,pAmmoStack);
                    projectile.shoot((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), pVelocity, pInaccuracy);
                    projectile.addTag("mitosis");
                } else {
                    if (EnchantmentHelper.getEnchantments(pCrossbowStack).containsKey(ModEnchantments.REND.get())){
                        projectile.addTag("rend");
                    }
                    projectile.shoot((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), pVelocity, pInaccuracy);
                }
            }

            pCrossbowStack.hurtAndBreak(flag ? 3 : 1, pShooter, (p_40858_) -> {
                p_40858_.broadcastBreakEvent(pHand);
            });
            if (scatterProjectile != null) {
                int i = 10;
                while (i > 0) {
                    Projectile tempProjectile;
                    tempProjectile = createArrow(pLevel,pShooter,pCrossbowStack,pAmmoStack);
                    tempProjectile.shoot(scatterProjectile.getDeltaMovement().x,scatterProjectile.getDeltaMovement().y,scatterProjectile.getDeltaMovement().z, pVelocity/4, (float) (Math.random() * 25));
                    tempProjectile.addTag("scatter");
                    pLevel.addFreshEntity(tempProjectile);
                    --i;
                }
            } else {
                pLevel.addFreshEntity(projectile);
            }
            pLevel.playSound((Player)null, pShooter.getX(), pShooter.getY(), pShooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, pSoundPitch);
        }
    }

    private static AbstractArrow getArrow(Level pLevel, LivingEntity pLivingEntity, ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        ArrowItem arrowitem = (ArrowItem)(pAmmoStack.getItem() instanceof ArrowItem ? pAmmoStack.getItem() : Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, pAmmoStack, pLivingEntity);
        if (pLivingEntity instanceof Player) {
            abstractarrow.setCritArrow(true);
        }


        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractarrow.setShotFromCrossbow(true);
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, pCrossbowStack);
        if (i > 0) {
            abstractarrow.setPierceLevel((byte)i);
        }

        return abstractarrow;
    }
    @Unique
    private static AbstractArrow createArrow(Level pLevel, LivingEntity pLivingEntity, ItemStack pCrossbowStack,ItemStack pAmmoStack) {
        ArrowItem arrowitem = (ArrowItem)(Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(pLevel,ItemStack.EMPTY, pLivingEntity);

        if (pLivingEntity instanceof Player) {
            abstractarrow.setCritArrow(true);
        }

        abstractarrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        abstractarrow.setBaseDamage(1.5);

        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractarrow.setShotFromCrossbow(true);

        return abstractarrow;
    }
}
