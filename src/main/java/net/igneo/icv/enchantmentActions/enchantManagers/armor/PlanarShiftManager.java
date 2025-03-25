package net.igneo.icv.enchantmentActions.enchantManagers.armor;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlanarShiftManager extends ArmorEnchantManager{
    public PlanarShiftManager(Player player) {
        super(EnchantType.CHESTPLATE, 300, -10, true, player);
    }
    public Vec3 position;
    private List<Entity> blackList = new ArrayList<>();
    @Override
    public void activate() {
        System.out.println("activating");
        position = player.position();
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
        resetCoolDown();
        Direction dir = player.getDirection();;
        System.out.println(dir);

        float dist = 1;
        Vec3 shiftRot = switch (dir) {
            case DOWN -> null;
            case UP -> null;
            case NORTH ->new Vec3(0,0,-dist);
            case SOUTH -> new Vec3(0,0,dist);
            case WEST -> new Vec3(-dist,0,0);
            case EAST -> new Vec3(dist,0,0);
        };
        double yaw = Math.atan2(shiftRot.z,shiftRot.x);
        float rot = (float) Math.toDegrees(yaw) - 90;
        System.out.println(shiftRot);
        System.out.println(rot);
        System.out.println(ICVUtils.getFlatInputDirectionND(rot,10,0));
        Vec3 scale = new Vec3(5,5,5);
        for (Entity entity : player.level().getEntities(null,new AABB(position.subtract(scale),position.add(scale)))) {
            entity.setPos(entity.position().add(ICVUtils.getFlatInputDirectionND(rot,10,0)));
        }
        active = false;
        resetCoolDown();
    }

    @Override
    public boolean isDualUse() {
        return true;
    }
}


