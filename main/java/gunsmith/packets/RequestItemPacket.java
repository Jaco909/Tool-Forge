package gunsmith.packets;

import gunsmith.gunsmith;
import necesse.engine.GameLog;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.GameMessageBuilder;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ItemRegistry;
import necesse.entity.pickup.PickupEntity;
import necesse.gfx.fairType.FairType;
import necesse.gfx.forms.components.chat.ChatMessage;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.inventory.*;
import necesse.inventory.container.mob.PawnbrokerContainer;
import necesse.level.maps.Level;

import java.awt.*;
import java.io.File;
import java.util.Iterator;

public class RequestItemPacket extends Packet {

    public final int requestSlot;
    public final String baseItem;
    public final Packet baseItemGND;

    public RequestItemPacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.requestSlot = reader.getNextInt();
        this.baseItem = reader.getNextString();
        this.baseItemGND = reader.getNextContentPacket();
    }

    public RequestItemPacket(int requestSlot, InventoryItem baseItem) {
        this.requestSlot = requestSlot;
        this.baseItem = baseItem.item.getStringID();
        this.baseItemGND = baseItem.getGndData().getContentPacket();
        PacketWriter writer = new PacketWriter(this);
        writer.putNextInt(this.requestSlot);
        writer.putNextString(this.baseItem);
        writer.putNextContentPacket(this.baseItemGND);
    }
    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        server.network.sendToClientsAtEntireLevel(this,server.getPlayer(this.requestSlot).getLevel());
    }

    public void processClient(NetworkPacket packet, Client client) {
        InventoryItem item = new InventoryItem(ItemRegistry.getItem(this.baseItem),1);
        GNDItemMap gndData = new GNDItemMap(this.baseItemGND);
        item.setGndData(gndData);

        StringBuilder displayString = new StringBuilder("[item=" + item.item.getStringID() + "]{");
        StringBuilder displayStringComp = new StringBuilder("[item=" + item.item.getStringID() + "]{");
        Iterator<Integer> GNDKeys = item.getGndData().getDirtyKeys().iterator();
        while (GNDKeys.hasNext()) {
            Integer keyValue = GNDKeys.next();
            displayString.append(keyValue).append("={");
            displayStringComp.append(keyValue).append("={");
            displayString.append("gndType=").append(item.getGndData().getItem(keyValue).getStringID()).append(",");
            displayStringComp.append("gndType=").append(item.getGndData().getItem(keyValue).getStringID()).append(",");
            if (item.getGndData().getItem(keyValue).getStringID().equalsIgnoreCase("float")) {
                displayString.append("value=").append(item.getGndData().getItem(keyValue)).append("}");
                displayStringComp.append("value=").append(item.getGndData().getItem(keyValue)).deleteCharAt(displayStringComp.lastIndexOf("f")).append("}");
            } else {
                displayString.append("value=").append(item.getGndData().getItem(keyValue)).append("}");
                displayStringComp.append("value=").append(item.getGndData().getItem(keyValue)).append("}");
            }
            if (GNDKeys.hasNext()) {
                displayString.append(",");
                displayStringComp.append(",");
            }
        }
        displayString.append("}");
        displayStringComp.append("}");
        String display = displayString.toString();

        boolean found = false;
        FairType type = new FairType();
        type.append(ChatMessage.fontOptions, "§#f7b345ToolForge MP Beta Sync§#FFFFFF: " + display);
        ChatMessage message = new ChatMessage(type);
        Iterator messages = client.chat.iterator();
        while (messages.hasNext()) {
            message = (ChatMessage) messages.next();
            if (message.type.getParseString().equalsIgnoreCase(displayStringComp.toString())) {
                found = true;
                client.chat.removeMessage(message);
                break;
            }
        }
        for (int i = 0; i < 11; i++) {
            if (client.getPlayer().getInv().main.getItem(i) == item) {
                found = true;
                break;
            }
        }
        //FairType type = new FairType();
        //type.append(ChatMessage.fontOptions, "§#f7b345ToolForge MP Beta Sync§#FFFFFF: " + display);
        //ChatMessage message = new ChatMessage(type);
        if (!found) {
            client.chat.addMessage(type);
            client.chat.removeMessage(message);


            //GameMessage message = GameMessage.getNewMessage("§#f7b345ToolForge MP Beta Sync§#FFFFFF: " + display);
            //client.chat.addMessage("§#f7b345ToolForge MP Beta Sync§#FFFFFF: " + display);
            //client.getMessage().
        }

        //I think this is the bad line
        client.chat.removeMessage(message);
    }
}