package fuzs.miniumstone.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.crafting.display.BlockSlotDisplay;
import fuzs.puzzleslib.api.data.v2.AbstractRegistriesDatapackGenerator;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Function;

public final class TransmutationInWorldRecipe extends SingleItemRecipe {
    public static final Component TRANSMUTATION_IN_WORLD_COMPONENT = AbstractRegistriesDatapackGenerator.getComponent(
            ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.key());

    private final boolean reversible;

    public TransmutationInWorldRecipe(String group, Block ingredient, Block result, boolean reversible) {
        this(group, Ingredient.of(ingredient), new ItemStack(result), reversible);
    }

    private TransmutationInWorldRecipe(String group, Ingredient ingredient, ItemStack result) {
        this(group, ingredient, result, false);
    }

    private TransmutationInWorldRecipe(TransmutationInWorldRecipe recipe, boolean reversible) {
        this(recipe.group(), recipe.input(), recipe.result(), reversible);
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_SERIALIZER.value();
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_BOOK_CATEGORY.value();
    }

    private TransmutationInWorldRecipe(String group, Ingredient ingredient, ItemStack result, boolean reversible) {
        super(group, ingredient, result);
        this.reversible = reversible;
    }

    @Override
    public boolean matches(SingleRecipeInput container, Level level) {
        return this.input().test(container.getItem(0));
    }

    @Override
    public ItemStack result() {
        return super.result();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new StonecutterRecipeDisplay(this.input().display(),
                this.resultDisplay(),
                new SlotDisplay.ItemSlotDisplay(ModRegistry.MINIUM_STONE_ITEM)));
    }

    public SlotDisplay resultDisplay() {
        return new BlockSlotDisplay(this.getBlockResult(), this.reversible);
    }

    public Block getBlockIngredient() {
        return ((BlockItem) this.input().items().getFirst().value()).getBlock();
    }

    public Block getBlockResult() {
        return ((BlockItem) this.result().getItem()).getBlock();
    }

    public boolean isReversible() {
        return this.reversible;
    }

    public static class Serializer extends SingleItemRecipe.Serializer<TransmutationInWorldRecipe> {

        public Serializer() {
            super(TransmutationInWorldRecipe::new);
        }

        @Override
        public MapCodec<TransmutationInWorldRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> {
                return instance.group(super.codec().forGetter(Function.identity()),
                                Codec.BOOL.fieldOf("reversible").forGetter(TransmutationInWorldRecipe::isReversible))
                        .apply(instance, TransmutationInWorldRecipe::new);
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TransmutationInWorldRecipe> streamCodec() {
            return StreamCodec.composite(super.streamCodec(),
                    Function.identity(),
                    ByteBufCodecs.BOOL,
                    TransmutationInWorldRecipe::isReversible,
                    TransmutationInWorldRecipe::new);
        }
    }
}
