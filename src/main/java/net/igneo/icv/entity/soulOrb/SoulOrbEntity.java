package net.igneo.icv.entity.soulOrb;

import net.igneo.icv.enchantmentActions.enchantManagers.armor.HauntManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.soulSpider.SoulSpiderEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class SoulOrbEntity extends ICVEntity {
    private SoulSpiderEntity child = null;
    int lifeTime = 0;
    public SoulOrbEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public double getGravity() {
        return 0;
    }

    @Override
    public double getAirFriction() {
        return 0.2;
    }

    @Override
    public void tick() {
        super.tick();
        if (lifeTime > 200) {
            this.discard();
        } else {
            ++lifeTime;
        }
        if (child == null) {
            child = ModEntities.SOUL_SPIDER.get().create(this.level());
            child.setOwner(this.getOwner());
            child.setPos(this.position());
            child.setOrb(this);
            level().addFreshEntity(child);
        }
    }

    @Override
    public void remove(RemovalReason pReason) {
        child.discard();
        HauntManager manager = ((HauntManager) ICVUtils.getManagerForType((Player) this.getOwner(), HauntManager.class));
        if (manager != null) manager.setChild(null);
        super.remove(pReason);
    }
}
