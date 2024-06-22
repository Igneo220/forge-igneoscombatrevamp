package net.igneo.icv.enchantment;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.PhantomPainC2SPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class PhantomPainEnchantment extends Enchantment {

    public PhantomPainEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pTarget, Entity pAttacker, int pLevel) {
        System.out.println(pAttacker + " just got hit by " + pTarget);
        pTarget.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (enchVar.getPhantomVictim() != null) {
                if (enchVar.getPhantomVictim() != pAttacker) {
                    enchVar.getPhantomVictim().heal(enchVar.getPhantomHurt());
                    enchVar.resetPhantomHurt();
                    enchVar.deletePhantomVictim();
                    System.out.println("healing previous entity");
                }
            }
            enchVar.setPhantomVictim((LivingEntity) pAttacker);
            enchVar.addPhantomHurt(4);
            enchVar.setPhantomDelay(System.currentTimeMillis());
            System.out.println(enchVar.getPhantomVictim());
            System.out.println(enchVar.getPhantomHurt());
        });
        super.doPostAttack(pTarget, pAttacker, pLevel);
    }

    @Override
    public float getDamageBonus(int level, MobType mobType, ItemStack enchantedItem) {
        return 4;
    }
}
