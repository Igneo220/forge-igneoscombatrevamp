package net.igneo.icv.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class RecoilEnchantment extends Enchantment {
    public RecoilEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, EnchantmentCategory.TRIDENT, pApplicableSlots);
    }
}
