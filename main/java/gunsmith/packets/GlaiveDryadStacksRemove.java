package gunsmith.packets;

import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.BuffRegistry;

public class GlaiveDryadStacksRemove extends Packet {

    public final boolean shouldRemove;

    public GlaiveDryadStacksRemove(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.shouldRemove = reader.getNextBoolean();
    }

    public GlaiveDryadStacksRemove(Boolean shouldRemove) {
        this.shouldRemove = shouldRemove;
        PacketWriter writer = new PacketWriter(this);
        writer.putNextBoolean(this.shouldRemove);
    }
    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        if (client.playerMob != null) {
            client.playerMob.buffManager.removeBuff(BuffRegistry.getBuff("GlaiveDryadStacks"), true);
        }
    }

    public void processClient(NetworkPacket packet, Client client) {
    }
}