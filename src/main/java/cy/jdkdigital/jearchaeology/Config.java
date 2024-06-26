package cy.jdkdigital.jearchaeology;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = JEArchaeology.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ATM_COMPAT = BUILDER
            .comment("Enable compatibility with ATM")
            .define("atm_compat", true);

    private static final ForgeConfigSpec.BooleanValue BETTERACHEOLOGY_COMPAT = BUILDER
            .comment("Enable compatibility with Better Archeology")
            .define("betterarcheology_compat", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean atm_compat;
    public static boolean betterarcheology_compat;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        atm_compat = ATM_COMPAT.get();
        betterarcheology_compat = BETTERACHEOLOGY_COMPAT.get();
    }
}
