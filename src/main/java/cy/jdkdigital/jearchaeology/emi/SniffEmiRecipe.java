package cy.jdkdigital.jearchaeology.emi;

import cy.jdkdigital.jearchaeology.recipe.SniffRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class SniffEmiRecipe extends BasicEmiRecipe
{
    public SniffEmiRecipe(RecipeHolder<SniffRecipe> recipe) {
        super(EmiPlugin.SNIFFING_CATEGORY, recipe.id(), 126, 75);

        for (ItemStack itemStack : recipe.value().item.getItems()) {
            this.outputs.add(EmiStack.of(itemStack));
        }
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        for (int i = 0; i < this.outputs.size(); i++){
            int row = (int) Math.floor(i / 7f);
            widgets.addSlot(this.outputs.get(i), (i - (row * 7)) * 18, row * 18).drawBack(false).recipeContext(this);
        }
    }
}
