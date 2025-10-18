package gunsmith.items.parts.barrels.pickaxe;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadPickaxeCopper extends HeadPickaxe {
    public HeadPickaxeCopper() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadPickaxeCopperTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("toolTier",0)
                .setInt("attackDamage",18)
                .setFloat("HeadDPSUpgrade1",245F)
                .setFloat("HeadDPSUpgrade10",568F)
                .setFloat("HeadDamageUpgrade1",84F)
                .setFloat("HeadDamageUpgrade10",159F)
                .setInt("toolDpsFlat",65)
                .setString("BarrelName","Copper"));
        return self;
    }
}
