package net.igneo.icv.enchantment.weapon;

import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.EnchantAttackS2CPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.concurrent.atomic.AtomicReference;

public abstract class WeaponEnchantment extends ICVEnchantment {
    protected WeaponEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public ServerLevel currentLevel;

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        if (!pAttacker.level().isClientSide) {
            currentLevel = (ServerLevel) pAttacker.level();
            if (pAttacker instanceof Player player) {
                player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                    if (enchVar.getManager(4).getClass() == this.getManager(player).getClass()) {
                        ((WeaponEnchantManager) enchVar.getManager(4)).onAttack(pTarget);
                        ModMessages.sendToPlayer(new EnchantAttackS2CPacket(4, pTarget.getId()), (ServerPlayer) player);
                    } else if (enchVar.getManager(5).getClass() == this.getManager(player).getClass()) {
                        ((WeaponEnchantManager) enchVar.getManager(5)).onAttack(pTarget);
                        ModMessages.sendToPlayer(new EnchantAttackS2CPacket(5, pTarget.getId()), (ServerPlayer) player);
                    }
                });
            }
        }
    }

    @Override
    public float getDamageBonus(int power, MobType mobType, ItemStack enchantedItem) {
        AtomicReference<Float> bonus = new AtomicReference<>((float) 0);
        AtomicReference<Boolean> shouldBreak = new AtomicReference<>((boolean) false);
        if (performBonusCheck() && currentLevel != null) {
            for (ServerPlayer player : currentLevel.players()) {
                player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                    if (player.getMainHandItem().equals(enchantedItem)) {
                        if (enchVar.getManager(4) instanceof WeaponEnchantManager manager) {
                            bonus.set(manager.getDamageBonus());
                            shouldBreak.set(true);
                        }
                    } else if (player.getOffhandItem().equals(enchantedItem)) {
                        if (enchVar.getManager(5) instanceof WeaponEnchantManager manager) {
                            bonus.set(manager.getDamageBonus());
                            shouldBreak.set(true);
                        }
                    }
                });
                if (shouldBreak.get()) break;
            }
        }
        System.out.println(bonus.get());
        return bonus.get();
    }

    public boolean performBonusCheck() {
        return false;
    }

    public static ServerLevel getServerLevel(ResourceKey<Level> dimension) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer(); // Gets the current server
        if (server != null) {
            return server.getLevel(dimension); // Returns the world (level) for the given dimension
        }
        return null;
    }
}
