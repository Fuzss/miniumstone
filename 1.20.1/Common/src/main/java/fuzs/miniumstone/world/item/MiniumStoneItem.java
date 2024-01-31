package fuzs.miniumstone.world.item;

import fuzs.miniumstone.MiniumStone;
import fuzs.puzzleslib.api.core.v1.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class MiniumStoneItem extends Item implements SpecialRecipePickerItem {
    public static final String KEY_CHARGE = "Charge";
    public static final String KEY_SELECTION = "Selection";

    public MiniumStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.isShiftKeyDown()) {
            ItemStack itemInHand = player.getItemInHand(usedHand);
            if (!level.isClientSide) {
                cycleSelectionMode(itemInHand);
                player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".changedSelection", getSelectionMode(itemInHand).component), true);
            }
            return InteractionResultHolder.sidedSuccess(itemInHand, level.isClientSide);
        } else {
            return super.use(level, player, usedHand);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".selection", getSelectionMode(stack).component).withStyle(ChatFormatting.GRAY));
        Component sneak = Component.empty().append(Proxy.INSTANCE.getKeyMappingComponent("key.sneak")).withStyle(ChatFormatting.LIGHT_PURPLE);
        Component use = Component.empty().append(Proxy.INSTANCE.getKeyMappingComponent("key.use")).withStyle(ChatFormatting.LIGHT_PURPLE);
        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".changeSelection", sneak, use).withStyle(ChatFormatting.GRAY));
        Component chargeMiniumStone = Component.empty().append(Proxy.INSTANCE.getKeyMappingComponent("key.chargeMiniumStone")).withStyle(ChatFormatting.LIGHT_PURPLE);
        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".charge", chargeMiniumStone).withStyle(ChatFormatting.GRAY));
        Component openCraftingGrid = Component.empty().append(Proxy.INSTANCE.getKeyMappingComponent("key.openCraftingGrid")).withStyle(ChatFormatting.LIGHT_PURPLE);
        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".crafting", openCraftingGrid).withStyle(ChatFormatting.GRAY));
    }

    public static int getCharge(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getByte(KEY_CHARGE) : 0;
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

    public static SelectionMode getSelectionMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        byte ordinal = tag != null ? tag.getByte(KEY_SELECTION) : 0;
        return SelectionMode.values()[ordinal];
    }

    public static void cycleSelectionMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putByte(KEY_SELECTION, (byte) ((tag.getByte(KEY_SELECTION) + 1) % SelectionMode.values().length));
    }

    @Override
    public boolean ignoreTagWhenMoving() {
        return true;
    }

    @Override
    public boolean supportsMultipleCraftingOperations() {
        return true;
    }

    public enum SelectionMode {
        FLAT, CUBE, LINE;

        public final String translationKey;
        public final Component component;

        SelectionMode() {
            this.translationKey = "item." + MiniumStone.MOD_ID + ".minium_stone." + this.name().toLowerCase(Locale.ROOT);
            this.component = Component.translatable(this.translationKey).withStyle(ChatFormatting.GOLD);
        }
    }
}
