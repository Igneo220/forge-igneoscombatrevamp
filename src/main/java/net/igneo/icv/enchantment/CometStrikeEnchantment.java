package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.CometStrikeC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class CometStrikeEnchantment extends Enchantment {

    private static long cometCooldown = -2500;

    public CometStrikeEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (Keybindings.comet_strike.isDown() && System.currentTimeMillis() >= cometCooldown + 2500 && uniPlayer.onGround() && !uniPlayer.isPassenger()) {
            cometCooldown = System.currentTimeMillis();
            ModMessages.sendToServer(new CometStrikeC2SPacket());
        }
    }

}
