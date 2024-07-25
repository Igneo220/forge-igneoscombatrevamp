package net.igneo.icv.mixin;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.event.ModEvents;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin extends AbstractContainerMenu {
    @Unique
    private static List<Enchantment> HELM_ENCHANTS = new ArrayList<Enchantment>();
    @Unique
    private static List<Enchantment> CHEST_ENCHANTS = new ArrayList<Enchantment>();
    @Unique
    private static List<Enchantment> LEG_ENCHANTS = new ArrayList<Enchantment>();
    @Unique
    private static List<Enchantment> BOOT_ENCHANTS = new ArrayList<Enchantment>();
    @Unique
    private static List<Enchantment> WEAPON_ENCHANTS = new ArrayList<Enchantment>();
    @Unique
    private static List<Enchantment> BOW_ENCHANTS = new ArrayList<Enchantment>();
    @Unique
    private static List<Enchantment> CROSSBOW_ENCHANTS = new ArrayList<Enchantment>();
    @Unique
    private static List<Enchantment> TRIDENT_ENCHANTS = new ArrayList<Enchantment>();
    @Unique
    private static List<Enchantment> TOOL_ENCHANTS = new ArrayList<Enchantment>();
    @Unique
    private static List<EnchantmentInstance> unlockedEnchants = new ArrayList<>();
    @Shadow
    private final RandomSource random = RandomSource.create();
    @Shadow
    private final DataSlot enchantmentSeed = DataSlot.standalone();
    @Shadow
    public final int[] enchantClue = new int[]{-1, -1, -1};
    @Shadow
    public final int[] levelClue = new int[]{-1, -1, -1};
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
    protected EnchantmentMenuMixin(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(MenuType.ENCHANTMENT, pContainerId);
        this.access = pAccess;
        this.addSlot(new Slot(this.enchantSlots, 0, 15, 47) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean mayPlace(ItemStack p_39508_) {
                return true;
            }

            /**
             * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
             * case of armor slots)
             */
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.enchantSlots, 1, 35, 47) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean mayPlace(ItemStack p_39517_) {
                return p_39517_.is(net.minecraftforge.common.Tags.Items.ENCHANTING_FUELS);
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlot(DataSlot.shared(this.costs, 0));
        this.addDataSlot(DataSlot.shared(this.costs, 1));
        this.addDataSlot(DataSlot.shared(this.costs, 2));
    }

    @Override @Shadow
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override @Shadow
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
            if ((itemstack1.isEmpty() || itemstack1.getCount() < 1) && !pPlayer.getAbilities().instabuild) {
                System.out.println("fail check 1");
                return false;
            } else if (itemstack.isEmpty() || (pPlayer.experienceLevel < 2 || !pPlayer.getAbilities().instabuild) && !pPlayer.isCreative()) {
                System.out.println("fail check 2");
                return false;
            } else {
                this.access.execute((level, tablePos) -> {
                    ItemStack itemstack2 = itemstack;
                    List<EnchantmentInstance> list = this.getChiselEnchantmentList(level,tablePos,itemstack);
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
                            System.out.println("right here officer: " + pId);
                            EnchantmentInstance enchantmentinstance = list.get(pId);
                            if (flag) {
                                EnchantedBookItem.addEnchantment(itemstack2, enchantmentinstance);
                            } else {
                                itemstack2.enchant(enchantmentinstance.enchantment,1);
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
                            CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayer)pPlayer, itemstack2, 1);
                        }

                        this.enchantSlots.setChanged();
                        this.enchantmentSeed.set(pPlayer.getEnchantmentSeed());
                        this.slotsChanged(this.enchantSlots);
                        level.playSound((Player)null, tablePos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
                    }

                });
                return true;
            }
        } else {
            Util.logAndPauseIfInIde(pPlayer.getName() + " pressed invalid button id: " + pId);
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
        updateEnchantmentLists();
        if (pInventory == this.enchantSlots) {
            System.out.println("this code is running");
            ItemStack itemstack = pInventory.getItem(0);
            if (!itemstack.isEmpty() && itemstack.isEnchantable()) {
                this.access.execute((level, tablePos) -> {
                    float j = 0;
                    /*
                    for(BlockPos blockpos : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
                        //System.out.println(level.getBlockState(tablePos));
                        if (EnchantmentTableBlock.isValidBookShelf(level, tablePos, blockpos)) {
                            j += level.getBlockState(tablePos.offset(blockpos)).getEnchantPowerBonus(level, tablePos.offset(blockpos));
                        }
                    }*/

                    //this.random.setSeed((long)this.enchantmentSeed.get());


                    for(int k = 0; k < 3; ++k) {
                        this.costs[k] = 1;
                        //this.enchantClue[k] = -1;
                        //this.levelClue[k] = -1;
                        //if (this.costs[k] < k + 1) {
                            //this.costs[k] = 0;
                        //}
                        //this.costs[k] = net.minecraftforge.event.ForgeEventFactory.onEnchantmentLevelSet(level, tablePos, k, (int)j, itemstack, costs[k]);
                    }

                    for(int l = 0; l < 3; ++l) {
                        List<EnchantmentInstance> list = this.getChiselEnchantmentList(level,tablePos,itemstack);
                        if (list != null && !list.isEmpty()) {
                            if(l < list.size()) {
                                EnchantmentInstance enchantmentinstance = list.get(l + ModEvents.enchShift);
                                this.enchantClue[l] = BuiltInRegistries.ENCHANTMENT.getId(enchantmentinstance.enchantment);
                                this.levelClue[l] = enchantmentinstance.level;
                            } else {
                                this.enchantClue[l] = -1;
                            }
                        }
                    }

                    this.broadcastChanges();
                });
            } else {
                for(int i = 0; i < 3; ++i) {
                    this.costs[i] = 0;
                    this.enchantClue[i] = -1;
                    this.levelClue[i] = -1;
                }
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
        ModEvents.enchShift = 0;
        ModEvents.enchLength = 0;
        this.access.execute((p_39469_, p_39470_) -> {
            this.clearContainer(pPlayer, this.enchantSlots);
        });
    }

    @Unique
    public List<EnchantmentInstance> getChiselEnchantmentList(Level level, BlockPos tablePos, ItemStack enchItem) {
        //this.random.setSeed((long) (this.enchantmentSeed.get() + pEnchantSlot));
        List<EnchantmentInstance> list = new ArrayList<EnchantmentInstance>();
        for(BlockPos blockpos : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
            //System.out.println(level.getBlockState(tablePos));
            if (level.getBlockState(tablePos.offset(blockpos)).getBlock().equals(Blocks.CHISELED_BOOKSHELF)) {
                ChiseledBookShelfBlockEntity bookShelf = (ChiseledBookShelfBlockEntity) level.getBlockEntity(tablePos.offset(blockpos));
                int i = 5;
                while (i > -1) {
                    if (!bookShelf.getItem(i).isEmpty()) {
                        for (Enchantment enchantment : EnchantmentHelper.getEnchantments(bookShelf.getItem(i)).keySet()) {
                            if (checkValidEquipmentSlot(enchItem,enchantment)) {
                                boolean add = true;
                                for (EnchantmentInstance enchantment1 : list) {
                                    if (enchantment1.enchantment.equals(enchantment)) {
                                        add = false;
                                        break;
                                    }
                                }
                                if (add) {
                                    list.add(new EnchantmentInstance(enchantment, 1));
                                }
                            }
                        }
                    }
                    --i;
                }
            }
        }
        if (list.size() != ModEvents.enchLength) {
            ModEvents.enchShift = 0;
        }
        ModEvents.enchLength = list.size();
        return list;
    }
    private boolean checkValidEquipmentSlot(ItemStack pStack, Enchantment enchantment) {
        if (pStack.isEnchantable()) {
            if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.MAINHAND)) {
                if (pStack.getItem().equals(Items.BOW)) {
                    boolean add = false;
                    for (Enchantment checkEnchant : BOW_ENCHANTS) {
                        if (enchantment == checkEnchant) {
                            add = true;
                        }
                    }
                    return add;
                } else if (pStack.getItem().equals(Items.CROSSBOW)) {
                    boolean add = false;
                    for (Enchantment checkEnchant : CROSSBOW_ENCHANTS) {
                        if (enchantment == checkEnchant) {
                            add = true;
                        }
                    }
                    return add;
                } else if (pStack.getItem().equals(Items.TRIDENT)) {
                    boolean add = false;
                    for (Enchantment checkEnchant : TRIDENT_ENCHANTS) {
                        if (enchantment == checkEnchant) {
                            add = true;
                        }
                    }
                    return add;
                } else if (pStack.getItem().toString().contains("sword") ||
                        pStack.getItem().toString().contains("scythe") ||
                        pStack.getItem().toString().contains("glaive") ||
                        pStack.getItem().toString().contains("halberd") ||
                        pStack.getItem().toString().contains("hammer") ||
                        pStack.getItem().toString().contains("rapier") ||
                        pStack.getItem().toString().contains("spear") ||
                        pStack.getItem().toString().contains("katana") ||
                        pStack.getItem().toString().contains("mace")) {
                    boolean add = false;
                    for (Enchantment checkEnchant : WEAPON_ENCHANTS) {
                        if (enchantment == checkEnchant) {
                            add = true;
                        }
                    }
                    return add;
                } else if (pStack.getItem().toString().contains("axe") ||
                        pStack.getItem().toString().contains("hoe") ||
                        pStack.getItem().toString().contains("shovel")) {
                    boolean add = false;
                    for (Enchantment checkEnchant : TOOL_ENCHANTS) {
                        if (enchantment == checkEnchant) {
                            add = true;
                        }
                    }
                    return add;
                }
            } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.HEAD)) {
                boolean add = false;
                for (Enchantment checkEnchant : HELM_ENCHANTS) {
                    if (enchantment == checkEnchant) {
                        add = true;
                    }
                }
                return add;
            } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.CHEST)) {
                boolean add = false;
                for (Enchantment checkEnchant : CHEST_ENCHANTS) {
                    if (enchantment == checkEnchant) {
                        add = true;
                    }
                }
                return add;
            } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.LEGS)) {
                boolean add = false;
                for (Enchantment checkEnchant : LEG_ENCHANTS) {
                    if (enchantment == checkEnchant) {
                        add = true;
                    }
                }
                return add;
            } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.FEET)) {
                boolean add = false;
                for (Enchantment checkEnchant : BOOT_ENCHANTS) {
                    if (enchantment == checkEnchant) {
                        add = true;
                    }
                }
                return add;
            }
        }
        return false;
    }
    @Unique
    private void updateEnchantmentLists() {
        if (HELM_ENCHANTS.isEmpty()) {
            HELM_ENCHANTS.add(ModEnchantments.BLACK_HOLE.get());
            HELM_ENCHANTS.add(ModEnchantments.BLIZZARD.get());
            HELM_ENCHANTS.add(ModEnchantments.FLAMETHROWER.get());
            HELM_ENCHANTS.add(ModEnchantments.SMITE.get());
            HELM_ENCHANTS.add(ModEnchantments.WARDEN_SCREAM.get());
        }
        if (CHEST_ENCHANTS.isEmpty()) {
            CHEST_ENCHANTS.add(ModEnchantments.CONCUSSION.get());
            CHEST_ENCHANTS.add(ModEnchantments.FLARE.get());
            CHEST_ENCHANTS.add(ModEnchantments.PARRY.get());
            CHEST_ENCHANTS.add(ModEnchantments.SIPHON.get());
            CHEST_ENCHANTS.add(ModEnchantments.WARDENSPINE.get());
        }
        if (LEG_ENCHANTS.isEmpty()) {
            LEG_ENCHANTS.add(ModEnchantments.ACROBATIC.get());
            LEG_ENCHANTS.add(ModEnchantments.CRUSH.get());
            LEG_ENCHANTS.add(ModEnchantments.INCAPACITATE.get());
            LEG_ENCHANTS.add(ModEnchantments.JUDGEMENT.get());
            LEG_ENCHANTS.add(ModEnchantments.TRAIN_DASH.get());
        }
        if (BOOT_ENCHANTS.isEmpty()) {
            BOOT_ENCHANTS.add(ModEnchantments.COMET_STRIKE.get());
            BOOT_ENCHANTS.add(ModEnchantments.DOUBLE_JUMP.get());
            BOOT_ENCHANTS.add(ModEnchantments.MOMENTUM.get());
            BOOT_ENCHANTS.add(ModEnchantments.SKY_CHARGE.get());
            BOOT_ENCHANTS.add(ModEnchantments.STONE_CALLER.get());
        }
        if (WEAPON_ENCHANTS.isEmpty()) {
            WEAPON_ENCHANTS.add(ModEnchantments.BLITZ.get());
            WEAPON_ENCHANTS.add(ModEnchantments.BREAKTHROUGH.get());
            WEAPON_ENCHANTS.add(ModEnchantments.COUNTERWEIGHTED.get());
            WEAPON_ENCHANTS.add(ModEnchantments.GUST.get());
            WEAPON_ENCHANTS.add(ModEnchantments.KINETIC.get());
            WEAPON_ENCHANTS.add(ModEnchantments.PHANTOM_PAIN.get());
            WEAPON_ENCHANTS.add(ModEnchantments.SKEWERING.get());
            WEAPON_ENCHANTS.add(ModEnchantments.TEMPO_THEFT.get());
        }
        if (BOW_ENCHANTS.isEmpty()) {
            BOW_ENCHANTS.add(ModEnchantments.ACCELERATE.get());
            BOW_ENCHANTS.add(ModEnchantments.PHASING.get());
            BOW_ENCHANTS.add(ModEnchantments.WHISTLER.get());
        }
        if (CROSSBOW_ENCHANTS.isEmpty()) {
            CROSSBOW_ENCHANTS.add(ModEnchantments.MITOSIS.get());
            CROSSBOW_ENCHANTS.add(ModEnchantments.REND.get());
            CROSSBOW_ENCHANTS.add(ModEnchantments.SCATTER.get());
        }
        if (TRIDENT_ENCHANTS.isEmpty()) {
            TRIDENT_ENCHANTS.add(ModEnchantments.EXTRACT.get());
            TRIDENT_ENCHANTS.add(ModEnchantments.RECOIL.get());
            TRIDENT_ENCHANTS.add(Enchantments.RIPTIDE);
        }
        if (TOOL_ENCHANTS.isEmpty()) {
            TOOL_ENCHANTS.add(Enchantments.SILK_TOUCH);
            TOOL_ENCHANTS.add(ModEnchantments.BRUTE_TOUCH.get());
        }
    }
}
