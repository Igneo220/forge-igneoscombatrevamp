package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.SmiteC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class SmiteEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer;
    public static boolean smiting;
    public static long smiteTime;
    public static int boltsShot;
    public SmiteEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().armor.get(3)).containsKey(ModEnchantments.SMITE.get())) {
                if (Keybindings.smite.isDown() && System.currentTimeMillis() >= smiteTime + 15000 && !smiting) {
                    System.out.println("starting");
                    pPlayer.setDeltaMovement(0,1,0);
                    smiteTime = System.currentTimeMillis();
                    boltsShot = 0;
                    ModMessages.sendToServer(new SmiteC2SPacket(boltsShot));
                    smiting = true;
                } else if (Keybindings.smite.isDown() && smiting && boltsShot <= 2 && !pPlayer.onGround() && System.currentTimeMillis() >= smiteTime + 500) {
                    System.out.println("shooting bolts");
                    smiteTime = System.currentTimeMillis();
                    ++boltsShot;
                    pPlayer.addDeltaMovement(new Vec3(pPlayer.getLookAngle().x/10,0,pPlayer.getLookAngle().z/10).reverse());
                    ModMessages.sendToServer(new SmiteC2SPacket(boltsShot));
                } else if (boltsShot > 2 && smiting) {
                    smiteTime = System.currentTimeMillis();
                    System.out.println("finished");
                    smiting = false;
                }
                if (smiting) {
                    pPlayer.addDeltaMovement(new Vec3(0,0.05,0));
                }
                if (pPlayer.onGround() && System.currentTimeMillis() > smiteTime + 200 && smiting) {
                    smiting = false;
                    smiteTime = System.currentTimeMillis();
                    System.out.println("finished");
                }
            }
        }
    }
}
