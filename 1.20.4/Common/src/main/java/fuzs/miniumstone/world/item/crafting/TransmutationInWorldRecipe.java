package fuzs.miniumstone.world.item.crafting;

import com.google.gson.JsonObject;
import fuzs.miniumstone.init.ModRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public record TransmutationInWorldRecipe(ResourceLocation id, Block ingredient, Block result, boolean reversible) implements Recipe<Container> {

    @Override
    public boolean matches(Container container, Level level) {
        return container.getItem(0).is(this.ingredient.asItem());
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.getResultItem(registryAccess);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return new ItemStack(this.result);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<TransmutationInWorldRecipe> {

        @Override
        public TransmutationInWorldRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Block ingredient = BuiltInRegistries.BLOCK.get(new ResourceLocation(GsonHelper.getAsString(json, "ingredient")));
            Block result = BuiltInRegistries.BLOCK.get(new ResourceLocation(GsonHelper.getAsString(json, "result")));
            boolean reversible = GsonHelper.getAsBoolean(json, "reversible");
            return new TransmutationInWorldRecipe(recipeId, ingredient, result, reversible);
        }

        @Override
        public TransmutationInWorldRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Block ingredient = buffer.readById(BuiltInRegistries.BLOCK);
            Block result = buffer.readById(BuiltInRegistries.BLOCK);
            boolean reversible = buffer.readBoolean();
            return new TransmutationInWorldRecipe(recipeId, ingredient, result, reversible);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TransmutationInWorldRecipe recipe) {
            buffer.writeId(BuiltInRegistries.BLOCK, recipe.ingredient());
            buffer.writeId(BuiltInRegistries.BLOCK, recipe.result);
            buffer.writeBoolean(recipe.reversible());
        }
    }
}
