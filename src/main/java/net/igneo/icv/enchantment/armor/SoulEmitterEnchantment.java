package net.igneo.icv.enchantment.armor;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.SoulEmitterManager;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.ConcussC2SPacket;
import net.igneo.icv.networking.packet.ConcussHurtC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class SoulEmitterEnchantment extends ICVEnchantment {
    public SoulEmitterEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public EnchantmentManager getManager(Player player) {
        return new SoulEmitterManager(player);
    }


}
