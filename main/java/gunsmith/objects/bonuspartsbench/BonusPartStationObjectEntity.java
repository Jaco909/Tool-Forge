package gunsmith.objects.bonuspartsbench;

import necesse.engine.localization.message.GameMessage;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.save.levelData.InventorySave;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.objectEntity.interfaces.OEInventory;
import necesse.inventory.Inventory;
import necesse.inventory.InventoryItem;
import necesse.inventory.InventoryRange;
import necesse.level.maps.Level;

import java.util.ArrayList;

public class BonusPartStationObjectEntity extends ObjectEntity implements OEInventory {
    public Inventory inventory = new Inventory(3);

    public BonusPartStationObjectEntity(Level level, int x, int y) {
        super(level, "bonuspartstation", x, y);
    }

    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addSaveData(InventorySave.getSave(this.inventory, "INVENTORY"));
    }

    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        this.inventory.override(InventorySave.loadSave(save.getFirstLoadDataByName("INVENTORY")));
    }

    public void setupContentPacket(PacketWriter writer) {
        super.setupContentPacket(writer);
        this.inventory.writeContent(writer);
    }

    public void applyContentPacket(PacketReader reader) {
        super.applyContentPacket(reader);
        this.inventory.override(Inventory.getInventory(reader));
    }

    public ArrayList<InventoryItem> getDroppedItems() {
        ArrayList<InventoryItem> list = new ArrayList();

        for (int i = 0; i < this.inventory.getSize(); ++i) {
            if (!this.inventory.isSlotClear(i)) {
                list.add(this.inventory.getItem(i));
            }
        }

        return list;
    }

    public void clientTick() {
        super.clientTick();
        this.inventory.tickItems(this);
    }

    public void serverTick() {
        super.serverTick();
        this.inventory.tickItems(this);
        this.serverTickInventorySync(this.getLevel().getServer(), this);
    }

    public void markClean() {
        super.markClean();
        this.inventory.clean();
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public GameMessage getInventoryName() {
        return this.getObject().getLocalization();
    }

    public boolean canSetInventoryName() {
        return false;
    }

    public boolean canQuickStackInventory() {
        return false;
    }

    public boolean canRestockInventory() {
        return false;
    }

    public boolean canSortInventory() {
        return false;
    }

    public boolean canUseForNearbyCrafting() {
        return false;
    }

    public InventoryRange getSettlementStorage() {
        return null;
    }
}