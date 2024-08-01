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

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class SiphonEnchantment extends Enchantment {
    //public static LocalPlayer uniPlayer;
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
            boots = uniPlayer.getInventory().getArmor(0);
            legs = uniPlayer.getInventory().getArmor(1);
            chest = uniPlayer.getInventory().getArmor(2);
            helm = uniPlayer.getInventory().getArmor(3);
            if (!boots.toString().contains("air") && !legs.toString().contains("air") && !chest.toString().contains("air") && !helm.toString().contains("air")) {
                bootsHealth = boots.getMaxDamage() - boots.getDamageValue();
                legsHealth = legs.getMaxDamage() - legs.getDamageValue();
                chestHealth = chest.getMaxDamage() - chest.getDamageValue();
                helmHealth = helm.getMaxDamage() - helm.getDamageValue();
                healAmount = uniPlayer.getMaxHealth() - uniPlayer.getHealth();
                if (bootsHealth > 50 && legsHealth > 50 && chestHealth > 50 && helmHealth > 50 && healAmount > 1) {
                    ModMessages.sendToServer(new SiphonC2SPacket());
                    uniPlayer.getInventory().getArmor(0).setDamageValue(uniPlayer.getInventory().getArmor(0).getDamageValue() + 25);
                    uniPlayer.getInventory().getArmor(1).setDamageValue(uniPlayer.getInventory().getArmor(1).getDamageValue() + 25);
                    uniPlayer.getInventory().getArmor(2).setDamageValue(uniPlayer.getInventory().getArmor(2).getDamageValue() + 25);
                    uniPlayer.getInventory().getArmor(3).setDamageValue(uniPlayer.getInventory().getArmor(3).getDamageValue() + 25);
                } else {
                    uniPlayer.level().playLocalSound(uniPlayer.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS,1,1,false);
                }
            }
        }
    }
}
