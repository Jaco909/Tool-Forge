package gunsmith.items.parts.bodies.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BodyMachinegunRipper extends BodyMachinegun {

    public BodyMachinegunRipper() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyMachinegunRipperTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BodyItem",this.getStringID())
                .setString("BodyName","Ripper")
                .setFloat("SpreadMod",1.38F)
                .setFloat("AmmoUseAdd",-0.25F)
                .setString("Type","ModularSMG")
                .setString("Part","Body"));
        return self;
    }
}