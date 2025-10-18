package gunsmith.items.parts.barrels.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BarrelMachinegunGold extends BarrelMachinegun {

    public BarrelMachinegunGold() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "BarrelMachinegunGoldTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("DamageMod",0.9F)
                .setInt("RangeAdd",160)
                .setFloat("SpreadMod",0.88F)
                .setString("BarrelName","Gold")
                .setString("Type","ModularSMG")
                .setString("Part","Barrel"));
        return self;
    }
}