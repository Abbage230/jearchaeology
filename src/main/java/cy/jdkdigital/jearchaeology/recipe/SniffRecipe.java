package cy.jdkdigital.jearchaeology.recipe;

import com.google.gson.JsonObject;
import cy.jdkdigital.jearchaeology.JEArchaeology;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class SniffRecipe implements Recipe<Container>
{
    private final ResourceLocation id;
    public final Ingredient item;
    public final double chance;

    public SniffRecipe(ResourceLocation id, Ingredient item, double chance) {
        this.id = id;
        this.item = item;
        this.chance = chance;
    }

    @Override
    public boolean matches(Container inv, Level levelIn) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return JEArchaeology.SNIFF.get();
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return JEArchaeology.SNIFF_TYPE.get();
    }

    public static class Serializer<T extends SniffRecipe> implements RecipeSerializer<T>
    {
        final SniffRecipe.Serializer.IRecipeFactory<T> factory;

        public Serializer(SniffRecipe.Serializer.IRecipeFactory<T> factory) {
            this.factory = factory;
        }

        @Nonnull
        @Override
        public T fromJson(ResourceLocation id, JsonObject json) {
            Ingredient item = Ingredient.fromJson(json.get("item"));

            double chance = GsonHelper.getAsDouble(json, "chance", 0.05D);

            return this.factory.create(id, item, chance);
        }

        public T fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buffer) {
            try {
                return this.factory.create(id, Ingredient.fromNetwork(buffer), buffer.readDouble());
            } catch (Exception e) {
                JEArchaeology.LOGGER.error("Error reading sniff recipe from packet. " + id, e);
                throw e;
            }
        }

        public void toNetwork(@Nonnull FriendlyByteBuf buffer, T recipe) {
            try {
                recipe.item.toNetwork(buffer);
                buffer.writeDouble(recipe.chance);
            } catch (Exception e) {
                JEArchaeology.LOGGER.error("Error writing sniff recipe to packet. " + recipe.getId(), e);
                throw e;
            }
        }

        public interface IRecipeFactory<T extends SniffRecipe>
        {
            T create(ResourceLocation id, Ingredient item, double chance);
        }
    }
}
