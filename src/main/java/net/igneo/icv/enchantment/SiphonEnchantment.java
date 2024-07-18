package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.SiphonC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

public class SiphonEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer;
    public static boolean consumeClick;
    public static ItemStack boots;
    private static ItemStack legs;
    private static ItemStack chest;
    private static ItemStack helm;
    private static int bootsHealth;
    private static int legsHealth;
    private static int chestHealth;
    private static int helmHealth;
    private static float healAmount;
    public SiphonEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (!consumeClick) {
            consumeClick = true;
            if (EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(2)).containsKey(ModEnchantments.SIPHON.get())) {
                LocalPlayer pPlayer = Minecraft.getInstance().player;
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
                    if (bootsHealth > 50 && legsHealth > 50 && chestHealth > 50 && helmHealth > 50 && healAmount > 1) {
                        System.out.println("siphoning");
                        ModMessages.sendToServer(new SiphonC2SPacket());
                        pPlayer.getInventory().getArmor(0).setDamageValue(pPlayer.getInventory().getArmor(0).getDamageValue() + 25);
                        pPlayer.getInventory().getArmor(1).setDamageValue(pPlayer.getInventory().getArmor(1).getDamageValue() + 25);
                        pPlayer.getInventory().getArmor(2).setDamageValue(pPlayer.getInventory().getArmor(2).getDamageValue() + 25);
                        pPlayer.getInventory().getArmor(3).setDamageValue(pPlayer.getInventory().getArmor(3).getDamageValue() + 25);
                    } else {
                        pPlayer.level().playLocalSound(pPlayer.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS,1,1,false);
                    }
                }
            }
        }
    }
}
