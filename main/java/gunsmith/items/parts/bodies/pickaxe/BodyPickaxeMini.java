package gunsmith.items.parts.bodies.pickaxe;

import necesse.engine.localization.Localization;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BodyPickaxeMini extends BodyPickaxe {
    public BodyPickaxeMini() {
        this.rarity = Rarity.COMMON;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyPickaxeMiniTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BodyItem",this.getStringID())
                .setString("BodyName","Mini")
                .setFloat("attackSpeedMod",1.47F)
                .setFloat("RangeMod",-0.65F)
                .setInt("extraRange",-2)
                .setFloat("DamageMod",-0.50F)
                .setFloat("toolDpsMod",-0.50F)
                .setFloat("Rarity",0.5F));
        return self;
    }
}
