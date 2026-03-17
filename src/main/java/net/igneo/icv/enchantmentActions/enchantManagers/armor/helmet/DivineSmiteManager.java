package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.boots.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.helmet.divineLightningRod.DivineLightningRodEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class DivineSmiteManager extends ArmorEnchantManager implements EntityTracker {
    private ICVEntity child = null;
    private static final int RANGE = 20;
    
    public DivineSmiteManager(Player player) {
        super(EnchantType.HELMET, 300, -10, false, player);
    }
    
    @Override
    public void onOffCoolDown(Player player) {
    
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
        
        DivineLightningRodEntity lightningRod = ModEntities.DIVINE_LIGHTNING_ROD.get().create(level);
        
        if (lightningRod == null) {
            return;
        }
        
        lightningRod.setOwner(player);
        
        HitResult hitResult = player.pick(RANGE, 0f, false);
        
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return;
        }
        
        BlockHitResult blockHitResult = (BlockHitResult) hitResult;
        Vec3 position = blockHitResult.getLocation();
        
        lightningRod.setPos(position.x, position.y, position.z);
        
        List<EntityType<?>> immuneEntities = new ArrayList<>();
        immuneEntities.add(player.getType());
        
        player.level().addFreshEntity(lightningRod);
        
        child = lightningRod;
        
        if (player instanceof ServerPlayer serverPlayer) {
            syncClientChild(serverPlayer, child, this);
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (child != null && !child.isAlive()) {
            child = null;
        }
    }
    
    @Override
    public boolean canUse() {
        return child == null;
    }
    
    @Override
    public boolean shouldTickCooldown() {
        return child == null;
    }
    
    @Override
    public void onRemove() {
        if (this.child != null) {
            this.child.discard();
        }
    }
    
    @Override
    public ICVEntity getChild() {
        return child;
    }
    
    @Override
    public void setChild(ICVEntity entity) {
        child = entity;
    }
}