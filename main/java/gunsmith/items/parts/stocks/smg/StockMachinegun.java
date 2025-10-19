package gunsmith.items.parts.stocks.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.matItem.MatItem;

public class StockMachinegun extends MatItem {

    public StockMachinegun() {
        super(1, Rarity.COMMON);
        this.setItemCategory(new String[]{"parts", "machinegun", "machinegunStock"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"machinegunStock"});
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockTip"));
        tooltips.add(Localization.translate("weapontooltip", "MachinegunTip"));
        return tooltips;
    }
}