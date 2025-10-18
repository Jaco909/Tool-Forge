package gunsmith.items.parts.barrels.pickaxe;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadPickaxeGold extends HeadPickaxe {
    public HeadPickaxeGold() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadPickaxeGoldTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("toolTier",0)
                .setInt("attackDamage",10)
                .setFloat("HeadDPSUpgrade1",245F)
                .setFloat("HeadDPSUpgrade10",588F)
                .setFloat("HeadDamageUpgrade1",84F)
                .setFloat("HeadDamageUpgrade10",150F)
                .setInt("toolDpsFlat",95)
                .setString("BarrelName","Gold"));
        return self;
    }
}
