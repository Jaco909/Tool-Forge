package gunsmith.items.parts.bodies.pistol;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BodyPistolMagic extends BodyPistol {

    public BodyPistolMagic() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyPistolMagicTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BodyItem",this.getStringID())
                .setFloat("manaCost",5F)
                .setFloat("Rarity",3.2F)
                .setString("BodyName","Magic")
                .setString("Type","ModularPistol")
                .setString("Part","Body"));
        return self;
    }
}