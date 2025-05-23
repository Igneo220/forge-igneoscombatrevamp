package net.igneo.icv.entity.helmet.glacialImpasse.iceSpikeSpawner;

import net.igneo.icv.entity.ICVEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class IceSpikeSpawnerEntity extends ICVEntity {
    private int lifetime = 0;
    
    public IceSpikeSpawnerEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Override
    public boolean isNoGravity() {
        return true;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.getOwner() == null) {
            return;
        }
        this.setDeltaMovement(this.getOwner().getViewVector(1.0f).scale(0.3f));
        
        if (lifetime < 200) {
            lifetime++;
        } else {
            this.discard();
        }
    }
}
