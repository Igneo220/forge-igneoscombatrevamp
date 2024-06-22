package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {


    /**
     * @author Igneo
     * @reason wont exist anymore
     */
    /*
    @Overwrite
    public static int getLoyalty(ItemStack pStack) {
        return getItemEnchantmentLevel(ModEnchantments.EXTRACT.get(), pStack);
    }
*/

}
