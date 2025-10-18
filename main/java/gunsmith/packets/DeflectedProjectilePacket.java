package gunsmith.packets;

import necesse.engine.GameLog;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.LevelIdentifier;

public class DeflectedProjectilePacket extends Packet {

    public final int uniqueID;
    public final LevelIdentifier levelID;

    public DeflectedProjectilePacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.uniqueID = reader.getNextInt();
        this.levelID = new LevelIdentifier(reader.getNextString());
    }

    public DeflectedProjectilePacket(int uniqueID, LevelIdentifier levelID) {
        this.uniqueID = uniqueID;
        this.levelID = levelID;
        PacketWriter writer = new PacketWriter(this);
        writer.putNextInt(uniqueID);
        writer.putNextString(levelID.stringID);
    }
    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        server.world.getLevel(this.levelID).entityManager.projectiles.get(uniqueID,true).remove();
    }

    public void processClient(NetworkPacket packet, Client client) {
        if (client.getLevel().getIdentifier() == this.levelID) {
            client.getLevel().entityManager.projectiles.get(this.uniqueID,true).remove();
        }
    }
}