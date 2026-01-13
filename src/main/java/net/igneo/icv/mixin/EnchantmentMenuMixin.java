package net.igneo.icv.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

import static net.igneo.icv.ForMixins.ForEnchantmentMenuMixin.*;


@Mixin (value = EnchantmentMenu.class)
public class EnchantmentMenuMixin extends AbstractContainerMenu {
    @Shadow
    private final RandomSource random = RandomSource.create();
    @Shadow
    private final DataSlot enchantmentSeed = DataSlot.standalone();
    @Shadow
    public final int[] enchantClue = new int[]{-1, -1, -1};
    @Shadow
    public final int[] levelClue = new int[]{-1, -1, -1};

    private int localEnchShift = returnLEnchShift();
    private int localLength = returnLLength();
    private ServerPlayer player = null;
    @Shadow
    private final Container enchantSlots = new SimpleContainer(2) {
        /**
         * For block entities, ensures the chunk containing the block entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void setChanged() {
            super.setChanged();
            EnchantmentMenuMixin.this.slotsChanged(this);
        }
    };
    @Shadow
    private final ContainerLevelAccess access;
    @Shadow
    public int[] costs = new int[3];
    
    @OnlyIn (Dist.CLIENT)
    protected EnchantmentMenuMixin(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(MenuType.ENCHANTMENT, pContainerId);
        //super(MenuType.ENCHANTMENT, pContainerId);
        access = pAccess;
    }
    
    @Override
    @Shadow
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }
    
    @Override
    @Shadow
    public boolean stillValid(Player pPlayer) {
        return false;
    }
    
    /**
     * @author Igneo220
     * @reason Rewriting enchantment table
     */
    @Overwrite
    public boolean clickMenuButton(Player pPlayer, int pId) {
        if (pId >= 0) {
            ItemStack itemstack = this.enchantSlots.getItem(0);
            ItemStack itemstack1 = this.enchantSlots.getItem(1);
            //int i = pId + 1;
            if ((itemstack1.isEmpty() && !pPlayer.isCreative()) || (itemstack1.getCount() < 1 && !pPlayer.isCreative())) {
                return false;
            } else if ((itemstack.isEmpty() && !pPlayer.isCreative()) || (pPlayer.experienceLevel < 1 && !pPlayer.isCreative())) {
                return false;
            } else {
                this.access.execute((level, tablePos) -> {
                    ItemStack itemstack2 = itemstack;
                    List<EnchantmentInstance> list = getChiselEnchantmentListICV(level, tablePos, itemstack);
                    if (!list.isEmpty()) {
                        pPlayer.onEnchantmentPerformed(itemstack, 1);
                        boolean flag = itemstack.is(Items.BOOK);
                        if (flag) {
                            itemstack2 = new ItemStack(Items.ENCHANTED_BOOK);
                            CompoundTag compoundtag = itemstack.getTag();
                            if (compoundtag != null) {
                                itemstack2.setTag(compoundtag.copy());
                            }
                            
                            this.enchantSlots.setItem(0, itemstack2);
                        }
                        
                        for (int j = 0; j < list.size(); ++j) {
                            EnchantmentInstance enchantmentinstance = list.get(pId);
                            if (flag) {
                                EnchantedBookItem.addEnchantment(itemstack2, enchantmentinstance);
                            } else {
                                itemstack2.enchant(enchantmentinstance.enchantment, 1);
                            }
                        }
                        
                        if (!pPlayer.getAbilities().instabuild) {
                            itemstack1.shrink(1);
                            if (itemstack1.isEmpty()) {
                                this.enchantSlots.setItem(1, ItemStack.EMPTY);
                            }
                        }
                        
                        pPlayer.awardStat(Stats.ENCHANT_ITEM);
                        if (pPlayer instanceof ServerPlayer) {
                            CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayer) pPlayer, itemstack2, 1);
                        }
                        
                        this.enchantSlots.setChanged();
                        this.enchantmentSeed.set(pPlayer.getEnchantmentSeed());
                        this.slotsChanged(this.enchantSlots);
                        level.playSound(null, tablePos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
                    }
                    
                });
                return true;
            }
        } else {
            if (this.localLength > 3) {
                if (pId == -1) {
                    if (this.localEnchShift + 3 < this.localLength) {
                        setLEnchShift(++localEnchShift);
                    }
                } else if (pId == -2 && this.localEnchShift > 0) {
                    setLEnchShift(--localEnchShift);
                }
            } else {
                setLEnchShift(0);
            }
            if (pPlayer instanceof ServerPlayer) {
                //ModMessages.sendToPlayer(new EnchTableUpdateS2CPacket(this.localEnchShift), (ServerPlayer) pPlayer);
                this.player = (ServerPlayer) pPlayer;
            }
            slotsChanged(enchantSlots);
            return false;
        }
    }
    
    /**
     * @author Igneo220
     * @reason Rewriting enchantment system
     */
    @Overwrite
    public void slotsChanged(Container pInventory) {
        if (this.player != null) {
            //ModMessages.sendToPlayer(new EnchTableUpdateS2CPacket(this.localEnchShift), (ServerPlayer) player);
        }
        updateEnchantmentListsICV();
        if (pInventory == this.enchantSlots) {
            ItemStack itemstack = pInventory.getItem(0);
            if (!itemstack.isEmpty() && itemstack.isEnchantable() && checkValidICV(itemstack)) {
                this.access.execute((level, tablePos) -> {
                    float j = 0;
                    for (int k = 0; k < 3; ++k) {
                        this.costs[k] = 1;
                    }
                    
                    for (int l = 0; l < 3; ++l) {
                        List<EnchantmentInstance> list = getChiselEnchantmentListICV(level, tablePos, itemstack);
                        if (list != null && !list.isEmpty()) {
                            if (l < list.size()) {
                                EnchantmentInstance enchantmentinstance = list.get(l + this.localEnchShift);
                                this.enchantClue[l] = BuiltInRegistries.ENCHANTMENT.getId(enchantmentinstance.enchantment);
                                this.levelClue[l] = enchantmentinstance.level;
                            } else {
                                this.enchantClue[l] = -1;
                            }
                        } else {
                            for (int i = 0; i < 3; ++i) {
                                this.costs[i] = 0;
                                this.enchantClue[i] = -1;
                                this.levelClue[i] = -1;
                            }
                            setLEnchShift(0);
                            setLLength(0);
                        }
                    }
                    
                    this.broadcastChanges();
                });
            } else {
                for (int i = 0; i < 3; ++i) {
                    this.costs[i] = 0;
                    this.enchantClue[i] = -1;
                    this.levelClue[i] = -1;
                }
                setLEnchShift(0);
                setLLength(0);
            }
        }
        
    }

    
    /**
     * @author Igneo220
     * @reason rewriting enchantment table
     */
    @Overwrite
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        setLEnchShift(0);
        if (pPlayer instanceof ServerPlayer) {
            //ModMessages.sendToPlayer(new EnchTableUpdateS2CPacket(this.localEnchShift), (ServerPlayer) pPlayer);
        }
        setLLength(0);
        this.access.execute((p_39469_, p_39470_) -> {
            this.clearContainer(pPlayer, this.enchantSlots);
        });
    }

}
