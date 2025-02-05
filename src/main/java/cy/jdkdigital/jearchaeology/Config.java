package cy.jdkdigital.jearchaeology;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = JEArchaeology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ATM_COMPAT = BUILDER
            .comment("Enable compatibility with ATM")
            .define("atm_compat", true);

    private static final ModConfigSpec.BooleanValue BETTERACHEOLOGY_COMPAT = BUILDER
            .comment("Enable compatibility with Better Archeology")
            .define("betterarcheology_compat", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean atm_compat;
    public static boolean betterarcheology_compat;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        atm_compat = ATM_COMPAT.get();
        betterarcheology_compat = BETTERACHEOLOGY_COMPAT.get();
    }
}
