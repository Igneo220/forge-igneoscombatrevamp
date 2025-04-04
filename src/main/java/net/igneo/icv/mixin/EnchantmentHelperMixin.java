package net.igneo.icv.mixin;

import net.igneo.icv.config.ICVCommonConfigs;
import net.igneo.icv.enchantment.ModEnchantments;
import net.minecraft.nbt.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

@Mixin(value = EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    /**
     * @author Igneo220
     * @reason Enchantment no longer exists, replacing with trims
     */
    @Overwrite
    public static int getKnockbackBonus(LivingEntity pPlayer) {
        int trimCount = 0;
        if (pPlayer instanceof Player && ICVCommonConfigs.TRIM_EFFECTS.get()) {
            Player player = (Player) pPlayer;
            for (int j = 0; j < 4; ++j) {
                if (!player.getInventory().getArmor(j).toString().contains("air") && player.getInventory().getArmor(j).serializeNBT().toString().contains("tag")) {
                    if (player.getInventory().getArmor(j).getTag().getAllKeys().contains("Trim")) {
                        Tag tag = player.getInventory().getArmor(j).getTag().get("Trim");
                        if (tag.toString().contains("sentry")) {
                            ++trimCount;
                        }
                    }
                }
            }
        }
        return trimCount;
    }
    /**
     * @author Igneo220
     * @reason Enchantment no longer exists, replacing with trims
     */
    @Overwrite
    public static int getDamageProtection(Iterable<ItemStack> pStacks, DamageSource pSource) {
        int protInt = 0;
        if (ICVCommonConfigs.TRIM_EFFECTS.get()) {
            if (pSource.is(DamageTypes.IN_FIRE) || pSource.is(DamageTypes.ON_FIRE) || pSource.is(DamageTypes.LAVA)) {
                for (ItemStack pStack : pStacks) {
                    if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                        if (pStack.getTag().getAllKeys().contains("Trim")) {
                            Tag tag = pStack.getTag().get("Trim");
                            if (tag.toString().contains("rib")) {
                                protInt += 3;
                            }
                        }
                    }
                }
            }
            if (pSource.is(DamageTypes.MAGIC) || pSource.is(DamageTypes.INDIRECT_MAGIC) || pSource.is(DamageTypes.DRAGON_BREATH)
                    || pSource.is(DamageTypes.SONIC_BOOM) || pSource.is(DamageTypes.LIGHTNING_BOLT) || pSource.is(DamageTypes.WITHER)) {
                for (ItemStack pStack : pStacks) {
                    if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                        if (pStack.getTag().getAllKeys().contains("Trim")) {
                            Tag tag = pStack.getTag().get("Trim");
                            if (tag.toString().contains("eye")) {
                                protInt += 3;
                            }
                        }
                    }
                }
            }
            if (pSource.is(DamageTypes.FALL)) {
                for (ItemStack pStack : pStacks) {
                    if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                        if (pStack.getTag().getAllKeys().contains("Trim")) {
                            Tag tag = pStack.getTag().get("Trim");
                            if (tag.toString().contains("spire")) {
                                protInt += 3;
                            }
                        }
                    }
                }
            }
            if (pSource.is(DamageTypes.EXPLOSION) || pSource.is(DamageTypes.PLAYER_EXPLOSION) || pSource.is(DamageTypes.BAD_RESPAWN_POINT)) {
                for (ItemStack pStack : pStacks) {
                    if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                        if (pStack.getTag().getAllKeys().contains("Trim")) {
                            Tag tag = pStack.getTag().get("Trim");
                            if (tag.toString().contains("ward")) {
                                protInt += 3;
                            }
                        }
                    }
                }
            }
            if (pSource.is(DamageTypes.ARROW) || pSource.is(DamageTypes.MOB_PROJECTILE) || pSource.is(DamageTypes.UNATTRIBUTED_FIREBALL)) {
                for (ItemStack pStack : pStacks) {
                    if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                        if (pStack.getTag().getAllKeys().contains("Trim")) {
                            Tag tag = pStack.getTag().get("Trim");
                            if (tag.toString().contains("ward")) {
                                protInt += 3;
                            }
                        }
                    }
                }
            }
            for (ItemStack pStack : pStacks) {
                if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                    if (pStack.getTag().getAllKeys().contains("Trim")) {
                        Tag tag = pStack.getTag().get("Trim");
                        if (tag.toString().contains("sentry")) {
                            protInt -= 3;
                        }
                    }
                }
            }
            for (ItemStack pStack : pStacks) {
                if (!pStack.toString().contains("air") && pStack.serializeNBT().toString().contains("tag")) {
                    if (pStack.getTag().getAllKeys().contains("Trim")) {
                        Tag tag = pStack.getTag().get("Trim");
                        if (tag.toString().contains("host")) {
                            protInt += 3;
                        }
                    }
                }
            }
        }
        return protInt;
    }

    /**
     * @author Igneo220
     * @reason Enchantment no longer exists, replacing with trims
     */
    @Overwrite
    public static int getDepthStrider(LivingEntity pEntity) {
        int trimCount = 0;
        if (pEntity instanceof Player && ICVCommonConfigs.TRIM_EFFECTS.get()) {
            Player player = (Player) pEntity;
            for (int j = 0; j < 4; ++j) {
                if (!player.getInventory().getArmor(j).toString().contains("air") && player.getInventory().getArmor(j).serializeNBT().toString().contains("tag")) {
                    if (player.getInventory().getArmor(j).getTag().getAllKeys().contains("Trim")) {
                        Tag tag = player.getInventory().getArmor(j).getTag().get("Trim");
                        if (tag.toString().contains("tide")) {
                            ++trimCount;
                        }
                    }
                }
            }
        }
        return trimCount;
    }
    /**
     * @author Igneo220
     * @reason Enchantment no longer exists, replacing with trims
     */
    @Overwrite
    public static int getRespiration(LivingEntity pEntity) {
        int trimCount = 0;
        if (pEntity instanceof Player && ICVCommonConfigs.TRIM_EFFECTS.get()) {
            Player player = (Player) pEntity;
            for (int j = 0; j < 4; ++j) {
                if (!player.getInventory().getArmor(j).toString().contains("air") && player.getInventory().getArmor(j).serializeNBT().toString().contains("tag")) {
                    if (player.getInventory().getArmor(j).getTag().getAllKeys().contains("Trim")) {
                        Tag tag = player.getInventory().getArmor(j).getTag().get("Trim");
                        if (tag.toString().contains("coast")) {
                            ++trimCount;
                        }
                    }
                }
            }
        }
        return trimCount;
    }

    /**
     * @author Igneo
     * @reason wont exist anymore
     */
    @Overwrite
    public static int getLoyalty(ItemStack pStack) {
        return 3;
    }

    /**
     * @author Igneo220
     * @reason removing efficiency adding brute touch
     */
    @Overwrite
    public static int getBlockEfficiency(LivingEntity pEntity) {
        if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BRUTE_TOUCH.get(),pEntity) == 1) {
            return 5;
        } else {
            return 0;
        }
    }
}
