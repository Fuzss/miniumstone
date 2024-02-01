package fuzs.miniumstone.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.miniumstone.init.ModRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public final class TransmutationInWorldRecipe extends SingleItemRecipe {
    private boolean reversible;

    public TransmutationInWorldRecipe(String group, Block ingredient, Block result, boolean reversible) {
        this(group, Ingredient.of(ingredient), new ItemStack(result));
        this.reversible = reversible;
    }

    public TransmutationInWorldRecipe(String group, Ingredient ingredient, ItemStack result) {
        super(ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value(),
                ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_SERIALIZER.value(),
                group,
                ingredient,
                result
        );
    }

    @Override
    public boolean matches(Container container, Level level) {
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
        public static final Codec<TransmutationInWorldRecipe> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(ExtraCodecs.strictOptionalField(Codec.STRING, "group", "")
                            .forGetter(SingleItemRecipe::getGroup),
                    BuiltInRegistries.BLOCK.byNameCodec()
                            .fieldOf("ingredient")
                            .forGetter(TransmutationInWorldRecipe::getBlockIngredient),
                    BuiltInRegistries.BLOCK.byNameCodec()
                            .fieldOf("result")
                            .forGetter(TransmutationInWorldRecipe::getBlockResult),
                    Codec.BOOL.fieldOf("reversible").forGetter(TransmutationInWorldRecipe::isReversible)
            ).apply(instance, TransmutationInWorldRecipe::new);
        });

        public Serializer() {
            super(TransmutationInWorldRecipe::new);
        }

        @Override
        public Codec<TransmutationInWorldRecipe> codec() {
            return CODEC;
        }

        @Override
        public TransmutationInWorldRecipe fromNetwork(FriendlyByteBuf buffer) {
            TransmutationInWorldRecipe recipe = super.fromNetwork(buffer);
            recipe.reversible = buffer.readBoolean();
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TransmutationInWorldRecipe recipe) {
            super.toNetwork(buffer, recipe);
            buffer.writeBoolean(recipe.reversible);
        }
    }
}
