package fuzs.miniumstone.world.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MiniumStoneItem extends Item {
    public static final String KEY_CHARGE = "Charge";

    public MiniumStoneItem(Properties properties) {
        super(properties);
    }

    public static int getCharge(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(KEY_CHARGE)) {
            return tag.getByte(KEY_CHARGE);
        }
        return 1;
    }

    public static boolean increaseCharge(ItemStack stack) {
        return setCharge(stack, getCharge(stack) + 1);
    }

    public static boolean decreaseCharge(ItemStack stack) {
        return setCharge(stack, getCharge(stack) - 1);
    }

    private static boolean setCharge(ItemStack stack, int charge) {
        charge = Mth.clamp(charge, 1, 4);
        CompoundTag tag = stack.getOrCreateTag();
        if (charge != tag.getByte(KEY_CHARGE)) {
            tag.putByte(KEY_CHARGE, (byte) charge);
            return true;
        }
        return false;
    }
}
