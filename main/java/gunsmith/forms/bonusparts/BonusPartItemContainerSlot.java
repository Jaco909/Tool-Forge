package gunsmith.forms.bonusparts;

import necesse.engine.localization.Localization;
import necesse.inventory.Inventory;
import necesse.inventory.InventoryItem;
import necesse.inventory.container.slots.ContainerSlot;

public class BonusPartItemContainerSlot extends ContainerSlot {
    private int slot;
    public BonusPartItemContainerSlot(Inventory inventory, int inventorySlot) {
        super(inventory, inventorySlot);
        this.slot = inventorySlot;
    }

    public String getItemInvalidError(InventoryItem item) {
        if (item == null) {
            return null;
        } else {
            if (item.item.getDefaultItem(null,1).getGndData().hasKey("Part")) {
                return null;
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