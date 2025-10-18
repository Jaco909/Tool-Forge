package gunsmith.items.parts.bodies.shovel;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BodyShovelGrass extends BodyShovel {
    public BodyShovelGrass() {
        this.rarity = Rarity.COMMON;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyShovelGrassTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setFloat("extraRange",-1)
                .setFloat("RangeMod",-0.2F)
                .setString("BodyItem",this.getStringID())
                .setString("BodyName","Grass")
                .setFloat("Rarity",1F));
        return self;
    }
}
