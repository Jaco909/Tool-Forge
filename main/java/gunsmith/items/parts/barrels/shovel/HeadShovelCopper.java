package gunsmith.items.parts.barrels.shovel;

import gunsmith.items.parts.barrels.pickaxe.HeadPickaxe;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadShovelCopper extends HeadShovel {
    public HeadShovelCopper() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadShovelCopperTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("toolTier",0)
                .setInt("attackDamage",12)
                .setFloat("HeadDPSUpgrade1",245F)
                .setFloat("HeadDPSUpgrade10",568F)
                .setFloat("HeadDamageUpgrade1",79F)
                .setFloat("HeadDamageUpgrade10",148F)
                .setInt("toolDpsFlat",65)
                .setString("BarrelName","Copper"));
        return self;
    }
}
