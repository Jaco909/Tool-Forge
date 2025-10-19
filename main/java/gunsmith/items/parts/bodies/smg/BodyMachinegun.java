package gunsmith.items.parts.bodies.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.matItem.MatItem;

public class BodyMachinegun extends MatItem {

    public BodyMachinegun() {
        super(1, Rarity.COMMON);
        this.setItemCategory(new String[]{"parts", "machinegun", "machinegunBody"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"machinegunBody"});
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyTip"));
        tooltips.add(Localization.translate("weapontooltip", "MachinegunTip"));
        return tooltips;
    }
}