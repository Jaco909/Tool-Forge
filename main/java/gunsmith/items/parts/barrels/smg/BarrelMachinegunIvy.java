package gunsmith.items.parts.barrels.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BarrelMachinegunIvy extends BarrelMachinegun {

    public BarrelMachinegunIvy() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "BarrelMachinegunIvyTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setInt("RangeAdd",200)
                .setFloat("DamageMod",0.85F)
                .setFloat("SpreadMod",0.2F)
                .setString("BarrelName","Ivy")
                .setString("Type","ModularSMG")
                .setString("Part","Barrel"));
        return self;
    }
}