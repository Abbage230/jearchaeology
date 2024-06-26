package cy.jdkdigital.jearchaeology;

import com.mojang.logging.LogUtils;
import cy.jdkdigital.jearchaeology.jei.BrushRecipeCategory;
import cy.jdkdigital.jearchaeology.jei.SniffRecipeCategory;
import cy.jdkdigital.jearchaeology.network.PacketHandler;
import cy.jdkdigital.jearchaeology.network.packets.Messages;
import cy.jdkdigital.jearchaeology.recipe.BrushingRecipe;
import cy.jdkdigital.jearchaeology.recipe.SniffRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(JEArchaeology.MODID)
public class JEArchaeology
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "jearchaeology";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);

    public static final RegistryObject<RecipeSerializer<SniffRecipe>> SNIFF = RECIPE_SERIALIZERS.register("sniff", () -> new SniffRecipe.Serializer<>(SniffRecipe::new));
    public static final RegistryObject<RecipeSerializer<BrushingRecipe>> BRUSH = RECIPE_SERIALIZERS.register("brush", () -> new BrushingRecipe.Serializer<>(BrushingRecipe::new));
    public static RegistryObject<RecipeType<SniffRecipe>> SNIFF_TYPE = RECIPE_TYPES.register("sniff", () -> new RecipeType<>() {
        public String toString() {
            return MODID + ":sniff";
        }
    });
    public static RegistryObject<RecipeType<BrushingRecipe>> BRUSH_TYPE = RECIPE_TYPES.register("brush", () -> new RecipeType<>() {
        public String toString() {
            return MODID + ":brush";
        }
    });

    public JEArchaeology()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::onLootTableLoad);
        MinecraftForge.EVENT_BUS.addListener(this::onDataSync);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        PacketHandler.init();
//        var serverLevel = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD);
//        LOGGER.info("onCommonSetup " + serverLevel);
    }

    private void onLootTableLoad(LootTableLoadEvent event) {
//        var serverLevel = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD);
//        LOGGER.info("loot table event " + event.getName());
//        LOGGER.info("loot table event " + serverLevel);
//        if (serverLevel != null && event.getName().equals(BuiltInLootTables.SNIFFER_DIGGING)) {
//            LootParams lootparams = (new LootParams.Builder(serverLevel)).create(LootContextParamSets.GIFT);
//            List<SniffRecipe> recipes = new ArrayList<>();
//            event.getTable().getRandomItems(lootparams).forEach(itemStack -> {
//                recipes.add(new SniffRecipe(new ResourceLocation(MODID, ForgeRegistries.ITEMS.getKey(itemStack.getItem()).getPath()), Ingredient.of(itemStack), 1D));
//            });
//            LOGGER.info("recipe count " + recipes.size());
//            SniffRecipeCategory.setRecipes(recipes);
//        }
//        if (event.getName().getNamespace().equals("minecraft") && event.getName().getPath().contains("archaeology")) {
////            event.getTable().
//        }
    }

    private void onDataSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            PacketHandler.sendToAllPlayers(new Messages.SnifferDataMessage(SniffRecipeCategory.getAllRecipes(event.getPlayerList().getServer().getLevel(Level.OVERWORLD))));
            PacketHandler.sendToAllPlayers(new Messages.BrushingDataMessage(BrushRecipeCategory.getAllRecipes(event.getPlayerList().getServer().getLevel(Level.OVERWORLD))));
        } else {
            PacketHandler.sendDataToPlayer(new Messages.SnifferDataMessage(SniffRecipeCategory.getAllRecipes(event.getPlayerList().getServer().getLevel(Level.OVERWORLD))), event.getPlayer());
            PacketHandler.sendDataToPlayer(new Messages.BrushingDataMessage(BrushRecipeCategory.getAllRecipes(event.getPlayerList().getServer().getLevel(Level.OVERWORLD))), event.getPlayer());
        }
    }
}
