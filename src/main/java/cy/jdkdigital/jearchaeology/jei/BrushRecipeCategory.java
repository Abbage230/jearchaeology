package cy.jdkdigital.jearchaeology.jei;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import cy.jdkdigital.jearchaeology.JEArchaeology;
import cy.jdkdigital.jearchaeology.compat.CompatHandler;
import cy.jdkdigital.jearchaeology.recipe.BrushingRecipe;
import cy.jdkdigital.jearchaeology.recipe.SniffRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BrushRecipeCategory implements IRecipeCategory<BrushingRecipe>
{
    private static final UUID BRUSHER_PLAYER_UUID = UUID.nameUUIDFromBytes("jea_brusher_player".getBytes(StandardCharsets.UTF_8));
    private static List<BrushingRecipe> cachedRecipes = new ArrayList<>();
    private final IDrawable background;
    private final IDrawable icon;

    public BrushRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(126, 70);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.BRUSH));
    }

    public static List<BrushingRecipe> getAllRecipes(ServerLevel level) {
        if (level != null && cachedRecipes.isEmpty()) {
            List<BrushingRecipe> recipeList = new ArrayList<>();

            Map<ResourceLocation, Pair<String, Ingredient>> tables = CompatHandler.getTables();

            Player fakePlayer = FakePlayerFactory.get(level, new GameProfile(BRUSHER_PLAYER_UUID, "jea_brusher_player"));
            LootParams lootparams = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(new BlockPos(0, 0, 0))).withLuck(1.0f).withParameter(LootContextParams.THIS_ENTITY, fakePlayer).create(LootContextParamSets.CHEST);
            tables.forEach((resourceLocation, pair) -> {
                Map<Item, ItemStack> items = new HashMap<>();
                var table = level.getServer().getLootData().getLootTable(resourceLocation);
                if (!table.equals(LootTable.EMPTY)) {
                    for (int i = 0; i < 600; i++) {
                        table.getRandomItems(lootparams).forEach(itemStack -> {
                            if (!items.containsKey(itemStack.getItem())) {
                                items.put(itemStack.getItem(), itemStack);
                            }
                        });
                    }
                }
                String locationName = pair.getFirst();
                if (items.size() > 42) {
                    recipeList.add(new BrushingRecipe(new ResourceLocation(JEArchaeology.MODID, locationName), Ingredient.of(items.values().stream().limit(42).toList().toArray(new ItemStack[0])), 1D, pair.getSecond()));
                    recipeList.add(new BrushingRecipe(new ResourceLocation(JEArchaeology.MODID, locationName), Ingredient.of(items.values().stream().skip(42).toList().toArray(new ItemStack[0])), 1D, pair.getSecond()));
                } else if (items.size() > 21) {
                    recipeList.add(new BrushingRecipe(new ResourceLocation(JEArchaeology.MODID, locationName), Ingredient.of(items.values().stream().limit(21).toList().toArray(new ItemStack[0])), 1D, pair.getSecond()));
                    recipeList.add(new BrushingRecipe(new ResourceLocation(JEArchaeology.MODID, locationName), Ingredient.of(items.values().stream().skip(21).toList().toArray(new ItemStack[0])), 1D, pair.getSecond()));
                } else {
                    recipeList.add(new BrushingRecipe(new ResourceLocation(JEArchaeology.MODID, locationName), Ingredient.of(items.values().toArray(new ItemStack[0])), 1D, pair.getSecond()));
                }
            });
            setRecipes(recipeList);
        }
        return cachedRecipes;
    }

    public static void setRecipes(List<BrushingRecipe> data) {
        cachedRecipes = data;
    }

    @Override
    public @NotNull RecipeType<BrushingRecipe> getRecipeType() {
        return JeiPlugin.BRUSH_RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable(JEArchaeology.MODID + ".recipe.brush");
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
    public void setRecipe(IRecipeLayoutBuilder builder, BrushingRecipe recipe, @NotNull IFocusGroup iFocusGroup) {
        AtomicInteger i = new AtomicInteger();
        Arrays.stream(recipe.item.getItems()).forEach(itemStack -> {
            int row = (int)Math.floor(i.get() /7f);
            builder.addSlot(RecipeIngredientRole.OUTPUT, (i.get() - (row*7)) * 18, row * 18).addItemStack(itemStack).setSlotName("thing" + i);
            i.set(i.get() + 1);
        });
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 56).addIngredients(recipe.brushableBlock).setSlotName("sussy_block");
    }

    @Override
    public void draw(BrushingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.drawString(minecraft.font, Language.getInstance().getVisualOrder(Component.translatable(JEArchaeology.MODID + ".brush.structure." + recipe.getId().getPath())), 22, 60, 0xFF000000, false);
    }
}
