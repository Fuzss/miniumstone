package fuzs.miniumstone.world.item.crafting;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.Proxy;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class TransmutationSmeltingRecipe extends CustomRecipe {
    private static final Ingredient MINIUM_STONE_INGREDIENT = Ingredient.of(ModRegistry.MINIUM_STONE_ITEM.get());
    private static final Ingredient FUEL_INGREDIENT = Ingredient.of(ItemTags.COALS);

    public TransmutationSmeltingRecipe(ResourceLocation resourceLocation, CraftingBookCategory craftingBookCategory) {
        super(resourceLocation, craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        if (!hasExactlyOne(container, MINIUM_STONE_INGREDIENT)) return false;
        if (!hasExactlyOne(container, FUEL_INGREDIENT)) return false;
        ItemStack itemStack = findAllMatchingItem(container);
        if (itemStack == ItemStack.EMPTY) {
            return false;
        } else {
            Container recipeContainer = new SimpleContainer(itemStack);
            return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, recipeContainer, level).filter(t -> t.matches(recipeContainer, level)).isPresent();
        }
    }

    private static ItemStack findAllMatchingItem(Container container) {
        ItemStack toSmelt = ItemStack.EMPTY;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (!itemStack.isEmpty() && !MINIUM_STONE_INGREDIENT.test(itemStack) && !FUEL_INGREDIENT.test(itemStack)) {
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

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack itemStack = findAllMatchingItem(container);
        if (itemStack != ItemStack.EMPTY) {
            Container recipeContainer = new SimpleContainer(itemStack);
            MinecraftServer minecraftServer = Proxy.INSTANCE.getGameServer();
            Level level = minecraftServer.getAllLevels().iterator().next();
            ItemStack resultStack = minecraftServer.getRecipeManager().getRecipeFor(RecipeType.SMELTING, recipeContainer, level).map(t -> t.assemble(recipeContainer, registryAccess)).orElse(ItemStack.EMPTY);
            if (!resultStack.isEmpty()) resultStack.setCount(itemStack.getCount());
            return resultStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.TRANSMUTATION_SMELTING_RECIPE_SERIALIZER.get();
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
}
