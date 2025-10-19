package gunsmith.items.parts.bodies.pistol;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.matItem.MatItem;

public class BodyPistol extends MatItem {

    public BodyPistol() {
        super(1, Rarity.COMMON);
        this.setItemCategory(new String[]{"parts", "pistol", "pistolBody"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"pistolBody"});
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyTip"));
        tooltips.add(Localization.translate("weapontooltip", "PistolTip"));
        return tooltips;
    }
}