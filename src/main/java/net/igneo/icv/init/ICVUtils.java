package net.igneo.icv.init;

import com.google.common.collect.Multimap;
import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.enchantmentActions.Input;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.networking.ModMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static net.igneo.icv.enchantmentActions.Input.getRotation;

public class ICVUtils {
    
    public static Vec3 getFlatInputDirection(float rot, Input input, float scale, double yVelocity) {
        int rotation = getRotation(input);
        double yaw = Math.toRadians(rot + rotation);
        double x = -Math.sin(yaw);
        double y = yVelocity;
        double z = Math.cos(yaw);
        
        return new Vec3(x * scale, y, z * scale);
    }
    
    public static Vec3 getFlatDirection(float rot, float scale, double yVelocity) {
        
        double yaw = Math.toRadians(rot);
        double x = -Math.sin(yaw);
        double y = yVelocity;
        double z = Math.cos(yaw);
        
        return new Vec3(x * scale, y, z * scale);
    }
    
    public static <T extends EnchantmentManager> EnchantmentManager getManagerForType(Player player, Class<T> desiredManager) {
        AtomicReference<T> returnManager = new AtomicReference<>(null);
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            for (EnchantmentManager manager : enchVar.getManagers()) {
                if (manager != null && manager.getClass().equals(desiredManager)) {
                    returnManager.set((T) manager);
                }
            }
        });
        return returnManager.get();
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
    
    public static List<Entity> collectEntitiesBox(Level level, Vec3 position, double radius) {
        Vec3 scale = new Vec3(radius, radius, radius);
        return level.getEntities(null, new AABB(position.subtract(scale), position.add(scale)));
    }
    
    @OnlyIn (Dist.CLIENT)
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
                case 0, 1, 2, 3 ->
                        enchList = player.getInventory().getArmor(slot).getAllEnchantments().keySet().stream().toList();
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
    
    @OnlyIn (Dist.CLIENT)
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
    
    @OnlyIn (Dist.CLIENT)
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
    
    public static ItemStack getItemForSlot(Player player, int slot) {
        return switch (slot) {
            case (1) -> player.getInventory().getArmor(1);
            case (2) -> player.getInventory().getArmor(2);
            case (3) -> player.getInventory().getArmor(3);
            case (4) -> player.getMainHandItem();
            case (5) -> player.getOffhandItem();
            default -> player.getInventory().getArmor(0);
        };
    }
    
    public static final Vec3 DOWN_VECTOR = new Vec3(0, -1, 0);
    public static final Vec3 UP_VECTOR = new Vec3(0, 1, 0);
    public static final Vec3 FOWARD_VECTOR = new Vec3(0, 0, 1);
    public static final Vec3 BACKWORD_VECTOR = new Vec3(0, 0, -1);
    public static final Vec3 LEFT_VECTOR = new Vec3(-1, 0, 0);
    public static final Vec3 RIGHT_VECTOR = new Vec3(1, 0, 0);
    
    public static double getDamageFromItem(ItemStack item, Entity entity) {
        Multimap<Attribute, AttributeModifier> modifiers = item.getAttributeModifiers(EquipmentSlot.MAINHAND);
        Collection<AttributeModifier> damageModifiers = modifiers.get(Attributes.ATTACK_DAMAGE);
        double baseDamage = 0.0;
        for (AttributeModifier mod : damageModifiers) {
            baseDamage += mod.getAmount();
        }
        if (entity instanceof Player player) baseDamage += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        
        return baseDamage;
    }
    
    public static <MSG> void sendPacketInRange(ServerLevel level, Vec3 pos, float range, MSG message) {
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(pos) < range) ModMessages.sendToPlayer(message, player);
        }
    }
}
