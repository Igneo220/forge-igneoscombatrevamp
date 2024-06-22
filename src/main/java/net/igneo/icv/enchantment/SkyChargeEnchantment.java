package net.igneo.icv.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SkyChargeEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer = Minecraft.getInstance().player;
    public static long charge;
    public static boolean charged = false;
    public static double chargeamount;

    public SkyChargeEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    @SubscribeEvent
    public static void OnClientTick() {
        pPlayer = Minecraft.getInstance().player;
        if (!(pPlayer == null)) {
            if (ModEnchantments.checkBootEnchantments().contains("net.igneo.icv.enchantment.SkyChargeEnchantment")) {
                if (Minecraft.getInstance().options.keyShift.isDown() && pPlayer.onGround() && !pPlayer.isPassenger()) {
                    if (!charged) {
                        charge = System.currentTimeMillis();
                    }
                    chargeamount = (double) (System.currentTimeMillis() - charge) / 2000;
                    if (chargeamount >= 1.1) {
                        chargeamount = 1.1;
                    }
                    charged = true;
                } else if (charged) {
                    pPlayer.setDeltaMovement(0, chargeamount, 0);
                    charged = false;
                }
            }
        }
    }*/

}
