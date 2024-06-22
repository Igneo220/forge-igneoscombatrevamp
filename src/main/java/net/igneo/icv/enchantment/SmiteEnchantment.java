package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.SmiteC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class SmiteEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer;
    public static boolean smiting;
    public static long smiteTime;
    public static int boltsShot;
    public SmiteEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().armor.get(3)).containsKey(ModEnchantments.SMITE.get())) {
                if (Keybindings.INSTANCE.smite.isDown() && pPlayer.onGround() && System.currentTimeMillis() >= smiteTime + 15000 && !smiting) {
                    System.out.println("starting");
                    smiteTime = System.currentTimeMillis();
                    boltsShot = 0;
                    ModMessages.sendToServer(new SmiteC2SPacket());
                    smiting = true;
                } else if (Keybindings.INSTANCE.smite.isDown() && smiting && boltsShot <= 2 && !pPlayer.onGround() && System.currentTimeMillis() >= smiteTime + 500) {
                    System.out.println("shooting bolts");
                    smiteTime = System.currentTimeMillis();
                    ++boltsShot;
                    ModMessages.sendToServer(new SmiteC2SPacket());
                } else if (boltsShot > 2 && smiting) {
                    smiteTime = System.currentTimeMillis();
                    System.out.println("finished");
                    smiting = false;
                }
            }
            if (pPlayer.onGround()) {
                //smiting = false;
            }
        }
    }*/
}
