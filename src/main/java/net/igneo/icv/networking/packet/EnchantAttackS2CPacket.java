package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EnchantAttackS2CPacket {
    private final int slot;
    private final int ID;
    public EnchantAttackS2CPacket(int slot, int ID){
        this.slot = slot;
        this.ID = ID;
    }
    public EnchantAttackS2CPacket(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
        this.ID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeInt(ID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if (slot == 5) {
                    ((WeaponEnchantManager) enchVar.getManager(5)).onAttack(Minecraft.getInstance().player.level().getEntity(ID));
                } else if (slot == 4){
                    ((WeaponEnchantManager) enchVar.getManager(4)).onAttack(Minecraft.getInstance().player.level().getEntity(ID));
                }
            });
        });
        return true;
    }
}
