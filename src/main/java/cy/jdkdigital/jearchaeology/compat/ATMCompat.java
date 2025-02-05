package cy.jdkdigital.jearchaeology.compat;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashMap;
import java.util.Map;

public class ATMCompat
{
    public static Map<ResourceKey<LootTable>, Pair<String, Ingredient>> getTables() {
        return new HashMap<>() {{
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("allthemodium:arch")), Pair.of("ancient_city", Ingredient.of(BuiltInRegistries.BLOCK.get(ResourceLocation.parse("allthemodium:suspicious_clay")))));
            put(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("allthemodium:arch2")), Pair.of("bastion", Ingredient.of(BuiltInRegistries.BLOCK.get(ResourceLocation.parse("allthemodium:suspicious_soul_sand")))));
        }};
    }
}
