package net.igneo.icv.enchantment;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BlockHoleC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class BlackHoleEnchantment extends Enchantment {
    public BlackHoleEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
    public static void onKeyInputEvent() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            pPlayer.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if (System.currentTimeMillis() > enchVar.getHoleCooldown() + 29000 && EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(3)).containsKey(ModEnchantments.BLACK_HOLE.get())) {
                    if (Keybindings.black_hole.isDown()) {
                        ModMessages.sendToServer(new BlockHoleC2SPacket());
                        enchVar.setHoleCooldown(System.currentTimeMillis());
                    }
                }
            });
        }
    }
}
