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

public class SkyChargeEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer = Minecraft.getInstance().player;
    public static long charge;
    public static boolean charged = false;
    public static double chargeamount;

    public SkyChargeEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @SubscribeEvent
    public static void onClientTick() {
        LocalPlayer pPlayer = Minecraft.getInstance().player;
        if (!(pPlayer == null)) {
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(0)).containsKey(ModEnchantments.SKY_CHARGE.get())) {
                if (Minecraft.getInstance().options.keyShift.isDown() && pPlayer.onGround() && !pPlayer.isPassenger()) {
                    if (!charged) {
                        ModMessages.sendToServer(new SkyChargeC2SPacket(0));
                        charge = System.currentTimeMillis();
                    }
                    charged = true;
                } else if (charged) {
                    chargeamount = (double) (charge - System.currentTimeMillis()) / 1000;
                    if (chargeamount >= 1.1) {
                        chargeamount = 1.1;
                    } else if (chargeamount <= 0.2) {
                        chargeamount = 0;
                    }
                    pPlayer.addDeltaMovement(new Vec3(0, chargeamount, 0));
                    ModMessages.sendToServer(new SkyChargeC2SPacket(chargeamount));
                    charged = false;
                }
            }
        }
    }

}
