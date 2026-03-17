package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.boots.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.Input;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.leggings.wave.WaveEntity;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.init.ParticleShapes;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class TsunamiManager extends ArmorEnchantManager {
    public TsunamiManager(Player player) {
        super(EnchantType.LEGGINGS, 1, -10, false, player);
    }
    
    @Override
    public void activate() {
        if (player.level() instanceof ServerLevel level) {
            for (int i = 8; i > 0; --i) {
                float rot = Input.getRotation(Input.getInput(i));
                WaveEntity entity = ModEntities.WAVE.get().create(level);
                entity.setPos(player.position().add(ICVUtils.getFlatDirection(rot, 2, 0)));
                entity.setTrajectory(ICVUtils.getFlatDirection(rot, 1, 0));
                entity.setOwnerID(player.getId());
                level.addFreshEntity(entity);
            }
            player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                    ModSounds.SURF_USE.get(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.5f, 1.0f);
        } else {
            for (Vec3 pos : ParticleShapes.renderSphereList(player.level(),player.getEyePosition(),10,10,1F)) {
                LodestoneParticles.waveParticlesBright(player.level(),pos,pos.subtract(player.position()).scale(2));
            }
        }
        resetCoolDown();
    }
    
    @Override
    public void onOffCoolDown(Player player) {
    
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new StasisCooldownIndicator(this);
    }
}


