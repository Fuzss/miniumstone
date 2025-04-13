package fuzs.miniumstone.client.util;

import fuzs.miniumstone.client.handler.MiniumStoneKeyHandler;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.Locale;
import java.util.function.Consumer;

public class MiniumStoneTooltipHelper {

    public static void appendHoverText(MiniumStoneItem item, ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, Consumer<Component> tooltipLineConsumer) {
        tooltipLineConsumer.accept(Component.translatable(TooltipComponent.SELECTION.getTranslationKey(),
                MiniumStoneItem.getSelectionMode(itemStack).getComponent()).withStyle(ChatFormatting.GRAY));
        if (!Screen.hasShiftDown()) {
            tooltipLineConsumer.accept(Component.translatable(TooltipComponent.ADDITIONAL.getTranslationKey(),
                            Component.translatable(TooltipComponent.SHIFT.getTranslationKey()).withStyle(ChatFormatting.YELLOW))
                    .withStyle(ChatFormatting.GRAY));
        } else {
            Options options = Minecraft.getInstance().options;
            Component sneakComponent = Component.keybind(options.keyShift.getName())
                    .withStyle(ChatFormatting.LIGHT_PURPLE);
            Component useComponent = Component.keybind(options.keyUse.getName()).withStyle(ChatFormatting.LIGHT_PURPLE);
            tooltipLineConsumer.accept(Component.translatable(TooltipComponent.CHANGE_SELECTION.getTranslationKey(),
                    sneakComponent,
                    useComponent).withStyle(ChatFormatting.GRAY));
            Component chargeMiniumStoneComponent = Component.keybind(MiniumStoneKeyHandler.CHARGE_MINIUM_STONE_KEY_MAPPING.getName())
                    .withStyle(ChatFormatting.LIGHT_PURPLE);
            tooltipLineConsumer.accept(Component.translatable(TooltipComponent.CHARGE.getTranslationKey(),
                    chargeMiniumStoneComponent).withStyle(ChatFormatting.GRAY));
            Component openCraftingGridComponent = Component.keybind(MiniumStoneKeyHandler.OPEN_CRAFTING_GRID_KEY_MAPPING.getName())
                    .withStyle(ChatFormatting.LIGHT_PURPLE);
            tooltipLineConsumer.accept(Component.translatable(TooltipComponent.CRAFTING.getTranslationKey(),
                    openCraftingGridComponent).withStyle(ChatFormatting.GRAY));
        }
    }

    public enum TooltipComponent implements StringRepresentable {
        ADDITIONAL,
        SHIFT,
        SELECTION,
        CHANGE_SELECTION,
        CHARGE,
        CRAFTING;

        public String getTranslationKey() {
            return ModRegistry.MINIUM_STONE_ITEM.value().getDescriptionId() + ".tooltip." + this.getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
