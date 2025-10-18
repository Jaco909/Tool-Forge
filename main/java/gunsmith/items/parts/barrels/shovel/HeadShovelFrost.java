package gunsmith.items.parts.barrels.shovel;

import gunsmith.items.parts.barrels.pickaxe.HeadPickaxe;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadShovelFrost extends HeadShovel {
    public HeadShovelFrost() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadShovelFrostTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("toolTier",1)
                .setFloat("attackDamage",8F)
                .setInt("toolDpsFlat",95)
                .setFloat("HeadDPSUpgrade1",245F)
                .setFloat("HeadDPSUpgrade10",588F)
                .setFloat("HeadDamageUpgrade1",74F)
                .setFloat("HeadDamageUpgrade10",144F)
                .setString("BarrelName","Frost"));
        return self;
    }
}
