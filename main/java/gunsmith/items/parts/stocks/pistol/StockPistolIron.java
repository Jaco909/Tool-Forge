package gunsmith.items.parts.stocks.pistol;

import gunsmith.items.parts.stocks.smg.StockMachinegun;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class StockPistolIron extends StockPistol {
    public StockPistolIron() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockPistolIronTip"));
        return tooltips;
    }

    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setFloat("Damage",17F)
                .setFloat("AttackSpeed",1F)
                .setFloat("StockDamageUpgrade1",85F)
                .setFloat("StockDamageUpgrade10",153F)
                .setString("Name","Iron")
                .setString("Type","ModularPistol")
                .setString("Part","Stock"));
        return self;
    }
}