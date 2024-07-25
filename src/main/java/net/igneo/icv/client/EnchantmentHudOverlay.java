package net.igneo.icv.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import static net.igneo.icv.enchantment.BlizzardEnchantment.doBeIcin;
import static net.igneo.icv.enchantment.BlizzardEnchantment.iceTime;
import static net.igneo.icv.enchantment.ConcussionEnchantment.concussTime;
import static net.igneo.icv.enchantment.FlamethrowerEnchantment.flameTime;
import static net.igneo.icv.enchantment.FlamethrowerEnchantment.flameo;
import static net.igneo.icv.enchantment.FlareEnchantment.chargeTime;
import static net.igneo.icv.enchantment.FlareEnchantment.charging;
import static net.igneo.icv.enchantment.IncapacitateEnchantment.incaCool;
import static net.igneo.icv.enchantment.JudgementEnchantment.judgeTime;
import static net.igneo.icv.enchantment.ParryEnchantment.parryCooldown;
import static net.igneo.icv.enchantment.SmiteEnchantment.smiteTime;
import static net.igneo.icv.enchantment.SmiteEnchantment.smiting;
import static net.igneo.icv.enchantment.TrainDashEnchantment.dashing;
import static net.igneo.icv.enchantment.TrainDashEnchantment.trainDelay;
import static net.igneo.icv.enchantment.WardenScreamEnchantment.wardenTime;

public class EnchantmentHudOverlay {
    public static long animTime = 0;
    public static int blizzardFrames = 0;
    public static int holeFrames = 0;
    public static int concussFrames = 0;
    public static int incaFrames = 0;
    public static int screamFrames = 0;
    public static int parryFrames = 0;
    public static int trainFrames = 0;
    public static int flareFrames = 0;
    public static int judgeFrames = 0;
    public static int flameFrames = 0;
    public static int smiteFrames = 0;
    private static final ResourceLocation BLIZZARD = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/blizzard.png");
    private static final ResourceLocation BLACK_HOLE = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/black_hole.png");
    private static final ResourceLocation FLAMETHROWER = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/flamethrower.png");
    private static final ResourceLocation WARDEN_SCREAM = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/warden_scream.png");
    private static final ResourceLocation SMITE = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/smite.png");



    private static final ResourceLocation CONCUSSION = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/concussion.png");
    private static final ResourceLocation PARRY = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/parry.png");
    private static final ResourceLocation FLARE = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/flare.png");



    private static final ResourceLocation INCAPACITATE = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/incapacitate.png");
    private static final ResourceLocation TRAIN_DASH = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/traindash.png");
    private static final ResourceLocation JUDGEMENT = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/judge.png");

    public static final IGuiOverlay HUD_ENCHANTMENTS = ((gui, poseStack, partialTick, width, height) -> {
        if (animTime == 0) {
            animTime = System.currentTimeMillis();
        }

        int x = width / 2;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BLIZZARD);
        //for(int i = 0; i < 10; i++) {
        //GuiGraphics graphic = new GuiGraphics(gui.getMinecraft(),gui.getMinecraft().renderBuffers().bufferSource());
        //if(Minecraft.getInstance().options.keyUp.isDown()) {
        if(!doBeIcin && System.currentTimeMillis() < iceTime + 21000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(3)).containsKey(ModEnchantments.BLIZZARD.get()) && (blizzardFrames * 16) < 2384) {
            poseStack.blit(BLIZZARD, x - 94 + (1), y - 65, 0, 16 * blizzardFrames, 16, 16, 16, 2384);
            if (System.currentTimeMillis() >= animTime + 125) {
                ++blizzardFrames;
            }
        }
        if(!smiting && System.currentTimeMillis() < smiteTime + 21000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(3)).containsKey(ModEnchantments.SMITE.get()) && (smiteFrames * 16) < 2272) {
            poseStack.blit(SMITE, x - 94 + (1), y - 65, 0, 16 * smiteFrames, 16, 16, 16, 2272);
            if (System.currentTimeMillis() >= animTime + 125) {
                ++smiteFrames;
            }
        }
        if(System.currentTimeMillis() < wardenTime + 20000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(3)).containsKey(ModEnchantments.WARDEN_SCREAM.get()) && (screamFrames * 16) < 1920) {
            poseStack.blit(WARDEN_SCREAM, x - 94 + (1), y - 65, 0, 16 * screamFrames, 16, 16, 16, 1920);
            if (System.currentTimeMillis() >= animTime + 125) {
                ++screamFrames;
            }
        }
        if(!flameo && System.currentTimeMillis() < flameTime + 20000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(3)).containsKey(ModEnchantments.FLAMETHROWER.get()) && (flameFrames * 16) < 2368) {
            poseStack.blit(FLAMETHROWER, x - 94 + (1), y - 65, 0, 16 * flameFrames, 16, 16, 16, 2368);
            if (System.currentTimeMillis() >= animTime + 125) {
                ++flameFrames;
            }
        }
        Minecraft.getInstance().player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (System.currentTimeMillis() < enchVar.getHoleCooldown() + 32000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(3)).containsKey(ModEnchantments.BLACK_HOLE.get()) && (holeFrames * 16) < 3680) {
                poseStack.blit(BLACK_HOLE, x - 94 + (1), y - 65, 0, 16 * holeFrames, 16, 16, 16, 3680);
                if (System.currentTimeMillis() >= animTime + 125) {
                    ++holeFrames;
                }
            }
        });



        if(System.currentTimeMillis() < concussTime + 21000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(2)).containsKey(ModEnchantments.CONCUSSION.get()) && (concussFrames * 16) < 960) {
            poseStack.blit(CONCUSSION, x - 94 + (1) + 16, y - 65, 0, 16 * concussFrames, 16, 16, 16, 960);
            if (System.currentTimeMillis() >= animTime + 125) {
                ++concussFrames;
            }
        }
        if(System.currentTimeMillis() < parryCooldown + 6000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(2)).containsKey(ModEnchantments.PARRY.get()) && (parryFrames * 16) < 672) {
            poseStack.blit(PARRY, x - 94 + (1) + 16, y - 65, 0, 16 * parryFrames, 16, 16, 16, 672);
            if (System.currentTimeMillis() >= animTime + 125) {
                ++parryFrames;
            }
        }
        if(!charging && System.currentTimeMillis() < chargeTime + 10000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(2)).containsKey(ModEnchantments.FLARE.get()) && (flareFrames * 16) < 1184) {
            poseStack.blit(FLARE, x - 94 + (1) + 16, y - 65, 0, 16 * flareFrames, 16, 16, 16, 1184);
            if (System.currentTimeMillis() >= animTime + 125) {
                ++flareFrames;
            }
        }



        if(System.currentTimeMillis() < incaCool + 15000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(1)).containsKey(ModEnchantments.INCAPACITATE.get()) && (incaFrames * 16) < 1648) {
            poseStack.blit(INCAPACITATE, x - 94 + (1) + 32, y - 65, 0, 16 * incaFrames, 16, 16, 16, 1648);
            if (System.currentTimeMillis() >= animTime + 125) {
                ++incaFrames;
            }
        }
        if(!dashing && System.currentTimeMillis() < trainDelay + 10000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(1)).containsKey(ModEnchantments.TRAIN_DASH.get()) && (trainFrames * 16) < 1040) {
            poseStack.blit(TRAIN_DASH, x - 94 + (1) + 32, y - 65, 0, 16 * trainFrames, 16, 16, 16, 1040);
            if (System.currentTimeMillis() >= animTime + 125) {
                ++trainFrames;
            }
        }
        if(System.currentTimeMillis() < judgeTime + 15000 && EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(1)).containsKey(ModEnchantments.JUDGEMENT.get()) && (judgeFrames * 16) < 928) {
            poseStack.blit(JUDGEMENT, x - 94 + (1) + 32, y - 65, 0, 16 * judgeFrames, 16, 16, 16, 928);
            if (System.currentTimeMillis() >= animTime + 125) {
                ++judgeFrames;
            }
        }



        if (System.currentTimeMillis() >= animTime + 125) {
            animTime = System.currentTimeMillis();
        }
    });
}
