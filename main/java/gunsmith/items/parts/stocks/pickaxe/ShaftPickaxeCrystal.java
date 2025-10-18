package gunsmith.items.parts.stocks.pickaxe;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class ShaftPickaxeCrystal extends ShaftPickaxe {
    public ShaftPickaxeCrystal() {
        this.rarity = Rarity.EPIC;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "ShaftPickaxeCrystalTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setInt("attackSpeed",400)
                .setFloat("CritChance",0.3F)
                .setFloat("Rarity",2.5F)
                .setString("Name","Crystal"));
        return self;
    }
}
