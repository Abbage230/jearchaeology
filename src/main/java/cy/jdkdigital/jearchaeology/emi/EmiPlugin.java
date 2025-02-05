package cy.jdkdigital.jearchaeology.emi;

import cy.jdkdigital.jearchaeology.JEArchaeology;
import cy.jdkdigital.jearchaeology.recipe.BrushingRecipe;
import cy.jdkdigital.jearchaeology.recipe.SniffRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

@EmiEntrypoint
public class EmiPlugin implements dev.emi.emi.api.EmiPlugin
{
    public static final EmiRecipeCategory BRUSHING_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, "brushing"),
            EmiStack.of(Items.BRUSH),
            new EmiTexture(ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, "textures/gui/emi/icon/brush.png"), 0, 0, 16, 16, 16, 16, 16, 16)
    );
    public static final EmiRecipeCategory SNIFFING_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, "sniffing"),
            EmiStack.of(Items.SNIFFER_EGG),
            new EmiTexture(ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, "textures/gui/emi/icon/sniff.png"), 0, 0, 16, 16, 16, 16, 16, 16)
    );

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(BRUSHING_CATEGORY);
        registry.addCategory(SNIFFING_CATEGORY);

        // Workstations
        registry.addWorkstation(BRUSHING_CATEGORY, EmiIngredient.of(Tags.Items.TOOLS_BRUSH));
        registry.addWorkstation(SNIFFING_CATEGORY, EmiStack.of(Items.SNIFFER_EGG));

        RecipeManager recipeManager = registry.getRecipeManager();

        List<RecipeHolder<BrushingRecipe>> produceRecipeList = recipeManager.getAllRecipesFor(JEArchaeology.BRUSH_TYPE.get());
        produceRecipeList.forEach(recipeHolder -> registry.addRecipe(new BrushEmiRecipe(recipeHolder)));

        List<RecipeHolder<SniffRecipe>> breedingRecipeList = recipeManager.getAllRecipesFor(JEArchaeology.SNIFF_TYPE.get());
        breedingRecipeList.forEach(recipeHolder -> registry.addRecipe(new SniffEmiRecipe(recipeHolder)));
    }
}
