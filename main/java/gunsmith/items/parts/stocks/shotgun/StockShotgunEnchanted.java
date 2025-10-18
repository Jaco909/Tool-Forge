package gunsmith.items.parts.stocks.shotgun;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class StockShotgunEnchanted extends StockShotgun {
    public StockShotgunEnchanted() {
        this.rarity = Rarity.COMMON;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockShotgunEnchantedTip"));
        return tooltips;
    }

    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setFloat("attackDamage",29F)
                .setInt("attackSpeed",1000)
                .setInt("Pellets",4)
                .setFloat("SpreadMod",-0.25F)
                .setFloat("StockDamageUpgrade1",60F)
                .setFloat("StockDamageUpgrade10",225F)
                .setFloat("Rarity",0.5F)
                .setString("Name","Enchanted"));
        return self;
    }
}