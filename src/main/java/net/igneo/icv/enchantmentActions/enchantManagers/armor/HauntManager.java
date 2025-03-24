package net.igneo.icv.enchantmentActions.enchantManagers.armor;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class HauntManager extends ArmorEnchantManager implements EntityTracker {
    private ICVEntity child;
    private boolean nullCheck;
    public HauntManager(Player player) {
        super(EnchantType.CHESTPLATE, 300, -10, true, player);
    }
    @Override
    public void activate() {
        if (player.level() instanceof ServerLevel level) {
            child = ModEntities.SOUL_ORB.get().create(player.level());
            child.setOwner(player);
            child.setPos(player.getEyePosition().subtract(0,1,0));
            child.setDeltaMovement(ICVUtils.getFlatDirection(player.getYRot(),1,0));
            level.addFreshEntity(child);
            syncClientChild((ServerPlayer) player,child,this);
            nullCheck = true;
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
    public boolean canUse() {
        return child == null;
    }

    @Override
    public void dualActivate() {
        child.setPos(player.getEyePosition());
    }

    @Override
    public boolean isDualUse() {
        return true;
    }


    @Override
    public ICVEntity getChild() {
        return child;
    }

    @Override
    public void setChild(ICVEntity entity) {
        child = entity;
    }

    @Override
    public void tick() {
        super.tick();
        if (nullCheck && player instanceof ServerPlayer serverPlayer && child == null) {
            syncClientChild(serverPlayer,null,this);
            nullCheck = false;
        }
    }
}


