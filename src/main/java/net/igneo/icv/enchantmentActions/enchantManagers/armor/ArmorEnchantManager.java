package net.igneo.icv.enchantmentActions.enchantManagers.armor;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public abstract class ArmorEnchantManager extends EnchantmentManager {
    protected ArmorEnchantManager(EnchantType type, int enchCoolDown, int coolDownDamageBonus, boolean dualUse, Player player) {
        super(type, player);
        this.maxCoolDown = enchCoolDown;
        this.coolDown = enchCoolDown;
        this.coolDownDamageBonus = coolDownDamageBonus;
        this.dualUse = dualUse;
    }
    
    private final boolean dualUse;
    public int coolDownDamageBonus;
    // 80 is roughly 1 second
    public final int maxCoolDown;
    private int coolDown;
    
    public boolean isDualUse() {
        return dualUse;
    }
    
    
    public int getCoolDown() {
        if (this.coolDown < 0) {
            this.coolDown = 0;
        }
        if (this.coolDown > maxCoolDown) {
            this.coolDown = maxCoolDown;
        }
        return coolDown;
    }
    
    public void addCoolDown(int coolDown) {
        if (this.shouldTickCooldown()) {
            this.coolDown = this.coolDown + coolDown;
            if (this.coolDown < 0) {
                this.coolDown = 0;
                onOffCoolDown(player);
            }
            if (this.coolDown > maxCoolDown) {
                this.coolDown = maxCoolDown;
            }
        }
    }
    
    public void resetCoolDown() {
        this.coolDown = maxCoolDown;
    }
    
    public abstract void onOffCoolDown(Player player);
    
    public boolean isOffCoolDown() {
        return coolDown == 0;
    }
    
    public void tickCoolDown(Player player) {
        if (this.coolDown > 0 && shouldTickCooldown()) {
            --this.coolDown;
            if (coolDown == 0) {
                onOffCoolDown(player);
            }
        }
    }
    
    public boolean shouldTickCooldown() {
        return canUse();
    }
    
    public void targetDamaged() {
        if (!isOffCoolDown()) {
            addCoolDown(coolDownDamageBonus);
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        tickCoolDown(player);
        if (isOffCoolDown()) {
            whileOffCoolDown();
        }
    }
    
    public void whileOffCoolDown() {
    }
    
    @Override
    public void use() {
        System.out.println(canUse());
        if (canUse()) {
            if (isOffCoolDown()) {
                activate();
                resetCoolDown();
            } else if (payBloodCost()) {
                activate();
                resetCoolDown();
            }
        } else if (dualUse) {
            dualActivate();
        }
    }
    
    public void dualActivate() {
    
    }
    
    public boolean canUse() {
        return true;
    }
    
    public boolean payBloodCost() {
        if (player.isCreative()) {
            return false;
        }
        float health = player.getMaxHealth() * 0.5F;
        float coolDownPercent = ((float) this.coolDown / maxCoolDown) + 2;
        float cost = health * coolDownPercent;
        if (player.getHealth() > cost) {
            if (player.level() instanceof ServerLevel level) {
                DamageSource damageSource = level.damageSources().magic();
                player.hurt(damageSource, cost);
            }
            return true;
        }
        return false;
    }
    
    public abstract EnchantIndicator getIndicator();
}
