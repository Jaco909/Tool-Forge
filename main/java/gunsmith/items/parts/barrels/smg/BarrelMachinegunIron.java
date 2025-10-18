package gunsmith.items.parts.barrels.smg;

import gunsmith.items.parts.barrels.shotgun.BarrelShotgun;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BarrelMachinegunIron extends BarrelMachinegun {

    public BarrelMachinegunIron() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "BarrelMachinegunIronTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setString("BarrelName","Iron")
                .setString("Type","ModularSMG")
                .setString("Part","Barrel"));
        return self;
    }
}