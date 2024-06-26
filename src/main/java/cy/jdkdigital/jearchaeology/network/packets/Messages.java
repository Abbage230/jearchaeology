package cy.jdkdigital.jearchaeology.network.packets;

import cy.jdkdigital.jearchaeology.JEArchaeology;
import cy.jdkdigital.jearchaeology.jei.BrushRecipeCategory;
import cy.jdkdigital.jearchaeology.jei.SniffRecipeCategory;
import cy.jdkdigital.jearchaeology.recipe.BrushingRecipe;
import cy.jdkdigital.jearchaeology.recipe.SniffRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Messages
{
    public interface RecipeMessage {}

    public static class SnifferDataMessage implements RecipeMessage
    {
        public List<SniffRecipe> data;

        public SnifferDataMessage(List<SniffRecipe> data) {
            this.data = data;
        }

        public static void encode(SnifferDataMessage message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.data.size());
            for (SniffRecipe entry : message.data) {
                buffer.writeResourceLocation(entry.getId());
                JEArchaeology.SNIFF.get().toNetwork(buffer, entry);
            }
        }

        public static SnifferDataMessage decode(FriendlyByteBuf buffer) {
            List<SniffRecipe> data = new ArrayList<>();
            IntStream.range(0, buffer.readInt()).forEach(i -> {
                data.add(JEArchaeology.SNIFF.get().fromNetwork(buffer.readResourceLocation(), buffer));
            });
            return new SnifferDataMessage(data);
        }

        public static void handle(SnifferDataMessage message, Supplier<NetworkEvent.Context> context) {
            SniffRecipeCategory.setRecipes(message.data);
            context.get().setPacketHandled(true);
        }
    }

    public static class BrushingDataMessage implements RecipeMessage
    {
        public List<BrushingRecipe> data;

        public BrushingDataMessage(List<BrushingRecipe> data) {
            this.data = data;
        }

        public static void encode(BrushingDataMessage message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.data.size());
            for (BrushingRecipe entry : message.data) {
                buffer.writeResourceLocation(entry.getId());
                JEArchaeology.BRUSH.get().toNetwork(buffer, entry);
            }
        }

        public static BrushingDataMessage decode(FriendlyByteBuf buffer) {
            List<BrushingRecipe> data = new ArrayList<>();
            IntStream.range(0, buffer.readInt()).forEach(i -> {
                data.add(JEArchaeology.BRUSH.get().fromNetwork(buffer.readResourceLocation(), buffer));
            });
            return new BrushingDataMessage(data);
        }

        public static void handle(BrushingDataMessage message, Supplier<NetworkEvent.Context> context) {
            BrushRecipeCategory.setRecipes(message.data);
            context.get().setPacketHandled(true);
        }
    }
}
