package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.SiphonC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SiphonEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer;
    public static ItemStack boots;
    public static ItemStack legs;
    public static ItemStack chest;
    public static ItemStack helm;
    public static int bootsHealth;
    public static int legsHealth;
    public static int chestHealth;
    public static int helmHealth;
    public static float healAmount;
    public SiphonEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onKeyInputEvent() {
        if (ModEnchantments.checkChestEnchantments().contains("Siphon")) {
            pPlayer = Minecraft.getInstance().player;
            boots = pPlayer.getInventory().getArmor(0);
            legs = pPlayer.getInventory().getArmor(1);
            chest = pPlayer.getInventory().getArmor(2);
            helm = pPlayer.getInventory().getArmor(3);
            if (!boots.toString().contains("air") && !legs.toString().contains("air") && !chest.toString().contains("air") && !helm.toString().contains("air")) {
                bootsHealth = boots.getMaxDamage() - boots.getDamageValue();
                legsHealth = legs.getMaxDamage() - legs.getDamageValue();
                chestHealth = chest.getMaxDamage() - chest.getDamageValue();
                helmHealth = helm.getMaxDamage() - helm.getDamageValue();
                healAmount = pPlayer.getMaxHealth() - pPlayer.getHealth();
                if (bootsHealth > 50 && legsHealth > 50 && chestHealth > 50 && helmHealth > 50 && healAmount > 0) {
                    ModMessages.sendToServer(new SiphonC2SPacket());
                }
            }
        }
    }*/
}
