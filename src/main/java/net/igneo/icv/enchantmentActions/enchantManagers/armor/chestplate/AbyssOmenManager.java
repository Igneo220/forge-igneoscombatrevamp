package net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate;

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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class AbyssOmenManager extends ArmorEnchantManager implements EntityTracker {
    private ICVEntity child = null;
    
    public AbyssOmenManager(Player player) {
        super(EnchantType.CHESTPLATE, 300, -10, false, player);
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
        child = ModEntities.ABYSS_STONE.get().create(player.level());
        child.setOwner(player);
        
        HitResult hitResult = player.pick(5, 0f, false);
        
        BlockHitResult blockHitResult = (BlockHitResult) hitResult;
        Vec3 position = blockHitResult.getLocation();
        
        child.setPos(position);
        level.addFreshEntity(child);
        syncClientChild((ServerPlayer) player, child, this);
    }
    
    @Override
    public boolean shouldTickCooldown() {
        return child == null;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (child != null && !child.isAlive()) {
            child = null;
        }
    }
    
    @Override
    public void onRemove() {
        if (this.child != null) {
            this.child.discard();
        }
    }
    
    @Override
    public boolean canUse() {
        return stableCheck() && child == null;
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