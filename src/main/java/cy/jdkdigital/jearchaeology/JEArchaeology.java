package cy.jdkdigital.jearchaeology;

import com.mojang.logging.LogUtils;
import cy.jdkdigital.jearchaeology.jei.SniffRecipeCategory;
import cy.jdkdigital.jearchaeology.recipe.BrushingRecipe;
import cy.jdkdigital.jearchaeology.recipe.Helper;
import cy.jdkdigital.jearchaeology.recipe.SniffRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Mod(JEArchaeology.MODID)
public class JEArchaeology
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "jearchaeology";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SniffRecipe>> SNIFF = RECIPE_SERIALIZERS.register("sniff", SniffRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BrushingRecipe>> BRUSH = RECIPE_SERIALIZERS.register("brush", BrushingRecipe.Serializer::new);
    public static DeferredHolder<RecipeType<?>, RecipeType<SniffRecipe>> SNIFF_TYPE = RECIPE_TYPES.register("sniff", () -> new RecipeType<>() {});
    public static DeferredHolder<RecipeType<?>, RecipeType<BrushingRecipe>> BRUSH_TYPE = RECIPE_TYPES.register("brush", () -> new RecipeType<>() {});

    public JEArchaeology(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @EventBusSubscriber(modid = JEArchaeology.MODID, bus = EventBusSubscriber.Bus.MOD)
    class ModEvents
    {
        @SubscribeEvent
        public static void payloadHandler(RegisterPayloadHandlersEvent event) {
//            final PayloadRegistrar registrar = event.registrar(JEArchaeology.MODID).versioned("1").optional();
//            registrar.playToClient(
//                    BrushingDataMessage.TYPE,
//                    BrushingDataMessage.STREAM_CODEC,
//                    new DirectionalPayloadHandler<>(
//                            BrushingDataMessage::clientHandle,
//                            BrushingDataMessage::serverHandle
//                    )
//            );
//            registrar.playToClient(
//                    SnifferDataMessage.TYPE,
//                    SnifferDataMessage.STREAM_CODEC,
//                    new DirectionalPayloadHandler<>(
//                            SnifferDataMessage::clientHandle,
//                            SnifferDataMessage::serverHandle
//                    )
//            );
        }
    }
    @EventBusSubscriber(modid = JEArchaeology.MODID)
    class Events
    {
        @SubscribeEvent
        public static void onDataSync(OnDatapackSyncEvent event) {
            var player = event.getRelevantPlayers().findFirst();
            if (player.isPresent() && player.get().getServer() != null) {
                var recipeManager = player.get().getServer().getRecipeManager();

                var sniffRecipes = recipeManager.getAllRecipesFor(SNIFF_TYPE.get());
                if (sniffRecipes.size() == 0) {
                    Collection<RecipeHolder<?>> allRecipes = new ArrayList<>(recipeManager.getRecipes());
                    allRecipes.addAll(Helper.getAllBrushingRecipes(event.getPlayerList().getServer().getLevel(Level.OVERWORLD)));
                    allRecipes.addAll(Helper.getAllSniffingRecipes(event.getPlayerList().getServer().getLevel(Level.OVERWORLD)));
                    recipeManager.replaceRecipes(allRecipes);
                }
            }
        }
    }
}
