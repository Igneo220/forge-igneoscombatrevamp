package net.igneo.icv.init;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ICVUtils {

    public static Vec3 getFlatInputDirection(float rot,float scale,double yVelocity) {
        int rotation = 0;

        if (Minecraft.getInstance().options.keyLeft.isDown()) {
            if (Minecraft.getInstance().options.keyUp.isDown()) {
                rotation = 315;
            } else if (Minecraft.getInstance().options.keyDown.isDown()) {
                rotation = 225;
            } else {
                rotation = 270;
            }
        }
        if (Minecraft.getInstance().options.keyRight.isDown()) {
            if (Minecraft.getInstance().options.keyUp.isDown()) {
                rotation = 45;
            } else if (Minecraft.getInstance().options.keyDown.isDown()) {
                rotation = 135;
            } else {
                rotation = 90;
            }
        }
        if (Minecraft.getInstance().options.keyDown.isDown()) rotation = 180;

        double yaw = Math.toRadians(rot + rotation);
        double x = -Math.sin(yaw);
        double y = yVelocity;
        double z = Math.cos(yaw);

        return new Vec3(x*scale, y, z*scale);
    }

    //ND stands for no diagonal
    public static Vec3 getFlatInputDirectionND(float rot,float scale,double yVelocity) {
        int rotation = 0;

        if (Minecraft.getInstance().options.keyLeft.isDown()) rotation = 270;
        if (Minecraft.getInstance().options.keyRight.isDown()) rotation = 90;
        if (Minecraft.getInstance().options.keyDown.isDown()) rotation = 180;

        double yaw = Math.toRadians(rot + rotation);
        double x = -Math.sin(yaw);
        double y = yVelocity;
        double z = Math.cos(yaw);

        return new Vec3(x*scale, y, z*scale);
    }

    public static Vec3 getFlatDirection(float rot,float scale,double yVelocity) {

        double yaw = Math.toRadians(rot);
        double x = -Math.sin(yaw);
        double y = yVelocity;
        double z = Math.cos(yaw);

        return new Vec3(x*scale, y, z*scale);
    }

    public static <T extends EnchantmentManager> EnchantmentManager getManagerForType(Player player, Class<T> desiredManager) {
        AtomicReference<T> returnManager= new AtomicReference<>(null);
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            for (EnchantmentManager manager : enchVar.getManagers()) {
                if (manager != null && manager.getClass().equals(desiredManager)) {
                    returnManager.set((T) manager);
                }
            }
        });
        return ((T) returnManager.get());
    }

    public static <T extends EnchantmentManager> int getSlotForType(Player player, Class<T> desiredManager) {
        AtomicInteger slot = new AtomicInteger(-1);
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            int tempSlot = 0;
            for (EnchantmentManager manager : enchVar.getManagers()) {
                if (manager != null && manager.getClass().equals(desiredManager)) {
                    slot.set(tempSlot);
                }
                ++tempSlot;
            }
        });
        return slot.get();
    }

    public static List<Entity> collectEntitiesBox(Player player, Vec3 position, double radius) {
        Vec3 scale = new Vec3(radius,radius,radius);
        return player.level().getEntities(null,new AABB(position.subtract(scale),position.add(scale)));
    }
}
