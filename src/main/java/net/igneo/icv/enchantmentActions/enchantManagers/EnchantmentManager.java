package net.igneo.icv.enchantmentActions.enchantManagers;

import com.alrex.parcool.common.action.impl.Dodge;
import com.alrex.parcool.common.action.impl.Slide;
import com.alrex.parcool.common.capability.Parkourability;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.AnimatedSyncC2SPacket;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EnchantmentManager {
    @OnlyIn(Dist.CLIENT)
    public ModifierLayer<IAnimation> animator;
    public int activeTicks = 0;
    public boolean active = false;
    protected EnchantmentManager(EnchantType setSlot, Player player) {
        this.type = setSlot;
        this.player = player;
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            this.enchVar = enchVar;
        });
        if (player instanceof LocalPlayer) {
            this.animator = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(new ResourceLocation(ICV.MOD_ID, "enchant_animator"));
        }
    }
    public PlayerEnchantmentActions enchVar;
    public Player player;
    private final EnchantType type;

    public EnchantType getType() {
        return type;
    }
    public void use() {
        this.activate();
    }

    public abstract boolean canUse();

    public abstract void activate();

    public void onRemove() {};
    public void onEquip() {};

    public void tick() {
        if (active) {
            ++activeTicks;
        } else {
            activeTicks = 0;
        }
        if (player.level().isClientSide) {
            if (!animator.isActive() && enchVar.animated) {
                enchVar.animated = false;
                ModMessages.sendToServer(new AnimatedSyncC2SPacket(false));
            }
        }
    };

    public boolean stableCheck() {
        return player.onGround() &&
                !enchVar.animated &&
                !player.isSwimming() &&
                !Parkourability.get(player).get(Dodge.class).isDoing() &&
                !Parkourability.get(player).get(Slide.class).isDoing();
    }

}
