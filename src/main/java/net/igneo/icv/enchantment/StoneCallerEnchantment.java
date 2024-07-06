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

public class StoneCallerEnchantment extends Enchantment {
    private static long charge = 0;
    private static boolean stoneCalling = false;
    private static long stoneDelay;
    private static int loop;
    public StoneCallerEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }


    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            pPlayer.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if (stoneCalling) {
                    callStone();
                }
                if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(0)).containsKey(ModEnchantments.STONE_CALLER.get())) {
                    if (Minecraft.getInstance().options.keyShift.isDown() && pPlayer.onGround() && enchVar.getStoneTime() == 0) {
                        if (charge == 0) {
                            charge = System.currentTimeMillis();
                        } else if (System.currentTimeMillis() > charge + 1000) {
                            stoneCalling = true;
                            charge = 0;
                        }
                    } else if (charge != 0) {
                        charge = 0;
                    }
                }
            });
        }
    }

    private static void callStone() {
        if (System.currentTimeMillis() > stoneDelay + 150) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            pPlayer.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if(enchVar.getStoneTime() == 0) {
                    enchVar.setStoneX(pPlayer.getBlockX());
                    enchVar.setStoneY(pPlayer.getBlockY());
                    enchVar.setStoneZ(pPlayer.getBlockZ());
                    enchVar.setStoneTime(System.currentTimeMillis());
                    pPlayer.setDeltaMovement(0, 1, 0);
                }
                ModMessages.sendToServer(new StoneCallerC2SPacket(loop,enchVar.getStoneX(), enchVar.getStoneY(), enchVar.getStoneZ()));
                //Minecraft.getInstance().level.setBlock(new BlockPos(enchVar.getStoneX(),enchVar.getStoneY() + loop,enchVar.getStoneZ()), Blocks.STONE.defaultBlockState(), 2);
            });
            ++loop;
            stoneDelay = System.currentTimeMillis();
        } else if (loop > 3) {
            loop = 0;
            stoneCalling = false;
        }
    }
}
