package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BlizzardC2SPacket;
import net.igneo.icv.networking.packet.WardenScreamC2SPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.gui.MinecraftServerGui;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.List;

public class WardenScreamEnchantment extends Enchantment {
    public static long wardenTime;
    public static Vec3 look;
    public static Vec3 playerpos;
    public static List<net.minecraft.world.entity.Entity> wardenhit;
    public WardenScreamEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            if (ModEnchantments.checkHelmEnchantments().contains("Scream")) {
                if (Keybindings.INSTANCE.wardenscream.isDown() && System.currentTimeMillis() >= wardenTime + 1500) {
                    look = Minecraft.getInstance().player.getLookAngle();
                    wardenTime = System.currentTimeMillis();
                    //wardenhit.add(raycastEntities(Minecraft.getInstance().player.getEyePosition(),Minecraft.getInstance().player.getEyePosition().add(look)));
                    //System.out.println(wardenhit);
                    playerpos = Minecraft.getInstance().player.getEyePosition();
                    System.out.println(playerpos);
                    ModMessages.sendToServer(new WardenScreamC2SPacket());
                }
            }
        }
    }*/

}
