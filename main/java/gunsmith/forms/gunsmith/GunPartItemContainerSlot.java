package gunsmith.forms.gunsmith;

import necesse.engine.localization.Localization;
import necesse.inventory.Inventory;
import necesse.inventory.InventoryItem;
import necesse.inventory.container.slots.ContainerSlot;

public class GunPartItemContainerSlot extends ContainerSlot {
    private int slot;
    public GunPartItemContainerSlot(Inventory inventory, int inventorySlot) {
        super(inventory, inventorySlot);
        this.slot = inventorySlot;
    }

    public String getItemInvalidError(InventoryItem item) {
        if (item == null) {
            return null;
        } else {
            if (item.item.getDefaultItem(null,1).getGndData().hasKey("Part")) {
                if (item.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Body") && this.slot == 1) {
                    return null;
                } else if (item.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Stock") && this.slot == 2) {
                    return null;
                } else if (item.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Barrel") && this.slot == 0) {
                    return null;
                } else {
                    return Localization.translate("ui", "wrongSlot");
                }
            } else {
                return Localization.translate("ui", "notPart");
            }
        }
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}