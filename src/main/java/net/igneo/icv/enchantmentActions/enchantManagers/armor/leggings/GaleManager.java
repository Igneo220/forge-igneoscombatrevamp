package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.leggings.GaleCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GaleManager extends ArmorEnchantManager {
    public GaleManager(Player player) {
        super(EnchantType.LEGGINGS, 60, -10, false, player);
    }
    
    @Override
    public void activate() {
        System.out.println("activating");
        for (Entity entity : player.level().getEntities(null, player.getBoundingBox().inflate(5))) {
            float scale = 2;
            if (entity == player) {
                scale = 1.5F;
            }
            entity.setDeltaMovement(ICVUtils.getFlatDirection(player.getYRot(), scale, 0.5));
        }
        if (player.level() instanceof ServerLevel level) {
            for (ServerPlayer sPlayer: level.players()) {
                if (sPlayer.distanceTo(player) < 500) {
                    Vec3 lookInverted = player.getLookAngle().reverse().normalize().scale(0.2);
                    level.sendParticles(sPlayer,
                            ModParticles.GALE_RUNE.get(),
                            true,
                            player.getX() - lookInverted.x,
                            player.getY() + 1,
                            player.getZ() - lookInverted.z,
                            1,
                            0,
                            0,
                            0,
                            0);

                }
            }
            level.playSound(null,player.blockPosition(), ModSounds.GALE_USE.get(), SoundSource.PLAYERS);
        }
        resetCoolDown();
    }
    
    @Override
    public void onOffCoolDown(Player player) {
        player.level().playSound(null, BlockPos.containing(player.position()),ModSounds.GALE_RECHARGE.get(), SoundSource.PLAYERS ,1F, 1F);
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new GaleCooldownIndicator(this);
    }
}


