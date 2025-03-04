package net.igneo.icv.enchantment;

public enum EnchantType {
    WEAPON,
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    BOW,
    CROSSBOW,
    SHIELD,
    TRIDENT;

    public static boolean applicableSlot(EnchantType type, int slot) {
        switch (type) {
            case BOOTS: {
                return slot == 0;
            }
            case LEGGINGS: {
                return slot == 1;
            }
            case CHESTPLATE: {
                return slot == 2;
            }
            case HELMET: {
                return slot == 3;
            }
            case WEAPON: {
                return slot == 4 || slot == 5;
            }
        }
        return false;
    }
}
