package cy.jdkdigital.jearchaeology.jei;

import cy.jdkdigital.jearchaeology.JEArchaeology;
import cy.jdkdigital.jearchaeology.recipe.SniffRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SniffRecipeCategory implements IRecipeCategory<RecipeHolder<SniffRecipe>>
{
    private final IDrawable background;
    private final IDrawable icon;

    public SniffRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(126, 70);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.SNIFFER_EGG));
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<SniffRecipe>> getRecipeType() {
        return JeiPlugin.SNIFF_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable(JEArchaeology.MODID + ".recipe.sniff");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<SniffRecipe> recipe, @NotNull IFocusGroup iFocusGroup) {
        AtomicInteger i = new AtomicInteger();
        Arrays.stream(recipe.value().item.getItems()).forEach(itemStack -> {
            int row = (int)Math.floor(i.get() /7f);
            builder.addSlot(RecipeIngredientRole.OUTPUT, (i.get() - (row*7)) * 18, row * 18)
                    .addItemStack(itemStack)
                    .setSlotName("thing" + i);
            i.set(i.get() + 1);
        });
    }
}
