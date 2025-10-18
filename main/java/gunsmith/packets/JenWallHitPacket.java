package gunsmith.packets;

import necesse.engine.commands.PermissionLevel;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameUtils;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.objectEntity.ObjectEntity;

public class JenWallHitPacket extends Packet {

    public final int mobUniqueID;
    public final int mobHealth;
    public final Packet buffContent;

    public JenWallHitPacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.mobUniqueID = reader.getNextInt();
        this.mobHealth = reader.getNextInt();
        this.buffContent = reader.getNextContentPacket();
    }

    public JenWallHitPacket(int mobUniqueID, int mobHealth, ActiveBuff ab) {
        this.mobUniqueID = mobUniqueID;
        this.mobHealth = mobUniqueID;
        this.buffContent = ab.getContentPacket();
        PacketWriter writer = new PacketWriter(this);
        writer.putNextInt(this.mobUniqueID);
        writer.putNextInt(this.mobHealth);
        writer.putNextContentPacket(this.buffContent);
    }

    public ActiveBuff getBuff(Mob owner) {
        return ActiveBuff.fromContentPacket(this.buffContent, owner);
    }

    public void processServer(NetworkPacket packet, Server server, ServerClient client) {

    }

    public void processClient(NetworkPacket packet, Client client) {

    }
}