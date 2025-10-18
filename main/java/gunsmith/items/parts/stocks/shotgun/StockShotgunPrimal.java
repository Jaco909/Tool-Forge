package gunsmith.items.parts.stocks.shotgun;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.inventory.InventoryItem;

import java.awt.*;

public class StockShotgunPrimal extends StockShotgun {
    public StockShotgunPrimal() {
        this.rarity = Rarity.RARE;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = this.getBaseTooltips(item, perspective, blackboard);
        tooltips.add(new StringTooltips(Localization.translate("item", "tier", "tiernumber", 1), new Color(133, 49, 168)));
        tooltips.add(Localization.translate("stocktooltip", "StockTip"));
        tooltips.add(Localization.translate("weapontooltip", "ShotgunTip"));
        tooltips.add(Localization.translate("stocktooltip", "StockShotgunPrimalTip"));
        tooltips.add(Localization.translate("itemtooltip", "AutoUpgradeTip","tier",1));
        return tooltips;
    }

    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setFloat("attackDamage",76F)
                .setInt("attackSpeed",860)
                .setInt("Pellets",3)
                .setFloat("StockDamageUpgrade1",76F)
                .setFloat("StockDamageUpgrade10",291F)
                .setFloat("Rarity",2.5F)
                .setInt("upgradeLevel",100)
                .setString("Name","Primal"));
        return self;
    }
}