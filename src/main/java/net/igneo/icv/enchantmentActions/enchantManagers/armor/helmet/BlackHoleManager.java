package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet;

import net.igneo.icv.client.indicators.BlackHoleIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class BlackHoleManager extends ArmorEnchantManager implements EntityTracker {
    public BlackHoleManager(Player player) {
        super(EnchantType.HELMET, 1, -30, true, player);
    }
    
    public ICVEntity child = null;
    
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
    public EnchantIndicator getIndicator() {
        return new BlackHoleIndicator(this);
    }
    
    @Override
    public void onOffCoolDown(Player player) {
    }
    
    @Override
    public boolean shouldTickCooldown() {
        return child == null;
    }
    
    @Override
    public void activate() {
        if (player.level() instanceof ServerLevel) {
            child = ModEntities.BLACK_HOLE.get().create(player.level());
            child.setOwner(player);
            child.setPos(player.getEyePosition());
            child.setDeltaMovement(player.getLookAngle().scale(0.4));
            player.level().addFreshEntity(child);
            syncClientChild((ServerPlayer) player, child, this);
        }
    }
    
    @Override
    public void dualActivate() {
        if (child != null && player.level() instanceof ServerLevel) {
            Vec3 pushVec = new Vec3((player.getX() - child.getX()), ((player.getEyeY() - 0.5) - child.getY()), (player.getZ() - child.getZ()));
            if (child.getDeltaMovement().length() < 0.3) {
                child.addDeltaMovement(pushVec.normalize().scale(0.1));
            } else {
                child.setDeltaMovement(pushVec.normalize().scale(0.3));
            }
        }
    }
    
    @Override
    public void onRemove() {
        if (this.child != null) this.child.discard();
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
