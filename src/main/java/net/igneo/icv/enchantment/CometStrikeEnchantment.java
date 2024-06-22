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

    public static long cometCooldown = -2500;

    public CometStrikeEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }/*
    @SubscribeEvent
    public static void onClientTick() {
        pPlayer = Minecraft.getInstance().player;
        if (!(pPlayer == null)) {
            pPlayer = Minecraft.getInstance().player;
            var enchant = EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(0));
            var enchants = enchant.toString();
            if (enchants.contains("net.igneo.icv.enchantment.CometStrikeEnchantment")) {
                if (Minecraft.getInstance().options.keyShift.isDown() && System.currentTimeMillis() >= cometCooldown + 2500) {
                    cometCooldown = System.currentTimeMillis();
                    ModMessages.sendToServer(new CometStrikeC2SPacket());
                }
            }
        }

    }
    public static void addOneEntity() {
        System.out.println("hoooraayyyy");
        WitherBoss witherBoss = new WitherBoss(EntityType.WITHER, pPlayer.level());
        witherBoss.moveTo(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
        pPlayer.level().addFreshEntity(witherBoss);
        //EntityType.WITHER.spawn(CometStrikeEnchantment., pPlayer.blockPosition(), MobSpawnType.MOB_SUMMONED);
        new MyThisTest();
    }
    public static class MyThisTest {
        
    }*/

}
