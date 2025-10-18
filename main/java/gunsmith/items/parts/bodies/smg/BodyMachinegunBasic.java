package gunsmith.items.parts.bodies.smg;

import gunsmith.items.parts.bodies.shotgun.BodyShotgun;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BodyMachinegunBasic extends BodyMachinegun {

    public BodyMachinegunBasic() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyMachinegunBasicTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BodyItem",this.getStringID())
                .setString("BodyName","Basic")
                .setString("Type","ModularSMG")
                .setString("Part","Body"));
        return self;
    }
}