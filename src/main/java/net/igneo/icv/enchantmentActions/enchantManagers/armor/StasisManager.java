package net.igneo.icv.enchantmentActions.enchantManagers.armor;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StasisManager extends ArmorEnchantManager{
    public StasisManager(Player player) {
        super(EnchantType.BOOTS, 300, -10, true, player);
    }
    public HashMap<Entity,StasisEntityDataManager> entityData = new HashMap<>();
    private List<Entity> blackList = new ArrayList<>();
    @Override
    public void activate() {
        System.out.println("activating");
        for (Entity entity : player.level().getEntities(null,player.getBoundingBox().inflate(20))) {
            if (!(entity instanceof LivingEntity) && !blackList.contains(entity)) {
                entityData.put(entity,new StasisEntityDataManager(Vec3.ZERO,entity.position(),entity.getLookAngle()));
                entity.addTag("stasis");
            }
        }
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
        release();
        resetCoolDown();
        System.out.println("no more active");
    }

    @Override
    public boolean isDualUse() {
        return true;
    }

    public void release() {
        active = false;
        for (Entity entity : entityData.keySet()) {
            entity.removeTag("stasis");
            entity.setDeltaMovement(entityData.get(entity).getMovement());
        }
        entityData = new HashMap<>();
    }

    @Override
    public void tick() {
        super.tick();
        if (active) {
            ++activeTicks;
            for (Entity entity : entityData.keySet()) {
                entity.setPos(entityData.get(entity).position);
                entityData.get(entity).addMovement(entity.getDeltaMovement());
                entity.setDeltaMovement(Vec3.ZERO);
                float yaw = (float) (Mth.atan2(-entityData.get(entity).look.x, entityData.get(entity).look.z) * (180.0F / Math.PI));
                float pitch = (float) (Mth.atan2(-entityData.get(entity).look.y, entityData.get(entity).look.horizontalDistance()) * (180.0F / Math.PI));

                // Apply new look angles
                entity.setYRot(yaw);
                entity.setXRot(pitch);
            }
        } else {
            activeTicks = 0;
        }
        if (activeTicks > 1200) {
            dualActivate();
        }
    }

    public void addMovement(Entity entity, Vec3 movement) {
        entityData.get(entity).addMovement(movement);
    }

    class StasisEntityDataManager {
        private Vec3 movement;
        public Vec3 position;
        public Vec3 look;
        StasisEntityDataManager(Vec3 movement, Vec3 position, Vec3 look) {
            this.movement = new Vec3(movement.x,0, movement.z);
            addMovement(Vec3.ZERO);
            this.position = position;
            this.look = look;
        }
        public Vec3 getMovement() {
            return movement;
        }
        public void addMovement(Vec3 push) {
            double max = 6;
            double d0 = Math.max(-max, Math.min(movement.x + push.x, max));
            double d1 = Math.max(0, Math.min(movement.y + push.y, 3));
            double d2 = Math.max(-max, Math.min(movement.z + push.z, max));

            this.movement = new Vec3(d0,d1,d2);
        }
    }
}


