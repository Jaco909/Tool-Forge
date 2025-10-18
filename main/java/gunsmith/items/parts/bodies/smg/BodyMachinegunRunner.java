package gunsmith.items.parts.bodies.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BodyMachinegunRunner extends BodyMachinegun {

    public BodyMachinegunRunner() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyMachinegunRunnerTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BodyItem",this.getStringID())
                .setString("BodyName","Runner")
                .setFloat("SpreadMod",2.5F)
                .setFloat("MovementMod",99F)
                .setInt("RangeAdd",-280)
                .setString("Type","ModularSMG")
                .setString("Part","Body"));
        return self;
    }
}