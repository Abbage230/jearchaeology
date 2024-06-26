package cy.jdkdigital.jearchaeology.compat;

import com.mojang.datafixers.util.Pair;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BetterAcheologyCompat
{
    public static Map<ResourceLocation, Pair<String, Ingredient>> getTables() {
        return new HashMap<>() {{
            put(new ResourceLocation("betterarcheology:archeology/mesa_red_sand"), Pair.of("archeologist_camp_redsand", Ingredient.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("betterarcheology:suspicious_red_sand")))));
            put(new ResourceLocation("betterarcheology:archeology/taiga_dirt"), Pair.of("archeologist_camp_grassy", Ingredient.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("betterarcheology:suspicious_dirt")))));
            put(new ResourceLocation("betterarcheology:archeology/plains_gravel"), Pair.of("plains_gravel", Ingredient.of(Blocks.SUSPICIOUS_GRAVEL)));
            put(new ResourceLocation("betterarcheology:archeology/desert_sand"), Pair.of("betterarcheology_desert", Ingredient.of(Blocks.SUSPICIOUS_SAND)));
            put(new ResourceLocation("betterarcheology:archeology/desert_sand"), Pair.of("archeologist_camp_sand", Ingredient.of(Blocks.SUSPICIOUS_SAND)));
            put(new ResourceLocation("betterarcheology:archeology/sussand_underwater"), Pair.of("underwater", Ingredient.of(Blocks.SUSPICIOUS_SAND)));
            put(new ResourceLocation("betterarcheology:archeology/fossiliferous_dirt_chicken"), Pair.of("fossil_chicken", Ingredient.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("betterarcheology:fossiliferous_dirt")))));
            put(new ResourceLocation("betterarcheology:archeology/fossiliferous_dirt_creeper"), Pair.of("fossil_creeper", Ingredient.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("betterarcheology:fossiliferous_dirt")))));
            put(new ResourceLocation("betterarcheology:archeology/fossiliferous_dirt_jungle"), Pair.of("fossil_jungle", Ingredient.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("betterarcheology:fossiliferous_dirt")))));
            put(new ResourceLocation("betterarcheology:archeology/fossiliferous_dirt_sheep"), Pair.of("fossil_sheep", Ingredient.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("betterarcheology:fossiliferous_dirt")))));
            put(new ResourceLocation("betterarcheology:archeology/fossiliferous_dirt_villager"), Pair.of("fossil_villager", Ingredient.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("betterarcheology:fossiliferous_dirt")))));
        }};
    }

    public static void addRecipeCatalyst(IRecipeCatalystRegistration registration, RecipeType<?> recipeType) {
        registration.addRecipeCatalyst(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("betterarcheology:iron_brush")))), recipeType);
        registration.addRecipeCatalyst(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("betterarcheology:diamond_brush")))), recipeType);
    }
}
