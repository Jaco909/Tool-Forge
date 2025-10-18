package gunsmith.items.parts.barrels.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BarrelMachinegunHeavy extends BarrelMachinegun {

    public BarrelMachinegunHeavy() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "BarrelMachinegunHeavyTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("DamageMod",1.45F)
                .setFloat("MovementMod",-0.23F)
                .setString("BarrelName","Heavy")
                .setString("Type","ModularSMG")
                .setString("Part","Barrel"));
        return self;
    }
}