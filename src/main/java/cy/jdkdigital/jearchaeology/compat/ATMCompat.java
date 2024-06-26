package cy.jdkdigital.jearchaeology.compat;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ATMCompat
{
    public static Map<ResourceLocation, Pair<String, Ingredient>> getTables() {
        return new HashMap<>() {{
            put(new ResourceLocation("allthemodium:arch"), Pair.of("ancient_city", Ingredient.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("allthemodium:suspicious_clay")))));
            put(new ResourceLocation("allthemodium:arch2"), Pair.of("bastion", Ingredient.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("allthemodium:suspicious_soul_sand")))));
        }};
    }
}
