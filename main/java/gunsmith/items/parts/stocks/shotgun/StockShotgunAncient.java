package gunsmith.items.parts.stocks.shotgun;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class StockShotgunAncient extends StockShotgun {
    public StockShotgunAncient() {
        this.rarity = Rarity.EPIC;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockShotgunAncientTip"));
        return tooltips;
    }

    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setFloat("attackDamage",38F)
                .setInt("attackSpeed",600)
                .setInt("Pellets",4)
                .setFloat("Rarity",2F)
                .setFloat("StockDamageUpgrade1",40F)
                .setFloat("StockDamageUpgrade10",135F)
                .setString("Name","Ancient"));
        return self;
    }
}