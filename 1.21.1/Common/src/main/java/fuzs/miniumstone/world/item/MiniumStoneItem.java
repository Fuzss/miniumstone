package fuzs.miniumstone.world.item;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.Proxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
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

import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;

public class MiniumStoneItem extends Item {

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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (context != TooltipContext.EMPTY) {
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
                Component chargeMiniumStone = Component.keybind("key.charge_minium_stone").withStyle(
                        ChatFormatting.LIGHT_PURPLE);
                tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".charge", chargeMiniumStone)
                        .withStyle(ChatFormatting.GRAY));
                Component openCraftingGrid = Component.keybind("key.open_crafting_grid").withStyle(
                        ChatFormatting.LIGHT_PURPLE);
                tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".crafting", openCraftingGrid)
                        .withStyle(ChatFormatting.GRAY));
            }
        }
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
        public static final StreamCodec<ByteBuf, SelectionMode> STREAM_CODEC = fromEnum(SelectionMode.class);

        static <E extends Enum<E>> StreamCodec<ByteBuf, E> fromEnum(Class<E> clazz) {
            IntFunction<E> idMapper = ByIdMap.continuous(E::ordinal, clazz.getEnumConstants(),
                    ByIdMap.OutOfBoundsStrategy.ZERO
            );
            return ByteBufCodecs.idMapper(idMapper, E::ordinal);
        }

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
