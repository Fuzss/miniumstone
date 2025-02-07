package fuzs.miniumstone.integration.rei;

import com.google.common.collect.Lists;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Copied from {@code me.shedaniel.rei.plugin.client.categories.DefaultStoneCuttingCategory}.
 */
public class TransmutationInWorldCategory implements DisplayCategory<TransmutationInWorldDisplay> {

    @Override
    public CategoryIdentifier<? extends TransmutationInWorldDisplay> getCategoryIdentifier() {
        return TransmutationInWorldDisplay.CATEGORY;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModRegistry.MINIUM_STONE_ITEM.value());
    }

    @Override
    public Component getTitle() {
        return TransmutationInWorldRecipe.TRANSMUTATION_IN_WORLD_COMPONENT;
    }

    @Override
    public List<Widget> setupDisplay(TransmutationInWorldDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
                .entries(display.getOutputEntries().getFirst())
                .disableBackground()
                .markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5))
                .entries(display.getInputEntries().getFirst())
                .markInput());
        if (display.isReversible()) {
            Widget widget = Widgets.createShapelessIcon(bounds);
//            Widgets.withTooltip())
            widgets.add(widget);
        }
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }
}
