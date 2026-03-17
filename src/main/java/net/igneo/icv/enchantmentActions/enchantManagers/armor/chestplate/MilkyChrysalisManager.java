package net.igneo.icv.enchantmentActions.enchantManagers.armor.chestplate;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.boots.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class MilkyChrysalisManager extends ArmorEnchantManager {
    public MilkyChrysalisManager(Player player) {
        super(EnchantType.CHESTPLATE, 300, -10, true, player);
    }
    
    public Vec3 position;
    private final List<Entity> blackList = new ArrayList<>();
    
    @Override
    public void activate() {
        System.out.println("activating");
        player.setDeltaMovement(ICVUtils.getFlatInputDirection(player.getYRot(), enchVar.input, 1.2F, 1));
        player.startFallFlying();
        active = true;
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
        return !active;
    }
    
    @Override
    public void dualActivate() {
        active = false;
        activeTicks = 0;
        resetCoolDown();
    }
    
    @Override
    public boolean isDualUse() {
        return true;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (active) {
            player.startFallFlying();
            ++activeTicks;
            if (activeTicks > 1000) {
                dualActivate();
            }
        }
    }
}


