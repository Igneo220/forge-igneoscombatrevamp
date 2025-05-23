package net.igneo.icv.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin (value = ThrownPotion.class)
public class ThrownPotionMixin extends ThrowableItemProjectile {
    @Unique
    private boolean increaseSpeed = false;
    
    public ThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Override
    public void tick() {
        if (!increaseSpeed) {
            System.out.println("increasing speed!");
            this.setDeltaMovement(this.getDeltaMovement().scale(1.5));
            //this.addDeltaMovement(this.getDeltaMovement());
            increaseSpeed = true;
        }
        super.tick();
    }
    
    @Override
    protected Item getDefaultItem() {
        return null;
    }
}
