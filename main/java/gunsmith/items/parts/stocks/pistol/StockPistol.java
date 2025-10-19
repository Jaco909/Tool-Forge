package gunsmith.items.parts.stocks.pistol;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.matItem.MatItem;

public class StockPistol extends MatItem {

    public StockPistol() {
        super(1, Rarity.COMMON);
        this.setItemCategory(new String[]{"parts", "pistol", "pistolStock"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"pistolStock"});
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockTip"));
        tooltips.add(Localization.translate("weapontooltip", "PistolTip"));
        return tooltips;
    }
}