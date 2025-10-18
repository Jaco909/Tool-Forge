package gunsmith.items.parts.barrels.pistol;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.matItem.MatItem;

public class BarrelPistol extends MatItem {

    public BarrelPistol() {
        super(1, Rarity.COMMON);
        this.setItemCategory(new String[]{"pistolBarrel"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"pistolBarrel"});
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "BarrelTip"));
        tooltips.add(Localization.translate("weapontooltip", "PistolTip"));
        return tooltips;
    }
}