package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.MakeMeGlowC2SPacket;
import net.igneo.icv.networking.packet.WardenScreamC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WardenScreamEnchantment extends Enchantment {
    public static long wardenTime;
    public static Vec3 look;
    public static Vec3 playerpos;
    public static long hitdelay;

    private static boolean glowed = true;

    private static boolean shooting = false;
    public WardenScreamEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (!glowed && System.currentTimeMillis() >= wardenTime + 10000) {
            ModMessages.sendToServer(new MakeMeGlowC2SPacket());
            glowed = true;
        }

        if (Keybindings.wardenscream.isDown() && System.currentTimeMillis() >= wardenTime + 10000) {
            EnchantmentHudOverlay.screamFrames = 0;
            look = Minecraft.getInstance().player.getLookAngle();
            glowed = false;
            wardenTime = System.currentTimeMillis();
            //wardenhit.add(raycastEntities(Minecraft.getInstance().uniPlayer.getEyePosition(),Minecraft.getInstance().uniPlayer.getEyePosition().add(look)));
            playerpos = Minecraft.getInstance().player.getEyePosition();
            ModMessages.sendToServer(new WardenScreamC2SPacket(false, look.x, look.y, look.z));
            shooting = true;
        } else if (!shooting){
            hitdelay = System.currentTimeMillis();
        }

        if (shooting && System.currentTimeMillis() > hitdelay + 400) {
            Minecraft.getInstance().player.addDeltaMovement(new Vec3(Minecraft.getInstance().player.getLookAngle().reverse().x,0,Minecraft.getInstance().player.getLookAngle().reverse().z));
            ModMessages.sendToServer(new WardenScreamC2SPacket(true, look.x, look.y, look.z));
            shooting = false;
        }
    }

}
