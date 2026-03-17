package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.leggings.TempestCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class TempestManager extends ArmorEnchantManager {

    public int charges = 0;

    public TempestManager(Player player) {
        super(EnchantType.LEGGINGS, 100, -10, false, player);
    }
    
    @Override
    public void activate() {
        --charges;
        if (player.level().isClientSide) {
            Vec3 dir = ICVUtils.getFlatInputDirection(player.getYRot(), enchVar.input, 0.8F, 0.5);
            player.setDeltaMovement(dir);
            for (int i = 15; i > 0; --i) {
                LodestoneParticles.tempestParticles(player.level(),player.position(),dir.add(0,-0.5,0));
            }

        }
        player.level().playSound(null, BlockPos.containing(player.position()), ModSounds.TEMPEST_USE.get(), SoundSource.PLAYERS ,1F, (float) Math.random()/2+0.75F);
    }
    
    @Override
    public void onOffCoolDown(Player player) {
    
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new TempestCooldownIndicator(this);
    }

    @Override
    public void tickCoolDown(Player player) {
        if (getCoolDown() == 0 && charges < 3) {
            ++charges;
            player.level().playSound(null, BlockPos.containing(player.position()), ModSounds.TEMPEST_RECHARGE.get(), SoundSource.PLAYERS ,1F, (float) charges/3);
            if (charges != 3) resetCoolDown();
        }
        super.tickCoolDown(player);
    }

    @Override
    public boolean isOffCoolDown() {
        System.out.println(charges);
        return charges > 0;
    }
}
