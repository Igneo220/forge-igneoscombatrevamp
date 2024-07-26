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

public class DoubleJumpEnchantment extends Enchantment {

    public static boolean CanDoubleJump = false;

    public static double startY;

    public DoubleJumpEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @SubscribeEvent
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(0)).containsKey(ModEnchantments.DOUBLE_JUMP.get())) {
                if (pPlayer.onGround() && !CanDoubleJump) {
                    startY = Minecraft.getInstance().player.getY();
                    CanDoubleJump = true;
                }
                if (Minecraft.getInstance().options.keyJump.isDown() && !pPlayer.onGround() && !pPlayer.isInFluidType() && !pPlayer.isPassenger() && CanDoubleJump) {
                    if (Minecraft.getInstance().player.getDeltaMovement().y <= 0) {
                        CanDoubleJump = false;
                        pPlayer.setDeltaMovement(pPlayer.getDeltaMovement().x,0.6,pPlayer.getDeltaMovement().z);
                        ModMessages.sendToServer(new DoubleJumpC2SPacket());
                    }
                }
            }
        }
    }
    /*
    public static void onDoubleJumped()
    {
            pPlayer = Minecraft.getInstance().player;

            pPlayer.setDeltaMovement(pPlayer.getDeltaMovement().x, 0.6, pPlayer.getDeltaMovement().z);
            pPlayer.hurtMarked = true;

            pPlayer.level().playSound(pPlayer, pPlayer.blockPosition(), SoundType.SAND.getPlaceSound(), SoundSource.PLAYERS, 2F, 5.0F);

        if (pPlayer.level() instanceof ServerLevel sl)
        {
            for (int i = 0; i < 20; ++i)
            {
                double d0 = sl.random.nextGaussian() * 0.02D;
                double d1 = sl.random.nextGaussian() * 0.02D;
                double d2 = sl.random.nextGaussian() * 0.02D;
                //this.level().addParticle(ParticleTypes.SMOKE, d8 + this.random.nextGaussian() * (double)0.3F, d10 + this.random.nextGaussian() * (double)0.3F, d2 + this.random.nextGaussian() * (double)0.3F, 0.0D, 0.0D, 0.0D);
                sl.addParticle(ParticleTypes.POOF,pPlayer.getX(),pPlayer.getY(),pPlayer.getZ(),0,0,10);
                sl.sendParticles(ParticleTypes.POOF,pPlayer.getX(),pPlayer.getY(),pPlayer.getZ(),10,0,0,0,10);
                sl.addAlwaysVisibleParticle(ParticleTypes.POOF,pPlayer.getX(),pPlayer.getY(),pPlayer.getZ(),0,0,10);
            }
        }


    }*/



}
