package gunsmith.registries;

import gunsmith.packets.*;
import necesse.engine.registries.PacketRegistry;

public class PacketsRegistry {
    public static void register() {
        registerPackets();
    }

    private static void registerPackets(){
        PacketRegistry.registerPacket(RequestItemPacket.class);
        PacketRegistry.registerPacket(GlaiveDryadStacksRemove.class);
        PacketRegistry.registerPacket(JenWallHitPacket.class);
        PacketRegistry.registerPacket(SaveModifierPacket.class);
        PacketRegistry.registerPacket(DeflectedProjectilePacket.class);
        PacketRegistry.registerPacket(ReflectedProjectilePacket.class);
    }
}
