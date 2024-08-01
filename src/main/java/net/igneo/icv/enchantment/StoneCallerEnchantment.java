package net.igneo.icv.enchantment;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.StoneCallerC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class StoneCallerEnchantment extends Enchantment {
    private static long charge = 0;
    private static boolean stoneCalling = false;
    private static long stoneDelay;
    private static int loop;
    public StoneCallerEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }


    public static void onClientTick() {
        uniPlayer.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (stoneCalling) {
                callStone();
            }
            if (Minecraft.getInstance().options.keyShift.isDown() && uniPlayer.onGround() && enchVar.getStoneTime() == 0) {
                if (charge == 0) {
                    charge = System.currentTimeMillis();
                } else if (System.currentTimeMillis() > charge + 500) {
                    stoneCalling = true;
                    charge = 0;
                }
            } else if (charge != 0) {
                charge = 0;
            }
        });
    }

    private static void callStone() {
        if (System.currentTimeMillis() > stoneDelay + 150) {
            uniPlayer.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if(enchVar.getStoneTime() == 0) {
                    enchVar.setStoneX(uniPlayer.getBlockX());
                    enchVar.setStoneY(uniPlayer.getBlockY());
                    enchVar.setStoneZ(uniPlayer.getBlockZ());
                    enchVar.setStoneTime(System.currentTimeMillis());
                    uniPlayer.setDeltaMovement(0, 1, 0);
                }
                ModMessages.sendToServer(new StoneCallerC2SPacket(loop,enchVar.getStoneX(), enchVar.getStoneY(), enchVar.getStoneZ()));
            });
            ++loop;
            stoneDelay = System.currentTimeMillis();
        } else if (loop > 3) {
            loop = 0;
            stoneCalling = false;
        }
    }
}
