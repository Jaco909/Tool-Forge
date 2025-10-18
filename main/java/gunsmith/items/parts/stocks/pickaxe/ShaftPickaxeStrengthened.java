package gunsmith.items.parts.stocks.pickaxe;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class ShaftPickaxeStrengthened extends ShaftPickaxe {
    public ShaftPickaxeStrengthened() {
        this.rarity = Rarity.UNCOMMON;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "ShaftPickaxeStrengthenedTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setInt("attackSpeed",450)
                .setFloat("RangeMod",0.2F)
                .setFloat("Rarity",1.5F)
                .setString("Name","Strengthened"));
        return self;
    }
}
