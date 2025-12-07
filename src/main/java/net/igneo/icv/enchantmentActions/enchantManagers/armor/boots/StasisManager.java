package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.init.ParticleShapes;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.SendStasisShaderS2CPacket;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StasisManager extends ArmorEnchantManager {
    public StasisManager(Player player) {
        super(EnchantType.BOOTS, 1, -10, true, player);
    }
    //lol, lmao even
    public HashMap<Entity, StasisEntityDataManager> entityData = new HashMap<>();
    private final List<Entity> blackList = new ArrayList<>();
    private Vec3 center;

    private int storedTicks = 0;
    
    @Override
    public void activate() {
        System.out.println("activating");
        center = player.position();
        if (player.level() instanceof ServerLevel level) {
            level.playSound(null,player.blockPosition(), ModSounds.STASIS_ACTIVATE.get(), SoundSource.PLAYERS);
            for (ServerPlayer player: level.players()) {
                ModMessages.sendToPlayer(new SendStasisShaderS2CPacket(center,player.getUUID()),player);
            }
        }
        active = true;
    }
    
    @Override
    public void onOffCoolDown(Player player) {
        player.level().playSound(null, BlockPos.containing(player.position()),ModSounds.STASIS_RECHARGE.get(), SoundSource.PLAYERS ,3F, 1.2F);
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new StasisCooldownIndicator(this);
    }
    
    @Override
    public boolean canUse() {
        return !active;
    }
    
    @Override
    public void dualActivate() {
        release();
        resetCoolDown();
        System.out.println("no more active");
    }
    
    @Override
    public boolean isDualUse() {
        return true;
    }
    
    public void release() {
        active = false;
        for (Entity entity : entityData.keySet()) {
            entity.removeTag("stasis");
            entity.setDeltaMovement(entityData.get(entity).getMovement());
        }
        if (player.level() instanceof ClientLevel level) {
            float scale = 1.8F;
            for (Vec3 pos : ParticleShapes.renderRingList(level, center.add(0,-2,0), 4, 1f)) {
                LodestoneParticles.stasisBreakParticles(level, pos,pos.subtract(center).normalize().scale(scale));
            }
            for (Vec3 pos : ParticleShapes.renderRingList(level, center.add(0,-1,0), 6, 1.5f)) {
                LodestoneParticles.stasisBreakParticles(level, pos,pos.subtract(center).normalize().scale(scale));
            }
            for (Vec3 pos : ParticleShapes.renderRingList(level, center, 8, 2f)) {
                LodestoneParticles.stasisBreakParticles(level, pos,pos.subtract(center).normalize().scale(scale));
            }
            for (Vec3 pos : ParticleShapes.renderRingList(level, center.add(0,1,0), 6, 1.5f)) {
                LodestoneParticles.stasisBreakParticles(level, pos,pos.subtract(center).normalize().scale(scale));
            }
            for (Vec3 pos : ParticleShapes.renderRingList(level, center.add(0,2,0), 4, 1f)) {
                LodestoneParticles.stasisBreakParticles(level, pos,pos.subtract(center).normalize().scale(scale));
            }
        }
        entityData = new HashMap<>();
        storedTicks = 0;
        if (player.level() instanceof ServerLevel level) {
            level.playSound(null,player.blockPosition(), ModSounds.STASIS_BREAK.get(), SoundSource.PLAYERS);
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (active) {
            ++activeTicks;
            for (Entity entity : ICVUtils.collectEntitiesBox(player.level(),center,3)) {
                if (!(entity instanceof LivingEntity) && !blackList.contains(entity) && !entityData.containsKey(entity)) {
                    System.out.println("initial movement: " + entity.getDeltaMovement());
                    entityData.put(entity, new StasisEntityDataManager(entity.getDeltaMovement(), entity.position(), entity.getYRot(), entity.getXRot()));
                }
            }
            for (Entity entity : entityData.keySet()) {
                if (entity.isAlive()) {
                    entity.setPos(entityData.get(entity).position);
                    System.out.println("stored movement: " + entityData.get(entity).movement);
                    if (entity.getDeltaMovement().length() > 0.1) {
                        System.out.println("adding movmement");
                        entityData.get(entity).addMovement(entity.getDeltaMovement());
                    }
                    entity.setDeltaMovement(Vec3.ZERO);
                    float yaw = entityData.get(entity).YRot;
                    float pitch = entityData.get(entity).XRot;

                    if (entity.level() instanceof ClientLevel level && entityData.get(entity).storedTick + 10 < activeTicks) {
                        entityData.get(entity).storedTick = activeTicks;
                        for (Vec3 pos : ParticleShapes.renderLineList(level, entity.getEyePosition(), entity.getEyePosition().add(entityData.get(entity).getMovement().scale(1.5)), 8)) {
                            LodestoneParticles.stasisDirectionParticles(level, pos);
                        }
                    }

                    // Apply new look angles
                    entity.setYRot(yaw);
                    entity.setXRot(pitch);
                }
            }
            if (player.level() instanceof ClientLevel level && storedTicks < activeTicks) {
                storedTicks += 160;
                for (Vec3 pos : ParticleShapes.renderRingList(level, center.add(0,-3,0), 2, 1.5f)) {
                    LodestoneParticles.stasisParticles(level, pos);
                }
                for (Vec3 pos : ParticleShapes.renderRingList(level, center.add(0,-1.5,0), 4, 2.5f)) {
                    LodestoneParticles.stasisParticles(level, pos);
                }
                for (Vec3 pos : ParticleShapes.renderRingList(level, center, 6, 3.5f)) {
                    LodestoneParticles.stasisParticles(level, pos);
                }
                for (Vec3 pos : ParticleShapes.renderRingList(level, center.add(0,1.5,0), 4, 2.5f)) {
                    LodestoneParticles.stasisParticles(level, pos);
                }
                for (Vec3 pos : ParticleShapes.renderRingList(level, center.add(0,3,0), 2, 1.5f)) {
                    LodestoneParticles.stasisParticles(level, pos);
                }
            }
        } else {
            activeTicks = 0;
        }
        if (activeTicks > 1600) {
            dualActivate();
        }
    }
    
    public void addMovement(Entity entity, Vec3 movement) {

        entityData.get(entity).addMovement(movement);
    }
    
    class StasisEntityDataManager {
        private Vec3 movement;
        public Vec3 position;
        public float YRot;
        public float XRot;
        public int storedTick = -10;
        
        StasisEntityDataManager(Vec3 movement, Vec3 position, float YRot, float XRot) {
            this.movement = movement;
            this.position = position;
            this.YRot = YRot;
            this.XRot = XRot;
        }
        
        public Vec3 getMovement() {
            return movement;
        }
        
        public void addMovement(Vec3 push) {
            double max = 6;
            double d0 = Math.max(-max, Math.min(movement.x + push.x, max));
            double d1 = Math.max(0, Math.min(movement.y + push.y, 3));
            double d2 = Math.max(-max, Math.min(movement.z + push.z, max));
            double m = new Vec3(d0, d1, d2).length();
            Vec3 direction = this.movement.normalize().add(push.normalize()).normalize();

            this.movement = direction.multiply(m,m,m);
        }
    }
}


