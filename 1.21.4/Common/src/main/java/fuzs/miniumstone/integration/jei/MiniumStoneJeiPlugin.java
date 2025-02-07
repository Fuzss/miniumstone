//package fuzs.miniumstone.integration.jei;
//
//import fuzs.miniumstone.MiniumStone;
//import fuzs.miniumstone.init.ModRegistry;
//import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
//import fuzs.puzzleslib.api.core.v1.Proxy;
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.JeiPlugin;
//import mezz.jei.api.constants.RecipeTypes;
//import mezz.jei.api.recipe.RecipeType;
//import mezz.jei.api.registration.IRecipeCatalystRegistration;
//import mezz.jei.api.registration.IRecipeCategoryRegistration;
//import mezz.jei.api.registration.IRecipeRegistration;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeHolder;
//
//@JeiPlugin
//public class MiniumStoneJeiPlugin implements IModPlugin {
//    static final RecipeType<TransmutationInWorldRecipe> TRANSMUTATION_IN_WORLD_RECIPE_TYPE = new RecipeType<>(
//            ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.key().location(),
//            TransmutationInWorldRecipe.class
//    );
//
//    @Override
//    public ResourceLocation getPluginUid() {
//        return MiniumStone.id("main");
//    }
//
//    @Override
//    public void registerCategories(IRecipeCategoryRegistration registration) {
//        registration.addRecipeCategories(new TransmutationInWorldRecipeCategory(registration.getJeiHelpers()
//                .getGuiHelper()));
//    }
//
//    @Override
//    public void registerRecipes(IRecipeRegistration registration) {
//        registration.addRecipes(TRANSMUTATION_IN_WORLD_RECIPE_TYPE,
//                Proxy.INSTANCE.getClientLevel()
//                        .getRecipeManager()
//                        .getAllRecipesFor(ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value())
//                        .stream()
//                        .map(RecipeHolder::value)
//                        .toList()
//        );
//    }
//
//    @Override
//    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
//        registration.addRecipeCatalyst(new ItemStack(ModRegistry.MINIUM_STONE_ITEM.value()),
//                TRANSMUTATION_IN_WORLD_RECIPE_TYPE
//        );
//        registration.addRecipeCatalyst(new ItemStack(ModRegistry.MINIUM_STONE_ITEM.value()), RecipeTypes.CRAFTING);
//    }
//}
