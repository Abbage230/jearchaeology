package cy.jdkdigital.jearchaeology.compat;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashMap;
import java.util.Map;

public class MinecraftCompat
{
    static Ingredient SUS_BLOCKS = Ingredient.of(Blocks.SUSPICIOUS_SAND, Blocks.SUSPICIOUS_GRAVEL);
    public static Map<ResourceKey<LootTable>, Pair<String, Ingredient>> getTables() {
        return new HashMap<>() {{
            put(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY, Pair.of("desert_well", SUS_BLOCKS));
            put(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY, Pair.of("desert_pyramid", SUS_BLOCKS));
            put(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON, Pair.of("trail_ruins", SUS_BLOCKS));
            put(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE, Pair.of("trail_ruins", SUS_BLOCKS));
            put(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY, Pair.of("warm_ocean_ruin", SUS_BLOCKS));
            put(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY, Pair.of("cold_ocean_ruin", SUS_BLOCKS));
        }};
    }
}
