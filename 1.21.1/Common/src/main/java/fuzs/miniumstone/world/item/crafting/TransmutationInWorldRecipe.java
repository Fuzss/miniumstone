package fuzs.miniumstone.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.miniumstone.init.ModRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public final class TransmutationInWorldRecipe extends SingleItemRecipe {
    private final boolean reversible;

    public TransmutationInWorldRecipe(String group, Block ingredient, Block result, boolean reversible) {
        this(group, Ingredient.of(ingredient), new ItemStack(result), reversible);
    }

    private TransmutationInWorldRecipe(String group, Ingredient ingredient, ItemStack result) {
        this(group, ingredient, result, false);
    }

    private TransmutationInWorldRecipe(TransmutationInWorldRecipe recipe, boolean reversible) {
        this(recipe.group, recipe.ingredient, recipe.result, reversible);
    }

    private TransmutationInWorldRecipe(String group, Ingredient ingredient, ItemStack result, boolean reversible) {
        super(ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value(),
                ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_SERIALIZER.value(), group, ingredient, result
        );
        this.reversible = reversible;
    }

    @Override
    public boolean matches(SingleRecipeInput container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModRegistry.MINIUM_STONE_ITEM.value());
    }

    public Block getBlockIngredient() {
        return ((BlockItem) this.ingredient.getItems()[0].getItem()).getBlock();
    }

    public Block getBlockResult() {
        return ((BlockItem) this.result.getItem()).getBlock();
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
                        Codec.BOOL.fieldOf("reversible").forGetter(TransmutationInWorldRecipe::isReversible)
                ).apply(instance, TransmutationInWorldRecipe::new);
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TransmutationInWorldRecipe> streamCodec() {
            return StreamCodec.composite(super.streamCodec(), Function.identity(), ByteBufCodecs.BOOL,
                    TransmutationInWorldRecipe::isReversible, TransmutationInWorldRecipe::new
            );
        }
    }
}
