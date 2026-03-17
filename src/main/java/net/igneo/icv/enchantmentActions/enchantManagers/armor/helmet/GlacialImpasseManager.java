package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.boots.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class GlacialImpasseManager extends ArmorEnchantManager implements EntityTracker {
    
    private ICVEntity iceSpikes = null;
    private ICVEntity iceSpikeSpawner = null;
    private int lifetime = 0;
    
    public GlacialImpasseManager(Player player) {
        super(EnchantType.HELMET, 300, -10, false, player);
    }
    
    @Override
    public void activate() {
        lifetime = 0;
        iceSpikeSpawner = ModEntities.ICE_SPIKE_SPAWNER.get().create(player.level());
        if (!(player.level() instanceof ServerLevel)) {
            return;
        }
        iceSpikeSpawner.setOwner(player);
        iceSpikeSpawner.setPos(player.getEyePosition().add(player.getViewVector(1.0f).normalize()));
        player.level().addFreshEntity(iceSpikeSpawner);
        syncClientChild((ServerPlayer) player, iceSpikeSpawner, this);
        
        active = true;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (iceSpikeSpawner == null || !(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        
        double checkRadius = 1.3d;
        boolean tooClose = !serverLevel.getEntities(ModEntities.ICE_SPIKE.get(),
                iceSpikeSpawner.getBoundingBox().inflate(checkRadius),
                spike -> spike != null &&
                        spike.distanceTo(iceSpikeSpawner) < checkRadius).isEmpty();
        
        if (tooClose) {
            return;
        }
        
        iceSpikes = ModEntities.ICE_SPIKE.get().create(player.level());
        if (iceSpikes == null) {
            return;
        }
        
        iceSpikes.setOwner(player);
        iceSpikes.setPos(iceSpikeSpawner.position().add(iceSpikeSpawner.position().normalize().scale(-1.0f)));
        player.level().addFreshEntity(iceSpikes);
        syncClientChild((ServerPlayer) player, iceSpikes, this);
        
        if (lifetime < 200) {
            lifetime++;
        } else {
            iceSpikeSpawner = null;
            iceSpikes = null;
        }
    }
    
    
    @Override
    public void onOffCoolDown(Player player) {
    
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new StasisCooldownIndicator(this);
    }
    
    @Override
    public ICVEntity getChild() {
        return iceSpikes;
    }
    
    @Override
    public void setChild(ICVEntity entity) {
        iceSpikes = entity;
    }
}