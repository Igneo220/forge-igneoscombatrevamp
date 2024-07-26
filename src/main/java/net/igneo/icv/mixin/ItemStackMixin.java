package net.igneo.icv.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemStack.class,priority = 999999999)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean isEnchantable();

    @Shadow public abstract boolean isEnchanted();

    @Inject(method = "enchant", at = @At("HEAD"), cancellable = true)
    private void enchancement$enchantmentLimit(Enchantment pEnchantment, int level, CallbackInfo ci) {
        if (pEnchantment == null || this.isEnchanted()) {
            ci.cancel();
        }
    }
}
