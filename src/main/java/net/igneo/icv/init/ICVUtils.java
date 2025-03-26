package net.igneo.icv.init;

import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ICVEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
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

    @OnlyIn(Dist.CLIENT)
    public static void directUpdate(int pSlot) {
        if (Minecraft.getInstance().player != null) {
            updateManager(Minecraft.getInstance().player, pSlot);
        }
    }
    public static void updateManager(Player player, int pSlot) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            int slot = pSlot;
            if (pSlot == 0) {
                slot = 4;
            } else if (pSlot <= 4) {
                --slot;
            }
            List<Enchantment> enchList = new ArrayList<>();
            switch (slot) {
                case 0,1,2,3 -> enchList = player.getInventory().getArmor(slot).getAllEnchantments().keySet().stream().toList();
                case 4 -> enchList = player.getMainHandItem().getAllEnchantments().keySet().stream().toList();
                case 5 -> enchList = player.getOffhandItem().getAllEnchantments().keySet().stream().toList();
            }
            if (!enchList.isEmpty()) {
                for (Enchantment enchantment : enchList) {
                    if (enchantment instanceof ICVEnchantment enchant) {
                        enchVar.setManager(enchant.getManager(player), slot);
                    }
                }
            } else {
                enchVar.setManager(null, slot);
            }
        });
    }

    public static void useEnchant(Player player, int slot) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (enchVar.getManager(slot) != null) {
                enchVar.getManager(slot).use();
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void syncClientEntity(int ID, int slot) {
        Minecraft.getInstance().player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (enchVar.getManager(slot) instanceof EntityTracker manager) {
                System.out.println(Minecraft.getInstance().level.getEntity(ID));
                System.out.println(ID);
                if (ID == -1) {
                    manager.setChild(null);
                } else if (Minecraft.getInstance().level.getEntity(ID) instanceof ICVEntity entity) {
                    System.out.println("synced!");
                    entity.setOwner(Minecraft.getInstance().player);
                    manager.setChild(entity);
                }
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientCooldownDamageBonuses() {
        sendCooldownDamageBonuses(Minecraft.getInstance().player);
    }
    public static void sendCooldownDamageBonuses(Player player) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            for (EnchantmentManager manager : enchVar.getManagers()) {
                if (manager instanceof ArmorEnchantManager aManager) {
                    aManager.targetDamaged();
                }
            }
        });
    }
}

