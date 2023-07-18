package fuzs.miniumstone.world.item;

import fuzs.puzzleslib.api.core.v1.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MiniumStoneItem extends Item implements SpecialRecipePickerItem {
    public static final String KEY_CHARGE = "Charge";

    public MiniumStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        Component keyMappingComponent = Component.empty().append(Proxy.INSTANCE.getKeyMappingComponent("key.openCraftingGrid")).withStyle(ChatFormatting.LIGHT_PURPLE);
        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".description", keyMappingComponent));
    }

    public static int getCharge(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(KEY_CHARGE)) {
            return tag.getByte(KEY_CHARGE);
        }
        return 0;
    }

    public static boolean increaseCharge(ItemStack stack) {
        return setCharge(stack, getCharge(stack) + 1);
    }

    public static boolean decreaseCharge(ItemStack stack) {
        return setCharge(stack, getCharge(stack) - 1);
    }

    private static boolean setCharge(ItemStack stack, int charge) {
        charge = Mth.clamp(charge, 0, 3);
        CompoundTag tag = stack.getOrCreateTag();
        if (charge != tag.getByte(KEY_CHARGE)) {
            tag.putByte(KEY_CHARGE, (byte) charge);
            return true;
        }
        return false;
    }

    @Override
    public boolean ignoreTagWhenMoving() {
        return true;
    }

    @Override
    public boolean supportsMultipleCraftingOperations() {
        return true;
    }
}
