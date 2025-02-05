package cy.jdkdigital.jearchaeology.recipe;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import cy.jdkdigital.jearchaeology.JEArchaeology;
import cy.jdkdigital.jearchaeology.compat.CompatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Helper
{
    private static final UUID BRUSHER_PLAYER_UUID = UUID.nameUUIDFromBytes("jea_brusher_player".getBytes(StandardCharsets.UTF_8));
    private static List<RecipeHolder<?>> cachedBrushingRecipes = new ArrayList<>();
    private static List<RecipeHolder<?>> cachedSniffingRecipes = new ArrayList<>();

    public static List<RecipeHolder<?>> getAllBrushingRecipes(ServerLevel level) {
        if (level != null && cachedBrushingRecipes.isEmpty()) {
            List<RecipeHolder<?>> recipeList = new ArrayList<>();

            Map<ResourceKey<LootTable>, Pair<String, Ingredient>> tables = CompatHandler.getTables();

            Player fakePlayer = FakePlayerFactory.get(level, new GameProfile(BRUSHER_PLAYER_UUID, "jea_brusher_player"));
            LootParams lootparams = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(new BlockPos(0, 0, 0))).withLuck(1.0f).withParameter(LootContextParams.THIS_ENTITY, fakePlayer).create(LootContextParamSets.CHEST);
            tables.forEach((lootTableKey, pair) -> {
                Map<Item, ItemStack> items = new HashMap<>();
                var table = level.getServer().reloadableRegistries().getLootTable(lootTableKey);
                if (!table.equals(LootTable.EMPTY)) {
                    for (int i = 0; i < 600; i++) {
                        table.getRandomItems(lootparams).forEach(itemStack -> {
                            if (!items.containsKey(itemStack.getItem())) {
                                items.put(itemStack.getItem(), itemStack);
                            }
                        });
                    }
                }
                String locationName = lootTableKey.location().getPath();
                if (items.size() > 42) {
                    recipeList.add(new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, locationName + "_4"), new BrushingRecipe(Ingredient.of(items.values().stream().limit(42).skip(21).toList().toArray(new ItemStack[0])), 1f, pair.getSecond())));
                    recipeList.add(new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, locationName + "_3"), new BrushingRecipe(Ingredient.of(items.values().stream().skip(42).toList().toArray(new ItemStack[0])), 1f, pair.getSecond())));
                }
                if (items.size() > 21) {
                    recipeList.add(new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, locationName + "_2"), new BrushingRecipe(Ingredient.of(items.values().stream().limit(21).toList().toArray(new ItemStack[0])), 1f, pair.getSecond())));
                    recipeList.add(new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, locationName + "_1"), new BrushingRecipe(Ingredient.of(items.values().stream().skip(21).toList().toArray(new ItemStack[0])), 1f, pair.getSecond())));
                } else {
                    recipeList.add(new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, locationName), new BrushingRecipe(Ingredient.of(items.values().toArray(new ItemStack[0])), 1f, pair.getSecond())));
                }
            });
            cachedBrushingRecipes = recipeList;
        }
        return cachedBrushingRecipes;
    }

    public static List<RecipeHolder<?>> getAllSniffingRecipes(ServerLevel level) {
        if (level != null && cachedSniffingRecipes.isEmpty()) {
            var sniffer = EntityType.SNIFFER.create(level);
            LootParams lootparams = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, new Vec3(0, 0, 0)).withParameter(LootContextParams.THIS_ENTITY, sniffer).create(LootContextParamSets.GIFT);
            Map<Item, ItemStack> items = new HashMap<>();
            var table = level.getServer().reloadableRegistries().getLootTable(BuiltInLootTables.SNIFFER_DIGGING);
            JEArchaeology.LOGGER.info("table " + table);
            for (int i = 0; i < 400; i++) {
                table.getRandomItems(lootparams).forEach(itemStack -> {
                    if (!items.containsKey(itemStack.getItem())) {
                        items.put(itemStack.getItem(), itemStack);
                    }
                });
            }
            cachedSniffingRecipes = List.of(new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(JEArchaeology.MODID, "sniffing"), new SniffRecipe(Ingredient.of(items.values().toArray(new ItemStack[0])), 1f)));
        }
        return cachedSniffingRecipes;
    }
}
