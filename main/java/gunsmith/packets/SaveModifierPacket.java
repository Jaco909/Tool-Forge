package gunsmith.packets;

import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;

public class SaveModifierPacket extends Packet {
    public final int slot;
    public final PlayerInventorySlot inventorySlot;
    public final Packet itemContent;

    public SaveModifierPacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.slot = reader.getNextByteUnsigned();
        int inventoryID = reader.getNextShortUnsigned();
        int inventorySlot = reader.getNextShortUnsigned();
        this.inventorySlot = new PlayerInventorySlot(inventoryID, inventorySlot);
        this.itemContent = reader.getNextContentPacket();
    }

    public SaveModifierPacket(int slot, PlayerMob player, PlayerInventorySlot invSlot) {
        this.slot = slot;
        this.inventorySlot = invSlot;
        this.itemContent = InventoryItem.getContentPacket(player.getInv().getItem(invSlot));
        PacketWriter writer = new PacketWriter(this);
        writer.putNextByteUnsigned(slot);
        writer.putNextShortUnsigned(this.inventorySlot.inventoryID);
        writer.putNextShortUnsigned(this.inventorySlot.slot);
        writer.putNextContentPacket(this.itemContent);
    }

    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        client.playerMob.getInv().setItem(this.inventorySlot, InventoryItem.fromContentPacket(this.itemContent));
    }
}