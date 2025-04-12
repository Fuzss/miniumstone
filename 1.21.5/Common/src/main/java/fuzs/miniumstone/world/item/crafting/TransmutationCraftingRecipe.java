package fuzs.miniumstone.world.item.crafting;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.util.MiniumStoneHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransmutationCraftingRecipe extends ShapelessRecipe {

    public TransmutationCraftingRecipe(ShapelessRecipe shapelessRecipe) {
        this(shapelessRecipe, shapelessRecipe.placementInfo().ingredients());
    }

    public TransmutationCraftingRecipe(ShapelessRecipe shapelessRecipe, List<Ingredient> ingredients) {
        this(shapelessRecipe.group(),
                shapelessRecipe.category(),
                shapelessRecipe.assemble(CraftingInput.EMPTY, RegistryAccess.EMPTY),
                ingredients);
    }

    public TransmutationCraftingRecipe(String group, CraftingBookCategory category, ItemStack result, List<Ingredient> ingredients) {
        super(group, category, result, ingredients);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput container) {
        return MiniumStoneHelper.damageMiniumStoneClearRest(container);
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(this.placementInfo()
                .ingredients()
                .stream()
                .map(Ingredient::display)
                .toList(),
                new SlotDisplay.ItemStackSlotDisplay(this.assemble(CraftingInput.EMPTY, RegistryAccess.EMPTY)),
                new SlotDisplay.ItemSlotDisplay(ModRegistry.MINIUM_STONE_ITEM)));
    }

    @Override
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return (RecipeSerializer<ShapelessRecipe>) (RecipeSerializer<?>) ModRegistry.TRANSMUTATION_CRAFTING_RECIPE_SERIALIZER.value();
    }

    public static class Serializer implements RecipeSerializer<TransmutationCraftingRecipe> {
        public static final MapCodec<TransmutationCraftingRecipe> CODEC = RecipeSerializer.SHAPELESS_RECIPE.codec()
                .flatXmap(recipe -> {
                    ItemStack itemStack = new ItemStack(ModRegistry.MINIUM_STONE_ITEM.value());
                    for (Ingredient ingredient : recipe.placementInfo().ingredients()) {
                        if (ingredient.test(itemStack)) {
                            return DataResult.error(() -> {
                                return "Transmutation recipe must not include minium stone as ingredient";
                            });
                        }
                    }
                    if (recipe.placementInfo().ingredients().size() > 8) {
                        return DataResult.error(() -> {
                            return "Too many ingredients for transmutation recipe";
                        });
                    } else {
                        NonNullList<Ingredient> ingredients = Stream.concat(Stream.of(Ingredient.of(ModRegistry.MINIUM_STONE_ITEM.value())),
                                        recipe.placementInfo().ingredients().stream())
                                .collect(Collectors.toCollection(NonNullList::create));
                        return DataResult.success(new TransmutationCraftingRecipe(recipe, ingredients));
                    }
                }, recipe -> {
                    ItemStack itemStack = new ItemStack(ModRegistry.MINIUM_STONE_ITEM.value());
                    NonNullList<Ingredient> ingredients = recipe.placementInfo()
                            .ingredients()
                            .stream()
                            .filter(ingredient -> !ingredient.test(itemStack))
                            .collect(Collectors.toCollection(NonNullList::create));
                    return DataResult.success(new TransmutationCraftingRecipe(recipe, ingredients));
                });

        @Override
        public MapCodec<TransmutationCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TransmutationCraftingRecipe> streamCodec() {
            return RecipeSerializer.SHAPELESS_RECIPE.streamCodec()
                    .map(TransmutationCraftingRecipe::new, Function.identity());
        }
    }
}
