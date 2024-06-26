package cy.jdkdigital.jearchaeology.compat;

import com.mojang.datafixers.util.Pair;
import cy.jdkdigital.jearchaeology.Config;
import cy.jdkdigital.jearchaeology.jei.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.ModList;

import java.util.Map;

public class CompatHandler
{
    public static Map<ResourceLocation, Pair<String, Ingredient>> getTables() {
        Map<ResourceLocation, Pair<String, Ingredient>> tables = MinecraftCompat.getTables();
        if (ModList.get().isLoaded("allthemodium") && Config.atm_compat) {
            tables.putAll(ATMCompat.getTables());
        }
        if (ModList.get().isLoaded("betterarcheology") && Config.betterarcheology_compat) {
            tables.putAll(BetterAcheologyCompat.getTables());
        }
        return tables;
    }

    public static void addRecipeCatalyst(IRecipeCatalystRegistration registration, RecipeType<?> recipeType) {
        if (ModList.get().isLoaded("betterarcheology") && Config.betterarcheology_compat && recipeType.equals(JeiPlugin.BRUSH_RECIPE_TYPE)) {
            BetterAcheologyCompat.addRecipeCatalyst(registration, recipeType);
        }
    }
}
