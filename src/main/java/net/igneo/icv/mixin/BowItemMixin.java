package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
//import net.igneo.icv.enchantment.WardenScreamEnchantment;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Predicate;

import static java.lang.Math.abs;

@Mixin(value = BowItem.class,priority = 999999999)
public class BowItemMixin{

    @Shadow
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
    @Shadow
    public static float getPowerForTime(int pCharge) {
        float f = (float)pCharge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }
    public AbstractArrow customArrow(AbstractArrow arrow) {
        return arrow;
    }

    /**
     * @author Igneo
     * @reason adding bow enchant
     */

    @Overwrite
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player && !EnchantmentHelper.getEnchantments(pStack).containsKey(ModEnchantments.ACCELERATE.get())) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;
            ItemStack itemstack = player.getProjectile(pStack);

            int i = this.getUseDuration(pStack) - pTimeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getPowerForTime(i);
                if (!((double)f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));
                    ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                    AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, itemstack, player);
                    if (EnchantmentHelper.getEnchantments(pEntityLiving.getMainHandItem()).containsKey(ModEnchantments.WHISTLER.get())){
                        abstractarrow.addTag("whistle");
                    }
                    if (EnchantmentHelper.getEnchantments(pEntityLiving.getMainHandItem()).containsKey(ModEnchantments.PHASING.get())){
                        abstractarrow.addTag("phase");
                    }
                    if (!pLevel.isClientSide) {
                        abstractarrow = customArrow(abstractarrow);
                        abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            abstractarrow.setCritArrow(true);
                        }

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, pStack);
                        if (j > 0) {
                            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, pStack);
                        if (k > 0) {
                            abstractarrow.setKnockback(k);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, pStack) > 0) {
                            abstractarrow.setSecondsOnFire(100);
                        }

                        pStack.hurtAndBreak(1, player, (p_289501_) -> {
                            p_289501_.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
                            abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }
                        if (abstractarrow.getTags().contains("whistle")) {
                            if (f == 1) {
                                pLevel.addFreshEntity(abstractarrow);
                            } else {
                                ServerLevel level = (ServerLevel) pLevel;
                                level.playSound(null,player.blockPosition(), ModSounds.BOW_FUMBLE.get(),SoundSource.PLAYERS);
                            }
                        } else {
                            pLevel.addFreshEntity(abstractarrow);
                        }
                    }

                    pLevel.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(Items.BOW));
                }
            }
        } else if (pEntityLiving instanceof Player player && EnchantmentHelper.getEnchantments(pStack).containsKey(ModEnchantments.ACCELERATE.get())) {
            if (!pLevel.isClientSide) {
                boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;
                ItemStack itemstack = player.getProjectile(pStack);
                int i = this.getUseDuration(pStack) - pTimeLeft;
                i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, !itemstack.isEmpty() || flag);
                float f = getPowerForTime(i);
                ServerLevel level = (ServerLevel) pLevel;
                if (f == 1.0F) {
                    Vec3 vec3 = pEntityLiving.getEyePosition();
                    Vec3 vec31 = pEntityLiving.getLookAngle().scale(25);//p_217704_.getEyePosition().subtract(vec3);
                    Vec3 vec32 = vec31.normalize();
                    for(int j = 1; j < Mth.floor(vec31.length()) + 7; ++j) {
                        Vec3 vec33 = pEntityLiving.getEyePosition().add(vec32.scale((double)j));
                        level.sendParticles(ParticleTypes.CRIT, vec33.x, vec33.y, vec33.z, 1, (Math.random()/5) * j, (Math.random()/5) * j, (Math.random()/5) * j, 0.2D);
                        //level.sendParticles(ParticleTypes.CRIT, vec33.x, vec33.y, vec33.z, 1, (Math.random()/5) * j, (Math.random()/5) * j, (Math.random()/5) * j, 0.2D);
                        //level.sendParticles(ParticleTypes.CRIT, vec33.x, vec33.y, vec33.z, 1, (Math.random()/5) * j, (Math.random()/5) * j, (Math.random()/5) * j, 0.2D);
                    }
                    itemstack.setCount(itemstack.getCount() - 1);
                    Thread AccelerateHurtEntities = new Thread(() -> {
                        ArrowItem arrowitem = (ArrowItem) (Items.ARROW);
                        AbstractArrow arrow = arrowitem.createArrow(level, pStack, pEntityLiving);
                        for (Entity entity : level.getAllEntities()) {
                            if (entity.getBoundingBox().intersects(player.getEyePosition(), player.position().add(player.getLookAngle().scale(20))) && entity != player && entity instanceof LivingEntity) {
                                entity.hurt(player.damageSources().arrow(arrow, pEntityLiving), 3);
                                entity.addDeltaMovement(new Vec3(player.getLookAngle().x/2,0.15,player.getLookAngle().z/2));
                            }
                        }
                    });
                    AccelerateHurtEntities.start();
                    level.playSound(null,player.blockPosition(), ModSounds.ACCELERATE.get(),SoundSource.PLAYERS, 4, (float) abs(Math.random() + 0.5));
                } else {
                    level.playSound(null,player.blockPosition(), ModSounds.BOW_FUMBLE.get(),SoundSource.PLAYERS);
                }
            }
        }
    }
}
