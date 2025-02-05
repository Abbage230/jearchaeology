package cy.jdkdigital.jearchaeology.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cy.jdkdigital.jearchaeology.JEArchaeology;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class SniffRecipe implements Recipe<RecipeInput>
{
    public final Ingredient item;
    public final float chance;

    public SniffRecipe(Ingredient item, float chance) {
        this.item = item;
        this.chance = chance;
    }

    @Override
    public boolean matches(RecipeInput inv, Level levelIn) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack assemble(RecipeInput inv, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
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

    public static class Serializer implements RecipeSerializer<SniffRecipe>
    {
        private static final MapCodec<SniffRecipe> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                                Ingredient.CODEC.fieldOf("item").forGetter(recipe -> recipe.item),
                                Codec.FLOAT.fieldOf("chance").orElse(0.05f).forGetter(recipe -> recipe.chance)
                        )
                        .apply(builder, SniffRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, SniffRecipe> STREAM_CODEC = StreamCodec.of(
                SniffRecipe.Serializer::toNetwork, SniffRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<SniffRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SniffRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static SniffRecipe fromNetwork(@Nonnull RegistryFriendlyByteBuf buffer) {
            try {
                return new SniffRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), buffer.readFloat());
            } catch (Exception e) {
                JEArchaeology.LOGGER.error("Error reading sniff recipe from packet.", e);
                throw e;
            }
        }

        public static void toNetwork(@Nonnull RegistryFriendlyByteBuf buffer, SniffRecipe recipe) {
            try {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.item);
                buffer.writeFloat(recipe.chance);
            } catch (Exception e) {
                JEArchaeology.LOGGER.error("Error writing sniff recipe to packet.", e);
                throw e;
            }
        }
    }
}
