package gunsmith.items.parts.stocks.shotgun;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class StockShotgunChilled extends StockShotgun {
    public StockShotgunChilled() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockShotgunChilledTip"));
        return tooltips;
    }

    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setFloat("attackDamage",15F)
                .setInt("attackSpeed",940)
                .setInt("Pellets",4)
                .setFloat("ProjectileSpeedMod",0.3F)
                .setFloat("StockDamageUpgrade1",62F)
                .setFloat("StockDamageUpgrade10",190F)
                .setString("Name","Chilled"));
        return self;
    }
}