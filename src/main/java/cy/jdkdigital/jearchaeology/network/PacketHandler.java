package cy.jdkdigital.jearchaeology.network;

import cy.jdkdigital.jearchaeology.JEArchaeology;
import cy.jdkdigital.jearchaeology.network.packets.Messages;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler
{
    private static int id = 0;
    private static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel channel;

    public static void init() {
        channel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(JEArchaeology.MODID, "jea"))
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();

        channel.messageBuilder(Messages.SnifferDataMessage.class, getId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Messages.SnifferDataMessage::decode)
                .encoder(Messages.SnifferDataMessage::encode)
                .consumerNetworkThread(Messages.SnifferDataMessage::handle)
                .add();

        channel.messageBuilder(Messages.BrushingDataMessage.class, getId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Messages.BrushingDataMessage::decode)
                .encoder(Messages.BrushingDataMessage::encode)
                .consumerNetworkThread(Messages.BrushingDataMessage::handle)
                .add();
    }

    public static int getId() {
        return ++id;
    }

    public static void sendDataToPlayer(Messages.RecipeMessage message, ServerPlayer player) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendToAllPlayers(Messages.RecipeMessage message) {
        channel.send(PacketDistributor.ALL.noArg(), message);
    }
}
