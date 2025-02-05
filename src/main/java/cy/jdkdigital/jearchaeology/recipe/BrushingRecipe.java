package cy.jdkdigital.jearchaeology.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cy.jdkdigital.jearchaeology.JEArchaeology;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class BrushingRecipe implements Recipe<RecipeInput>
{
    public final Ingredient item;
    public final float chance;
    public final Ingredient brushableBlock;

    public BrushingRecipe(Ingredient item, float chance, Ingredient brushableBlock) {
        this.item = item;
        this.chance = chance;
        this.brushableBlock = brushableBlock;
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
        return JEArchaeology.BRUSH.get();
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return JEArchaeology.BRUSH_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<BrushingRecipe>
    {
        private static final MapCodec<BrushingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                                Ingredient.CODEC.fieldOf("item").forGetter(recipe -> recipe.item),
                                Codec.FLOAT.fieldOf("chance").orElse(0.05f).forGetter(recipe -> recipe.chance),
                                Ingredient.CODEC.fieldOf("brushableBlock").forGetter(recipe -> recipe.brushableBlock)
                        )
                        .apply(builder, BrushingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, BrushingRecipe> STREAM_CODEC = StreamCodec.of(
                BrushingRecipe.Serializer::toNetwork, BrushingRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<BrushingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BrushingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static BrushingRecipe fromNetwork(@Nonnull RegistryFriendlyByteBuf buffer) {
            try {
                return new BrushingRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), buffer.readFloat(), Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            } catch (Exception e) {
                JEArchaeology.LOGGER.error("Error reading brush recipe from packet.", e);
                throw e;
            }
        }

        public static void toNetwork(@Nonnull RegistryFriendlyByteBuf buffer, BrushingRecipe recipe) {
            try {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.item);
                buffer.writeFloat(recipe.chance);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.brushableBlock);
            } catch (Exception e) {
                JEArchaeology.LOGGER.error("Error writing brush recipe to packet.", e);
                throw e;
            }
        }
    }
}
