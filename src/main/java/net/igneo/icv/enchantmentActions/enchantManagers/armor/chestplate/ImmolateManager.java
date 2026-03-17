package net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.boots.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ImmolateManager extends ArmorEnchantManager {
    private static final double IMMOLATE_RADIUS = 5.0d;
    private static final String IMMOLATE_TAG = "Immolate_Fire";
    
    private final HashMap<UUID, Entity> markedEntities = new HashMap<>();
    
    public ImmolateManager(Player player) {
        super(EnchantType.CHESTPLATE, 300, -10, false, player);
    }
    
    @Override
    public void onOffCoolDown(Player player) {
        if (player.level() instanceof ServerLevel level) {
            level.sendParticles(
                    ParticleTypes.FLAME,
                    player.getX(), player.getY() + 1.0f, player.getZ(),
                    20, 0.5f, 0.5f, 0.5f, 0.05f
            );
        }
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new StasisCooldownIndicator(this);
    }
    
    @Override
    public void activate() {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        
        level.sendParticles(
                ParticleTypes.EXPLOSION,
                player.getX(), player.getY() + 1.0f, player.getZ(),
                10, 0.5f, 0.5f, 0.5f, 0.1f
        );
        
        level.sendParticles(
                ParticleTypes.FLAME,
                player.getX(), player.getY() + 1.0f, player.getZ(),
                50, 2.0f, 1.0f, 2.0f, 0.8f
        );
        
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0f, 0.8f);
        
        List<Entity> entities = ICVUtils.collectEntitiesBox(player.level(), player.position(), IMMOLATE_RADIUS);
        
        for (Entity entity : entities) {
            if (entity == player) {
                continue;
            }
            
            entity.setSecondsOnFire(10);
            entity.addTag(IMMOLATE_TAG);
            
            markedEntities.put(entity.getUUID(), entity);
            
            level.sendParticles(
                    ParticleTypes.SMALL_FLAME,
                    entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(),
                    10, 0.3f, 0.3f, 0.3f, 0.05f
            );
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        
        Iterator<UUID> iterator = markedEntities.keySet().iterator();
        
        while (iterator.hasNext()) {
            UUID id = iterator.next();
            Entity entity = markedEntities.get(id);
            
            if (entity == null || !entity.isAddedToWorld()) {
                if (entity instanceof ItemEntity) {
                    createKnockbackEffect(level, entity.position());
                }
                iterator.remove();
                continue;
            }
            
            if (!entity.isAlive()) {
                if (entity.getTags().contains(IMMOLATE_TAG)) {
                    createKnockbackEffect(level, entity.position());
                }
                iterator.remove();
                continue;
            }
            
            if (!entity.isOnFire()) {
                entity.removeTag(IMMOLATE_TAG);
                iterator.remove();
                continue;
            }
            
            if (level.getGameTime() % 10 == 0) {
                level.sendParticles(
                        ParticleTypes.FLAME,
                        entity.getX(), entity.getY() + 0.5f, entity.getZ(),
                        3, 0.2f, 0.2f, 0.2f, 0.01f
                );
            }
        }
    }
    
    private void createKnockbackEffect(ServerLevel level, Vec3 position) {
        level.playSound(null, position.x, position.y, position.z,
                SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
        
        level.sendParticles(
                ParticleTypes.EXPLOSION_EMITTER,
                position.x, position.y, position.z,
                1, 0, 0, 0, 0
        );
        
        level.sendParticles(
                ParticleTypes.LARGE_SMOKE,
                position.x, position.y, position.z,
                15, 0.5f, 0.5f, 0.5f, 0.1f
        );
        
        List<Entity> nearbyEntities = ICVUtils.collectEntitiesBox(player.level(), position, IMMOLATE_RADIUS);
        
        for (Entity nearbyEntity : nearbyEntities) {
            nearbyEntity.hurt(level.damageSources().explosion(player, null), 1);
            
            Vec3 knockbackDir = nearbyEntity.position().subtract(position).normalize();
            
            double knockbackStrength = 1.5f;
            
            if (!player.onGround()) {
                knockbackStrength = 0.8f;
            }
            
            nearbyEntity.setDeltaMovement(nearbyEntity.getDeltaMovement().add(
                    knockbackDir.x * knockbackStrength,
                    0.7f,
                    knockbackDir.z * knockbackStrength
            ));
            
            nearbyEntity.setSecondsOnFire(10);
            nearbyEntity.addTag(IMMOLATE_TAG);
            
            nearbyEntity.hasImpulse = true;
        }
    }
    
    @Override
    public boolean canUse() {
        return stableCheck();
    }
}