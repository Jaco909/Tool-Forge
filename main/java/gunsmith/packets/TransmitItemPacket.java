package gunsmith.packets;

import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;

public class TransmitItemPacket extends Packet {

    public final String baseItem;
    public final String barrelItem;
    public final String bodyItem;
    public final String stockItem;

    public TransmitItemPacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.baseItem = reader.getNextString();
        this.barrelItem = reader.getNextString();
        this.bodyItem = reader.getNextString();
        this.stockItem = reader.getNextString();
    }

    public TransmitItemPacket(String baseItem, String barrelItem, String bodyItem, String stockItem) {
        this.baseItem = baseItem;
        this.barrelItem = barrelItem;
        this.bodyItem = bodyItem;
        this.stockItem = stockItem;
        PacketWriter writer = new PacketWriter(this);
        writer.putNextString(this.baseItem);
        writer.putNextString(this.barrelItem);
        writer.putNextString(this.bodyItem);
        writer.putNextString(this.stockItem);
    }
    public void processServer(NetworkPacket packet, Server server, ServerClient client) {

    }

    public void processClient(NetworkPacket packet, Client client) {

    }
}