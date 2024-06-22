package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Player.class)
public class PlayerMixin{
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
}
