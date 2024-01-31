package fuzs.miniumstone.world.item.crafting;

import com.google.gson.JsonObject;
import fuzs.miniumstone.init.ModRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Based on {@link net.minecraft.world.item.crafting.SingleItemRecipe} as we need this to be a {@link net.minecraft.world.item.crafting.CraftingRecipe},
 * which isn't possible with that super class due to different type parameters.
 */
public class TransmutationCraftingRecipe extends CustomRecipe {
    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int ingredientCount;

    public TransmutationCraftingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, int ingredientCount) {
        super(id, CraftingBookCategory.MISC);
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.ingredientCount = ingredientCount;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean foundMiniumStone = false;
        int foundIngredients = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.is(ModRegistry.MINIUM_STONE_ITEM.get())) {
                if (foundMiniumStone) {
                    return false;
                } else {
                    foundMiniumStone = true;
                }
            } else if (this.ingredient.test(stack)) {
                foundIngredients++;
            } else if (!stack.isEmpty()) {
                return false;
            }
        }
        return foundMiniumStone && foundIngredients == this.ingredientCount;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return this.getResultItem(registryAccess);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = container.getItem(i);
            if (itemstack.getItem().hasCraftingRemainingItem()) {
                nonnulllist.set(i, new ItemStack(itemstack.getItem().getCraftingRemainingItem()));
            } else if (itemstack.is(ModRegistry.MINIUM_STONE_ITEM.get())) {
                itemstack = itemstack.copy();
                if (itemstack.hurt(1, RandomSource.create(), null)) {
                    itemstack = ItemStack.EMPTY;
                }
                nonnulllist.set(i, itemstack);
                break;
            }
        }

        return nonnulllist;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ModRegistry.MINIUM_STONE_ITEM.get()));
        for (int i = 0; i < this.ingredientCount; i++) {
            ingredients.add(this.ingredient);
        }
        return ingredients;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.TRANSMUTATION_CRAFTING_RECIPE_SERIALIZER.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.ingredientCount + 1;
    }

    /**
     * Copied from {@link net.minecraft.world.item.crafting.SingleItemRecipe.Serializer} as the required interface is not accessible on Forge.
     */
    public static class Serializer implements RecipeSerializer<TransmutationCraftingRecipe> {

        public TransmutationCraftingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String string = GsonHelper.getAsString(json, "group", "");
            Ingredient ingredient;
            if (GsonHelper.isArrayNode(json, "ingredient")) {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
            } else {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
            }

            String string2 = GsonHelper.getAsString(json, "result");
            int i = GsonHelper.getAsInt(json, "result_count");
            ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(string2)), i);
            int ingredientCount = GsonHelper.getAsInt(json, "ingredient_count");
            return new TransmutationCraftingRecipe(recipeId, string, ingredient, itemStack, ingredientCount);
        }

        public TransmutationCraftingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String string = buffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemStack = buffer.readItem();
            byte ingredientCount = buffer.readByte();
            return new TransmutationCraftingRecipe(recipeId, string, ingredient, itemStack, ingredientCount);
        }

        public void toNetwork(FriendlyByteBuf buffer, TransmutationCraftingRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeByte(recipe.ingredientCount);
        }
    }
}
