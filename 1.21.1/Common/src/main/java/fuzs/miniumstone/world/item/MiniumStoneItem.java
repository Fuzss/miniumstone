package fuzs.miniumstone.world.item;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
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
    public static final String TAG_CHARGE = MiniumStone.id("charge").toString();
    public static final String TAG_SELECTION = MiniumStone.id("selection").toString();

    public MiniumStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.isSecondaryUseActive()) {
            ItemStack itemInHand = player.getItemInHand(usedHand);
            if (!level.isClientSide) {
                cycleSelectionMode(itemInHand);
                player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".changedSelection",
                        getSelectionMode(itemInHand).getComponent()
                ), true);
            }
            return InteractionResultHolder.sidedSuccess(itemInHand, level.isClientSide);
        } else {
            return super.use(level, player, usedHand);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (level != null) {
            tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".selection",
                    getSelectionMode(stack).getComponent()
            ).withStyle(ChatFormatting.GRAY));
            if (!Proxy.INSTANCE.hasShiftDown()) {
                tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".more",
                        Component.translatable(this.getDescriptionId() + ".shift").withStyle(ChatFormatting.YELLOW)
                ).withStyle(ChatFormatting.GRAY));
            } else {
                Component sneak = Component.keybind("key.sneak").withStyle(ChatFormatting.LIGHT_PURPLE);
                Component use = Component.keybind("key.use").withStyle(ChatFormatting.LIGHT_PURPLE);
                tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".changeSelection", sneak, use)
                        .withStyle(ChatFormatting.GRAY));
                Component chargeMiniumStone = Component.keybind("key.chargeMiniumStone")
                        .withStyle(ChatFormatting.LIGHT_PURPLE);
                tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".charge", chargeMiniumStone)
                        .withStyle(ChatFormatting.GRAY));
                Component openCraftingGrid = Component.keybind("key.openCraftingGrid")
                        .withStyle(ChatFormatting.LIGHT_PURPLE);
                tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".crafting", openCraftingGrid)
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public static void cycleSelectionMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putByte(TAG_SELECTION, (byte) ((tag.getByte(TAG_SELECTION) + 1) % SelectionMode.values().length));
    }

    public static SelectionMode getSelectionMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        byte ordinal = tag != null ? tag.getByte(TAG_SELECTION) : 0;
        return SelectionMode.values()[ordinal];
    }

    @Override
    public boolean ignoreTagWhenMoving() {
        return true;
    }

    @Override
    public boolean supportsMultipleCraftingOperations() {
        return true;
    }

    public static boolean increaseCharge(ItemStack stack) {
        return setCharge(stack, getCharge(stack) + 1);
    }

    private static boolean setCharge(ItemStack stack, int charge) {
        charge = Mth.clamp(charge, 0, 3);
        CompoundTag tag = stack.getOrCreateTag();
        if (charge != tag.getByte(TAG_CHARGE)) {
            tag.putByte(TAG_CHARGE, (byte) charge);
            return true;
        }
        return false;
    }

    public static int getCharge(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getByte(TAG_CHARGE) : 0;
    }

    public static boolean decreaseCharge(ItemStack stack) {
        return setCharge(stack, getCharge(stack) - 1);
    }

    public enum SelectionMode implements StringRepresentable {
        FLAT, CUBE, LINE;

        public Component getComponent() {
            String translationKey = ModRegistry.MINIUM_STONE_ITEM.value()
                    .getDescriptionId() + "." + this.getSerializedName();
            return Component.translatable(translationKey).withStyle(ChatFormatting.GOLD);
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
