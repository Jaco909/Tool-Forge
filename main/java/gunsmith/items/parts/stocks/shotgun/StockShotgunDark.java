package gunsmith.items.parts.stocks.shotgun;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class StockShotgunDark extends StockShotgun {
    public StockShotgunDark() {
        this.rarity = Rarity.UNCOMMON;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockShotgunDarkTip"));
        return tooltips;
    }

    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setFloat("attackDamage",37F)
                .setInt("attackSpeed",950)
                .setInt("Pellets",5)
                .setFloat("KnockbackMod",0.5F)
                .setFloat("StockDamageUpgrade1",48F)
                .setFloat("StockDamageUpgrade10",151F)
                .setFloat("Rarity",1F)
                .setString("Name","Dark"));
        return self;
    }
}