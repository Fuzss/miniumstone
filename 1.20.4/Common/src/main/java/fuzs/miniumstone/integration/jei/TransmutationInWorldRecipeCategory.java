package fuzs.miniumstone.integration.jei;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.puzzleslib.api.core.v1.Proxy;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Copied from {@link mezz.jei.library.plugins.vanilla.stonecutting.StoneCuttingRecipeCategory}.
 */
public class TransmutationInWorldRecipeCategory implements IRecipeCategory<TransmutationInWorldRecipe> {
    public static final ResourceLocation BACKGROUND_LOCATION = MiniumStone.id(
            "textures/gui/transmutation_in_world_background.png");
    public static final Component RECIPE_TYPE_COMPONENT = Component.translatable(Util.makeDescriptionId("recipe_type",
            MiniumStoneJeiPlugin.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.getUid()
    ));

    private final IDrawable background;
    private final IDrawable icon;

    public TransmutationInWorldRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(BACKGROUND_LOCATION, 0, 0, 82, 34);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModRegistry.MINIUM_STONE_ITEM.value()));
    }

    @Override
    public RecipeType<TransmutationInWorldRecipe> getRecipeType() {
        return MiniumStoneJeiPlugin.TRANSMUTATION_IN_WORLD_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return RECIPE_TYPE_COMPONENT;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TransmutationInWorldRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9)
                .addItemStack(recipe.getResultItem(Proxy.INSTANCE.getClientLevel().registryAccess()));
    }
}
