package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.CometStrikeC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CometStrikeEnchantment extends Enchantment {

    private static long cometCooldown = -2500;

    public CometStrikeEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(0)).containsKey(ModEnchantments.COMET_STRIKE.get())) {
                if (Minecraft.getInstance().options.keyShift.isDown() && System.currentTimeMillis() >= cometCooldown + 2500 && pPlayer.onGround() && !pPlayer.isPassenger()) {
                    cometCooldown = System.currentTimeMillis();
                    ModMessages.sendToServer(new CometStrikeC2SPacket());
                }
            }
        }

    }

}
