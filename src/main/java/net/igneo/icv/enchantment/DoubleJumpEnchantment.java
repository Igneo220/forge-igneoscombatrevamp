package net.igneo.icv.enchantment;

import com.mojang.brigadier.CommandDispatcher;
import net.igneo.icv.ICV;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.DoubleJumpC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.PlaySoundCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class DoubleJumpEnchantment extends Enchantment {

    public static boolean CanDoubleJump = false;

    public static double startY;

    public DoubleJumpEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @SubscribeEvent
    public static void onClientTick() {
        if (uniPlayer.onGround() && !CanDoubleJump) {
            startY = Minecraft.getInstance().player.getY();
            CanDoubleJump = true;
        }
        if (Minecraft.getInstance().options.keyJump.isDown() && !uniPlayer.onGround() && !uniPlayer.isInFluidType() && !uniPlayer.isPassenger() && CanDoubleJump) {
            if (Minecraft.getInstance().player.getDeltaMovement().y <= 0) {
                CanDoubleJump = false;
                uniPlayer.setDeltaMovement(uniPlayer.getDeltaMovement().x, 0.6, uniPlayer.getDeltaMovement().z);
                ModMessages.sendToServer(new DoubleJumpC2SPacket());
            }
        }
    }
}
