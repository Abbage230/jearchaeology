package cy.jdkdigital.jearchaeology.jei;

import cy.jdkdigital.jearchaeology.JEArchaeology;
import cy.jdkdigital.jearchaeology.compat.CompatHandler;
import cy.jdkdigital.jearchaeology.recipe.BrushingRecipe;
import cy.jdkdigital.jearchaeology.recipe.SniffRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin
{
    private static final ResourceLocation pluginId = ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, JEArchaeology.MODID);

    public static Supplier<RecipeType<RecipeHolder<SniffRecipe>>> SNIFF_RECIPE_TYPE = RecipeType.createFromDeferredVanilla(JEArchaeology.SNIFF_TYPE);
    public static Supplier<RecipeType<RecipeHolder<BrushingRecipe>>> BRUSH_RECIPE_TYPE = RecipeType.createFromDeferredVanilla(JEArchaeology.BRUSH_TYPE);

    public JeiPlugin() {
    }

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return pluginId;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Items.SNIFFER_EGG), SNIFF_RECIPE_TYPE.get());
        registration.addRecipeCatalyst(new ItemStack(Items.BRUSH), BRUSH_RECIPE_TYPE.get());
        CompatHandler.addRecipeCatalyst(registration, BRUSH_RECIPE_TYPE.get());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(new SniffRecipeCategory(guiHelper));
        registration.addRecipeCategories(new BrushRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(SNIFF_RECIPE_TYPE.get(), recipeManager.getAllRecipesFor(JEArchaeology.SNIFF_TYPE.get()));
        registration.addRecipes(BRUSH_RECIPE_TYPE.get(), recipeManager.getAllRecipesFor(JEArchaeology.BRUSH_TYPE.get()));
    }
}
