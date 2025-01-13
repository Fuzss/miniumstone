package fuzs.miniumstone.integration.rei;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Optional;

/**
 * Copied from {@code me.shedaniel.rei.plugin.common.displays.DefaultStoneCuttingDisplay}.
 */
public class TransmutationInWorldDisplay extends BasicDisplay {
    public static final CategoryIdentifier<TransmutationInWorldDisplay> CATEGORY = CategoryIdentifier.of(MiniumStone.id(
            "plugins/transmutation_in_world"));
    public static final DisplaySerializer<TransmutationInWorldDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(EntryIngredient.codec()
                                    .listOf()
                                    .fieldOf("inputs")
                                    .forGetter(TransmutationInWorldDisplay::getInputEntries),
                            EntryIngredient.codec()
                                    .listOf()
                                    .fieldOf("outputs")
                                    .forGetter(TransmutationInWorldDisplay::getOutputEntries),
                            ResourceLocation.CODEC.optionalFieldOf("location")
                                    .forGetter(TransmutationInWorldDisplay::getDisplayLocation),
                            Codec.BOOL.fieldOf("is_reversible").forGetter(TransmutationInWorldDisplay::isReversible))
                    .apply(instance, TransmutationInWorldDisplay::new)),
            StreamCodec.composite(EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    TransmutationInWorldDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    TransmutationInWorldDisplay::getOutputEntries,
                    ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
                    TransmutationInWorldDisplay::getDisplayLocation,
                    ByteBufCodecs.BOOL,
                    TransmutationInWorldDisplay::isReversible,
                    TransmutationInWorldDisplay::new));

    private final boolean isReversible;

    public TransmutationInWorldDisplay(RecipeHolder<TransmutationInWorldRecipe> recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.value().input())),
                List.of(EntryIngredients.of(recipe.value().result())),
                Optional.of(recipe.id().location()),
                recipe.value().isReversible());
    }

    public TransmutationInWorldDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location, boolean isReversible) {
        super(inputs, outputs, location);
        this.isReversible = isReversible;
    }

    public boolean isReversible() {
        return this.isReversible;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CATEGORY;
    }

    @Override
    public DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }
}

