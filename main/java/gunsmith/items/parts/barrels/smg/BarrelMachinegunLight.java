package gunsmith.items.parts.barrels.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BarrelMachinegunLight extends BarrelMachinegun {

    public BarrelMachinegunLight() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "BarrelMachinegunLightTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("DamageMod",0.85F)
                .setFloat("MovementMod",0.15F)
                .setString("BarrelName","Light")
                .setString("Type","ModularSMG")
                .setString("Part","Barrel"));
        return self;
    }
}