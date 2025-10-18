package gunsmith.items.parts.stocks.pickaxe;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class ShaftPickaxeSlime extends ShaftPickaxe {
    public ShaftPickaxeSlime() {
        this.rarity = Rarity.EPIC;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "ShaftPickaxeSlimeTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setInt("attackSpeed",400)
                .setInt("extraRange",1)
                .setFloat("Rarity",2.5F)
                .setString("Name","Slime"));
        return self;
    }
}
