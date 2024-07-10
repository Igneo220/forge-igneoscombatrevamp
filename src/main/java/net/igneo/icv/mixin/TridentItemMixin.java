package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    @Shadow
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Inject(method = "releaseUsing",at= @At("HEAD"))
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft, CallbackInfo ci) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            if (i >= 10 && EnchantmentHelper.getEnchantments(pStack).containsKey(ModEnchantments.RECOIL.get())) {
                if (pLevel instanceof ServerLevel) {
                    ServerLevel level = (ServerLevel) pLevel;
                    level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1, 0.1F);
                }
                player.addDeltaMovement(player.getLookAngle().reverse());
            } else if (EnchantmentHelper.getEnchantments(pStack).containsKey(ModEnchantments.EXTRACT.get())) {
                if (pLevel instanceof ServerLevel) {
                    ServerLevel level = (ServerLevel) pLevel;
                    level.playSound(null,pEntityLiving.blockPosition(), SoundType.CHAIN.getPlaceSound(), SoundSource.PLAYERS);
                }
            }
        }
    }
}
