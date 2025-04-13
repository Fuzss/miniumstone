package fuzs.miniumstone.world.item;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import fuzs.puzzleslib.api.util.v1.InteractionResultHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class MiniumStoneItem extends Item {

    public MiniumStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCraftingRemainder() {
        // cannot set this in item properties, as item instance has not been constructed
        // we also do not necessarily need this, as our recipes handle the minium stone item separately,
        // but a bug remains where the recipe book will forget the minium stone item is still there
        // after every crafting operation, until the inventory is updated (by clicking any slot)
        return new ItemStack(this);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResultHelper.sidedSuccess(context.getLevel().isClientSide);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (player.isSecondaryUseActive()) {
            ItemStack itemInHand = player.getItemInHand(usedHand);
            if (!level.isClientSide) {
                cycleSelectionMode(itemInHand);
                player.displayClientMessage(Component.translatable(this.getChangedSelectionTranslationKey(),
                        getSelectionMode(itemInHand).getComponent()), true);
            }
            return InteractionResultHelper.sidedSuccess(itemInHand, level.isClientSide);
        } else {
            return super.use(level, player, usedHand);
        }
    }

    public String getChangedSelectionTranslationKey() {
        return this.getDescriptionId() + ".tooltip.changed_selection";
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    public static void cycleSelectionMode(ItemStack itemStack) {
        itemStack.set(ModRegistry.SELECTION_DATA_COMPONENT_TYPE.value(), getSelectionMode(itemStack).cycle());
    }

    public static SelectionMode getSelectionMode(ItemStack itemStack) {
        return itemStack.get(ModRegistry.SELECTION_DATA_COMPONENT_TYPE.value());
    }

    public static boolean increaseCharge(ItemStack itemStack) {
        return setCharge(itemStack, getCharge(itemStack) + 1);
    }

    public static boolean decreaseCharge(ItemStack itemStack) {
        return setCharge(itemStack, getCharge(itemStack) - 1);
    }

    private static boolean setCharge(ItemStack itemStack, int charge) {
        byte newCharge = (byte) Mth.clamp(charge, 0, 3);
        byte oldCharge = itemStack.get(ModRegistry.CHARGE_DATA_COMPONENT_TYPE.value());
        if (newCharge != oldCharge) {
            itemStack.set(ModRegistry.CHARGE_DATA_COMPONENT_TYPE.value(), newCharge);
            return true;
        } else {
            return false;
        }
    }

    public static int getCharge(ItemStack itemStack) {
        return itemStack.get(ModRegistry.CHARGE_DATA_COMPONENT_TYPE.value());
    }

    public enum SelectionMode implements StringRepresentable {
        FLAT,
        CUBE,
        LINE;

        private static final SelectionMode[] VALUES = SelectionMode.values();
        public static final StringRepresentable.StringRepresentableCodec<SelectionMode> CODEC = StringRepresentable.fromEnum(
                SelectionMode::values);
        public static final StreamCodec<ByteBuf, SelectionMode> STREAM_CODEC = ExtraStreamCodecs.fromEnum(SelectionMode.class);

        public Component getComponent() {
            String translationKey =
                    ModRegistry.MINIUM_STONE_ITEM.value().getDescriptionId() + "." + this.getSerializedName();
            return Component.translatable(translationKey).withStyle(ChatFormatting.GOLD);
        }

        public SelectionMode cycle() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
