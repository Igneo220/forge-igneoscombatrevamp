package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.StoneBreakC2SPacket;
import net.igneo.icv.networking.packet.StoneCallC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.openjdk.nashorn.internal.ir.Symbol;

public class StoneCallerEnchantment extends Enchantment {

    //public static LocalPlayer pPlayer = Minecraft.getInstance().player;

    public static int initialY;
    public static boolean finishcall = true;
    public static boolean blocksbroken = true;
    public static  boolean startCalling = true;
    public static long stoneTime;
    public static int loopCount;
    static double charge;
    public static BlockPos lastPos;
    public static long callCoolDown;
    public static boolean donecalling = false;
    static boolean checkcharge = true;
    public static int lastX;
    public static int lastZ;
    static boolean stonecalled = false;
    public StoneCallerEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    @SubscribeEvent
    public static void OnClientTick() {
        pPlayer = Minecraft.getInstance().player;
        if (!(pPlayer == null)) {
            if (ModEnchantments.checkEnchantments().contains("net.igneo.icv.enchantment.StoneCallerEnchantment")) {
                if (Minecraft.getInstance().options.keyShift.isDown()){
                    if (System.currentTimeMillis() > charge + 1000 && !stonecalled){
                        if (startCalling) {
                            initialY = pPlayer.getBlockY() - 1;
                            lastX = pPlayer.getBlockX();
                            lastZ = pPlayer.getBlockZ();
                            pPlayer.setDeltaMovement(0, 1, 0);
                        }
                        startCalling = false;
                        donecalling = false;
                        callCoolDown = System.currentTimeMillis();
                        callStone(100);
                    }
                } else {
                    charge = System.currentTimeMillis();
                }
                if (stonecalled){
                    charge = System.currentTimeMillis();
                }
            }
            breakStone();
        }
    }
    public static void callStone(int stoneDelay) {
        if (finishcall && !donecalling) {
            finishcall = false;
            stoneTime = System.currentTimeMillis();
            loopCount = 0;
        } else if (!finishcall) {
            System.out.println("CALLING STONE!!!");
            if (System.currentTimeMillis() >= stoneTime + stoneDelay) {
                ModMessages.sendToServer(new StoneCallC2SPacket());
                pPlayer.level().playSound(pPlayer, pPlayer.blockPosition(), SoundType.DEEPSLATE_BRICKS.getBreakSound(), SoundSource.PLAYERS, 10F, 0.1F);
                stoneTime = System.currentTimeMillis();
                loopCount = loopCount + 1;
            }
            if (loopCount > 3) {
                finishcall = true;
                donecalling = true;
                stonecalled = true;
                startCalling = true;
            }
        }
    }
    public static void breakStone() {
        if(stonecalled) {
            if (System.currentTimeMillis() >= 5000 + callCoolDown) {
                System.out.println("BREAKING!!");
                pPlayer.level().playSound(pPlayer, pPlayer.blockPosition(), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.PLAYERS, 2F, 5.0F);
                ModMessages.sendToServer(new StoneBreakC2SPacket());
                blocksbroken = true;
                stonecalled = false;
            }
        }
    }*/

}
