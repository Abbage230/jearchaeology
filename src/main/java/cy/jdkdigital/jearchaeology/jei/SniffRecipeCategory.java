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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SniffRecipeCategory implements IRecipeCategory<SniffRecipe>
{
    private static List<SniffRecipe> cachedRecipes = new ArrayList<>();
    private final IDrawable background;
    private final IDrawable icon;

    public SniffRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(126, 70);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.SNIFFER_EGG));
    }

    public static List<SniffRecipe> getAllRecipes(ServerLevel level) {
        if (level != null && cachedRecipes.isEmpty()) {
            var sniffer = EntityType.SNIFFER.create(level);
            LootParams lootparams = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, new Vec3(0, 0, 0)).withParameter(LootContextParams.THIS_ENTITY, sniffer).create(LootContextParamSets.GIFT);
            Map<Item, ItemStack> items = new HashMap<>();
            var table = level.getServer().getLootData().getLootTable(BuiltInLootTables.SNIFFER_DIGGING);
            for (int i = 0; i < 200; i++) {
                table.getRandomItems(lootparams).forEach(itemStack -> {
                    if (!items.containsKey(itemStack.getItem())) {
                        items.put(itemStack.getItem(), itemStack);
                    }
                });
            }
            setRecipes(List.of(new SniffRecipe(new ResourceLocation(JEArchaeology.MODID, "sniffing"), Ingredient.of(items.values().toArray(new ItemStack[0])), 1D)));
        }
        return cachedRecipes;
    }

    public static void setRecipes(List<SniffRecipe> data) {
        cachedRecipes = data;
    }

    @Override
    public @NotNull RecipeType<SniffRecipe> getRecipeType() {
        return JeiPlugin.SNIFF_RECIPE_TYPE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, SniffRecipe recipe, @NotNull IFocusGroup iFocusGroup) {
        AtomicInteger i = new AtomicInteger();
        Arrays.stream(recipe.item.getItems()).forEach(itemStack -> {
            int row = (int)Math.floor(i.get() /7f);
            builder.addSlot(RecipeIngredientRole.OUTPUT, (i.get() - (row*7)) * 18, row * 18)
                    .addItemStack(itemStack)
                    .setSlotName("thing" + i);
            i.set(i.get() + 1);
        });
    }
}
