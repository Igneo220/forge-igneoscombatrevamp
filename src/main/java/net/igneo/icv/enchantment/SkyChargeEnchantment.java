package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.SkyChargeC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class SkyChargeEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer = Minecraft.getInstance().uniPlayer;
    public static long charge;
    public static boolean charged = false;
    public static double chargeamount;

    public SkyChargeEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @SubscribeEvent
    public static void onClientTick() {
        if (Minecraft.getInstance().options.keyShift.isDown() && uniPlayer.onGround() && !uniPlayer.isPassenger()) {
            if (!charged) {
                ModMessages.sendToServer(new SkyChargeC2SPacket(0));
                charge = System.currentTimeMillis();
            }
            charged = true;
        } else if (charged) {
            chargeamount = (double) (System.currentTimeMillis() - charge) / 1500;
            if (chargeamount >= 1.1) {
                chargeamount = 1.1;
            } else if (chargeamount <= 0.2) {
                chargeamount = 0;
            }
            uniPlayer.addDeltaMovement(new Vec3(0, chargeamount, 0));
            ModMessages.sendToServer(new SkyChargeC2SPacket(chargeamount));
            charged = false;
        }
    }
}
