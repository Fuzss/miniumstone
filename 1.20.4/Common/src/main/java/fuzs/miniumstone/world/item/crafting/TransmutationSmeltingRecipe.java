package fuzs.miniumstone.world.item.crafting;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.util.MiniumStoneHelper;
import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class TransmutationSmeltingRecipe extends CustomRecipe {
    private final Ingredient miniumStone = Ingredient.of(ModRegistry.MINIUM_STONE_ITEM.value());
    private final Ingredient fuel = Ingredient.of(ItemTags.COALS);

    public TransmutationSmeltingRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        if (!hasExactlyOne(container, this.miniumStone)) return false;
        if (!hasExactlyOne(container, this.fuel)) return false;
        ItemStack itemStack = this.findAllMatchingItem(container);
        if (itemStack == ItemStack.EMPTY) {
            return false;
        } else {
            Container recipeContainer = new SimpleContainer(itemStack);
            return level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, recipeContainer, level)
                    .map(RecipeHolder::value)
                    .filter(recipe -> recipe.matches(recipeContainer, level))
                    .isPresent();
        }
    }

    private static boolean hasExactlyOne(Container container, Ingredient ingredient) {
        boolean hasIngredient = false;
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (ingredient.test(container.getItem(i))) {
                if (hasIngredient) {
                    return false;
                } else {
                    hasIngredient = true;
                }
            }
        }
        return hasIngredient;
    }

    private ItemStack findAllMatchingItem(Container container) {
        ItemStack toSmelt = ItemStack.EMPTY;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (!itemStack.isEmpty() && !this.miniumStone.test(itemStack) && !this.fuel.test(itemStack)) {
                if (toSmelt == ItemStack.EMPTY) {
                    toSmelt = itemStack.copyWithCount(1);
                } else if (ItemStack.isSameItem(toSmelt, itemStack)) {
                    toSmelt.grow(1);
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }
        return toSmelt;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack itemStack = this.findAllMatchingItem(container);
        if (itemStack != ItemStack.EMPTY) {
            Container recipeContainer = new SimpleContainer(itemStack);
            MinecraftServer minecraftServer = CommonAbstractions.INSTANCE.getMinecraftServer();
            Level level = minecraftServer.getAllLevels().iterator().next();
            ItemStack resultStack = minecraftServer.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, recipeContainer, level)
                    .map(RecipeHolder::value)
                    .map(recipe -> recipe.assemble(recipeContainer, registryAccess))
                    .orElse(ItemStack.EMPTY);
            if (!resultStack.isEmpty()) resultStack.setCount(itemStack.getCount());
            return resultStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        return MiniumStoneHelper.damageMiniumStoneClearRest(container);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.TRANSMUTATION_SMELTING_RECIPE_SERIALIZER.value();
    }
}
