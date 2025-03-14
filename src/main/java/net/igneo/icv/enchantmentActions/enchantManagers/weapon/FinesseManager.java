package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FinesseManager extends WeaponEnchantManager{
    public FinesseManager(Player player) {
        super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross"));
    }

    @Override
    public void onAttack(Entity entity) {
        Vec3 redirect = player.getDeltaMovement().reverse();
        double scale = 6;
        if (!player.onGround()) {
            scale = 3;
        }
        double Yscale = 1.2;
        double d0 = redirect.x * scale;
        double d1 = redirect.y * Yscale;
        double d2 = redirect.z * scale;

        if (d0 > 2) d0 = 2;
        if (d0 < -2) d0 = -2;

        if (d1 > 1) d1 = 1;
        if (d1 < -1) d1 = -1;

        if (d2 > 2) d2 = 2;
        if (d2 < -2) d2 = -2;

        redirect = new Vec3(d0,d1,d2);
        player.setDeltaMovement(redirect);
    }

    @Override
    public void activate() {
        super.activate();
        double yaw = Math.toRadians(player.getYRot());
        double x = -Math.sin(yaw);
        double y = 0.25;
        double z = Math.cos(yaw);
        double scale = 2;

        Vec3 flatDirection = new Vec3(x*scale, y, z*scale);
        player.setDeltaMovement(flatDirection);
    }

    @Override
    public float getDamageBonus() {
        System.out.println(target);
        return 20;
    }


}
