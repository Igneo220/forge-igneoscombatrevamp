package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StoneCallerCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.Input;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.boots.stonePillar.StonePillarEntity;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.init.ParticleShapes;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StoneCallerManager extends ArmorEnchantManager {
    public StoneCallerManager(Player player) {
        super(EnchantType.BOOTS, 300, -10, false, player);
    }
    
    public Vec3 position = Vec3.ZERO;
    
    @Override
    public void activate() {
        HitResult hitResult = player.pick(20, 0f, false);

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            position = entityHitResult.getEntity().position();
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            position = blockHitResult.getLocation();
        }

        for (Vec3 pos : ParticleShapes.renderLineList(player.level(),player.getEyePosition(),position,10)) {
            player.level().addParticle(ParticleTypes.END_ROD,pos.x,pos.y,pos.z,0,0,0);
        }

        player.level().playSound(null, position.x, position.y, position.z,
                ModSounds.STONE_CALLER_ACTIVATE.get(),
                net.minecraft.sounds.SoundSource.PLAYERS, 1f, 1.0f);

        for (int i = 8; i > 0; --i) {
            float rot = Input.getRotation(Input.getInput(i));
            for (Vec3 pos : ParticleShapes.renderRingList(player.level(),position.add(ICVUtils.getFlatDirection(rot, 3, 0)),4,0.2F)) {
                LodestoneParticles.smokeParticles(player.level(),pos, null);
                LodestoneParticles.rockParticles(player.level(),pos, (float) (0.4 + Math.random()/3));
            }

            if (player.level() instanceof ServerLevel level) {
                StonePillarEntity entity = ModEntities.STONE_PILLAR.get().create(level);
                entity.setPos(position.add(ICVUtils.getFlatDirection(rot, 3, 0)));
                level.addFreshEntity(entity);
            }
        }
        active = true;
    }
    
    @Override
    public void onOffCoolDown(Player player) {
        player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                ModSounds.STONE_CALLER_RECHARGE.get(),
                net.minecraft.sounds.SoundSource.PLAYERS, 1f, 1.0f);
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new StoneCallerCooldownIndicator(this);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (active) {
            if (activeTicks >= 1200) {
                activeTicks = 0;
                active = false;
            } else {
                ++activeTicks;
            }
        }
    }
    
    @Override
    public boolean canUse() {
        return !active;
    }
}


