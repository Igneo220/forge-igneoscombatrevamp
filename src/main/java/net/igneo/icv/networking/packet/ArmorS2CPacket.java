package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.event.ModEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.igneo.icv.event.ModEvents.*;

public class ArmorS2CPacket {
    private final int j;
    public ArmorS2CPacket(int slot){
        this.j = slot;
    }
    public ArmorS2CPacket(FriendlyByteBuf buf) {
        this.j = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(j);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                uniPlayer = Minecraft.getInstance().player;
                if (!uniPlayer.getInventory().armor.get(j).toString().contains("air")) {
                    List<Enchantment> enchantlist = new ArrayList<Enchantment>(uniPlayer.getInventory().armor.get(j).getAllEnchantments().keySet());
                    if (!enchantlist.isEmpty()) {
                        String enchID = enchantlist.get(0).getFullname(1).toString();
                        enchID = enchID.replace("', args=[]}[style={color=gray}]", "");
                        enchID = enchID.replace("translation{key='enchantment.icv.", "");
                        switch (j) {
                            case 0:
                                switch (enchID) {
                                    case "comet_strike":
                                        //System.out.println(enchID);
                                        enchVar.setBootID(1);
                                        break;
                                    case "double_jump":
                                        enchVar.setBootID(2);
                                        break;
                                    case "momentum":
                                        enchVar.setBootID(3);
                                        break;
                                    case "sky_charge":
                                        enchVar.setBootID(4);
                                        break;
                                    case "stone_caller":
                                        enchVar.setBootID(5);
                                        break;
                                }
                                break;
                            case 1:
                                refreshLegs = true;
                                switch (enchID) {
                                    case "acrobatic":
                                        enchVar.setLegID(1);
                                        break;
                                    case "crush":
                                        enchVar.setLegID(2);
                                        break;
                                    case "incapacitate":
                                        enchVar.setLegID(3);
                                        break;
                                    case "judgement":
                                        enchVar.setLegID(4);
                                        break;
                                    case "train_dash":
                                        enchVar.setLegID(5);
                                        break;
                                }
                                break;
                            case 2:
                                refreshChest = true;
                                switch (enchID) {
                                    case "concussion":
                                        enchVar.setChestID(1);
                                        break;
                                    case "flare":
                                        enchVar.setChestID(2);
                                        break;
                                    case "parry":
                                        enchVar.setChestID(3);
                                        break;
                                    case "siphon":
                                        enchVar.setChestID(4);
                                        break;
                                    case "wardenspine":
                                        enchVar.setChestID(5);
                                        break;
                                }
                                break;
                            case 3:
                                refreshHelmet = true;
                                switch (enchID) {
                                    case "black_hole":
                                        enchVar.setHelmetID(1);
                                        break;
                                    case "blizzard":
                                        enchVar.setHelmetID(2);
                                        break;
                                    case "flamethrower":
                                        enchVar.setHelmetID(3);
                                        break;
                                    case "smite":
                                        enchVar.setHelmetID(4);
                                        break;
                                    case "warden_scream":
                                        enchVar.setHelmetID(5);
                                        break;
                                }
                                break;
                        }
                    }
                } else {
                    switch (j) {
                        case 0:
                            enchVar.setBootID(0);
                            break;
                        case 1:
                            enchVar.setLegID(0);
                            break;
                        case 2:
                            enchVar.setChestID(0);
                            break;
                        case 3:
                            enchVar.setHelmetID(0);
                            break;
                    }
                }
            });
        });
        return true;
    }
}
