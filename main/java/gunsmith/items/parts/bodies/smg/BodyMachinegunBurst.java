package gunsmith.items.parts.bodies.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BodyMachinegunBurst extends BodyMachinegun {

    public BodyMachinegunBurst() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyMachinegunBurstTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BodyItem",this.getStringID())
                .setString("BodyName","Burst")
                .setInt("ReloadTime",400)
                .setInt("ShotTime",120)
                .setInt("ShotAmount",3)
                .setFloat("DamageMod",0.86F)
                .setFloat("AttackSpeed",1.5F)
                .setString("Type","ModularSMG")
                .setString("Part","Body"));
        return self;
    }
}