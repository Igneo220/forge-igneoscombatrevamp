package net.igneo.icv.ForMixins;

import net.igneo.icv.enchantment.ModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class ForEnchantmentMenuMixin {

        private static List<Enchantment> HELM_ENCHANTS = new ArrayList<Enchantment>();

        private static List<Enchantment> CHEST_ENCHANTS = new ArrayList<Enchantment>();

        private static List<Enchantment> LEG_ENCHANTS = new ArrayList<Enchantment>();

        private static List<Enchantment> BOOT_ENCHANTS = new ArrayList<Enchantment>();

        private static List<Enchantment> WEAPON_ENCHANTS = new ArrayList<Enchantment>();

        private static List<Enchantment> BOW_ENCHANTS = new ArrayList<Enchantment>();

        private static List<Enchantment> CROSSBOW_ENCHANTS = new ArrayList<Enchantment>();

        private static List<Enchantment> TRIDENT_ENCHANTS = new ArrayList<Enchantment>();

        private static List<Enchantment> TOOL_ENCHANTS = new ArrayList<Enchantment>();

        private static int localEnchShift = 0;

        public static int returnLEnchShift(){
            return localEnchShift;
        }
        public static void setLEnchShift(int pSet){
            localEnchShift = pSet;
        }

        private static int localLength = 0;

        public static int returnLLength(){
            return localLength;
        }
        public static void setLLength(int pSet){
            localLength = pSet;
        }

    public static boolean checkValidICV(ItemStack pStack){
        return checkValid(pStack);
    }

    private static boolean checkValid(ItemStack pStack) {
        boolean valid = false;
        if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.MAINHAND)) {
            if (pStack.getItem().equals(Items.BOW)) {
                valid = true;
            } else if (pStack.getItem().equals(Items.CROSSBOW)) {
                valid = true;
            } else if (pStack.getItem().equals(Items.TRIDENT)) {
                valid = true;
            } else if (pStack.getItem().toString().contains("sword") ||
                    pStack.getItem().toString().contains("scythe") ||
                    pStack.getItem().toString().contains("glaive") ||
                    pStack.getItem().toString().contains("halberd") ||
                    pStack.getItem().toString().contains("hammer") ||
                    pStack.getItem().toString().contains("rapier") ||
                    pStack.getItem().toString().contains("spear") ||
                    pStack.getItem().toString().contains("katana") ||
                    pStack.getItem().toString().contains("mace")) {
                valid = true;
            } else if (pStack.getItem().toString().contains("axe") ||
                    pStack.getItem().toString().contains("hoe") ||
                    pStack.getItem().toString().contains("shovel")) {
                valid = true;
            }
        } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.HEAD)) {
            valid = true;
        } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.CHEST)) {
            valid = true;
        } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.LEGS)) {
            valid = true;
        } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.FEET)) {
            valid = true;
        }
        return valid;
    }

    public static List<EnchantmentInstance> getChiselEnchantmentListICV(Level level, BlockPos tablePos, ItemStack enchItem){
        return getChiselEnchantmentList(level, tablePos, enchItem);

    }

    public static List<EnchantmentInstance> getChiselEnchantmentList(Level level, BlockPos tablePos, ItemStack enchItem) {
        //this.random.setSeed((long) (this.enchantmentSeed.get() + pEnchantSlot));
        List<EnchantmentInstance> list = new ArrayList<EnchantmentInstance>();
        for (BlockPos blockpos : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
            if (level.getBlockState(tablePos.offset(blockpos)).getBlock().equals(Blocks.CHISELED_BOOKSHELF)) {
                ChiseledBookShelfBlockEntity bookShelf = (ChiseledBookShelfBlockEntity) level.getBlockEntity(tablePos.offset(blockpos));
                int i = 5;
                while (i > -1) {
                    if (!bookShelf.getItem(i).isEmpty()) {
                        for (Enchantment enchantment : EnchantmentHelper.getEnchantments(bookShelf.getItem(i)).keySet()) {
                            if (checkValidEquipmentSlotICV(enchItem, enchantment)) {
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
        if (list.size() != returnLLength()) {
            setLEnchShift(0);
        }
        setLLength(list.size());
        return list;
    }

    public static boolean checkValidEquipmentSlotICV(ItemStack pStack, Enchantment enchantment){
            return checkValidEquipmentSlot(pStack,enchantment);
    }


    private static boolean checkValidEquipmentSlot(ItemStack pStack, Enchantment enchantment) {
        if (pStack.isEnchantable() && checkValidICV(pStack)) {
            if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.MAINHAND)) {
                if (pStack.getItem().equals(Items.BOW)) {
                    boolean add = false;
                    for (Enchantment checkEnchant : BOW_ENCHANTS) {
                        if (enchantment == checkEnchant) {
                            add = true;
                            break;
                        }
                    }
                    return add;
                } else if (pStack.getItem().equals(Items.CROSSBOW)) {
                    boolean add = false;
                    for (Enchantment checkEnchant : CROSSBOW_ENCHANTS) {
                        if (enchantment == checkEnchant) {
                            add = true;
                            break;
                        }
                    }
                    return add;
                } else if (pStack.getItem().equals(Items.TRIDENT)) {
                    boolean add = false;
                    for (Enchantment checkEnchant : TRIDENT_ENCHANTS) {
                        if (enchantment == checkEnchant) {
                            add = true;
                            break;
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
                            break;
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
                            break;
                        }
                    }
                    return add;
                }
            } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.HEAD)) {
                boolean add = false;
                for (Enchantment checkEnchant : HELM_ENCHANTS) {
                    if (enchantment == checkEnchant) {
                        add = true;
                        break;
                    }
                }
                return add;
            } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.CHEST)) {
                boolean add = false;
                for (Enchantment checkEnchant : CHEST_ENCHANTS) {
                    if (enchantment == checkEnchant) {
                        add = true;
                        break;
                    }
                }
                return add;
            } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.LEGS)) {
                boolean add = false;
                for (Enchantment checkEnchant : LEG_ENCHANTS) {
                    if (enchantment == checkEnchant) {
                        add = true;
                        break;
                    }
                }
                return add;
            } else if (LivingEntity.getEquipmentSlotForItem(pStack).equals(EquipmentSlot.FEET)) {
                boolean add = false;
                for (Enchantment checkEnchant : BOOT_ENCHANTS) {
                    if (enchantment == checkEnchant) {
                        add = true;
                        break;
                    }
                }
                return add;
            }
        }
        return false;
    }

    public static void updateEnchantmentListsICV(){
        updateEnchantmentLists();
    }

    private static void updateEnchantmentLists() {
            /*
        if (HELM_ENCHANTS.isEmpty()) {
            HELM_ENCHANTS.add(ModEnchantments.BLACK_HOLE.get());
            HELM_ENCHANTS.add(ModEnchantments.WARDEN_SCREAM.get());
            HELM_ENCHANTS.add(ModEnchantments.BLIZZARD.get());
            HELM_ENCHANTS.add(ModEnchantments.FLAMETHROWER.get());
            HELM_ENCHANTS.add(ModEnchantments.SMITE.get());
        }
        if (CHEST_ENCHANTS.isEmpty()) {
            CHEST_ENCHANTS.add(ModEnchantments.CONCUSSION.get());
            CHEST_ENCHANTS.add(ModEnchantments.FLARE.get());
            CHEST_ENCHANTS.add(ModEnchantments.PARRY.get());
            CHEST_ENCHANTS.add(ModEnchantments.PLANAR_SHIFT.get());
            CHEST_ENCHANTS.add(ModEnchantments.WARDENSPINE.get());
        }
        if (LEG_ENCHANTS.isEmpty()) {
            LEG_ENCHANTS.add(ModEnchantments.TEMPEST.get());
            LEG_ENCHANTS.add(ModEnchantments.CRUSH.get());
            LEG_ENCHANTS.add(ModEnchantments.INCAPACITATE.get());
            LEG_ENCHANTS.add(ModEnchantments.JUDGEMENT.get());
            LEG_ENCHANTS.add(ModEnchantments.TRAIN_DASH.get());
        }
        if (BOOT_ENCHANTS.isEmpty()) {
            BOOT_ENCHANTS.add(ModEnchantments.COMET_STRIKE.get());
            BOOT_ENCHANTS.add(ModEnchantments.DOUBLE_JUMP.get());
            BOOT_ENCHANTS.add(ModEnchantments.MOMENTUM.get());
            BOOT_ENCHANTS.add(ModEnchantments.STONE_CALLER.get());
        }
        if (WEAPON_ENCHANTS.isEmpty()) {
            WEAPON_ENCHANTS.add(ModEnchantments.BURST.get());
            WEAPON_ENCHANTS.add(ModEnchantments.BREAKTHROUGH.get());
            WEAPON_ENCHANTS.add(ModEnchantments.FINESSE.get());
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
        */
    }
}

