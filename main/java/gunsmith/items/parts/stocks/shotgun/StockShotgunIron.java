package gunsmith.items.parts.stocks.shotgun;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class StockShotgunIron extends StockShotgun {
    public StockShotgunIron() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockShotgunIronTip"));
        return tooltips;
    }

    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setFloat("attackDamage",18F)
                .setInt("attackSpeed",940)
                .setInt("Pellets",4)
                .setFloat("StockDamageUpgrade1",68F)
                .setFloat("StockDamageUpgrade10",200F)
                .setString("Name","Iron"));
        return self;
    }
}