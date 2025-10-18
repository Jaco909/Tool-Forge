package gunsmith.items.parts.bodies.shotgun;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BodyShotgunStuffed extends BodyShotgun {

    public BodyShotgunStuffed() {
        this.rarity = Rarity.COMMON;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyShotgunStuffedTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BodyItem",this.getStringID())
                .setInt("Pellets",3)
                .setFloat("Rarity",1F)
                .setFloat("attackSpeedMod",-0.4F)
                .setString("BodyName","Stuffed"));
        return self;
    }
}