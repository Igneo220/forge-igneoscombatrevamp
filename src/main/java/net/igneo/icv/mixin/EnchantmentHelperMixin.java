package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

@Mixin(value = EnchantmentHelper.class,priority = 999999999)
public class EnchantmentHelperMixin {


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
            return 7;
        } else {
            return 0;
        }
    }
}
