package cy.jdkdigital.jearchaeology.compat;

import com.mojang.datafixers.util.Pair;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BetterAcheologyCompat
{
    public static Map<ResourceKey<LootTable>, Pair<String, Ingredient>> getTables() {
        return new HashMap<>() {{
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/mesa_red_sand")), Pair.of("archeologist_camp_redsand", Ingredient.of(BuiltInRegistries.BLOCK.get(ResourceLocation.parse("betterarcheology:suspicious_red_sand")))));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/taiga_dirt")), Pair.of("archeologist_camp_grassy", Ingredient.of(BuiltInRegistries.BLOCK.get(ResourceLocation.parse("betterarcheology:suspicious_dirt")))));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/plains_gravel")), Pair.of("plains_gravel", Ingredient.of(Blocks.SUSPICIOUS_GRAVEL)));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/desert_sand")), Pair.of("betterarcheology_desert", Ingredient.of(Blocks.SUSPICIOUS_SAND)));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/desert_sand")), Pair.of("archeologist_camp_sand", Ingredient.of(Blocks.SUSPICIOUS_SAND)));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/sussand_underwater")), Pair.of("underwater", Ingredient.of(Blocks.SUSPICIOUS_SAND)));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/fossiliferous_dirt_chicken")), Pair.of("fossil_chicken", Ingredient.of(BuiltInRegistries.BLOCK.get(ResourceLocation.parse("betterarcheology:fossiliferous_dirt")))));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/fossiliferous_dirt_creeper")), Pair.of("fossil_creeper", Ingredient.of(BuiltInRegistries.BLOCK.get(ResourceLocation.parse("betterarcheology:fossiliferous_dirt")))));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/fossiliferous_dirt_jungle")), Pair.of("fossil_jungle", Ingredient.of(BuiltInRegistries.BLOCK.get(ResourceLocation.parse("betterarcheology:fossiliferous_dirt")))));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/fossiliferous_dirt_sheep")), Pair.of("fossil_sheep", Ingredient.of(BuiltInRegistries.BLOCK.get(ResourceLocation.parse("betterarcheology:fossiliferous_dirt")))));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("betterarcheology:archeology/fossiliferous_dirt_villager")), Pair.of("fossil_villager", Ingredient.of(BuiltInRegistries.BLOCK.get(ResourceLocation.parse("betterarcheology:fossiliferous_dirt")))));
        }};
    }

    public static void addRecipeCatalyst(IRecipeCatalystRegistration registration, RecipeType<?> recipeType) {
        registration.addRecipeCatalyst(new ItemStack(Objects.requireNonNull(BuiltInRegistries.ITEM.get(ResourceLocation.parse("betterarcheology:iron_brush")))), recipeType);
        registration.addRecipeCatalyst(new ItemStack(Objects.requireNonNull(BuiltInRegistries.ITEM.get(ResourceLocation.parse("betterarcheology:diamond_brush")))), recipeType);
    }
}
