package gunsmith.items.parts.bodies.shovel;

import gunsmith.items.parts.bodies.pickaxe.BodyPickaxe;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BodyShovelBlock extends BodyShovel {
    public BodyShovelBlock() {
        this.rarity = Rarity.UNCOMMON;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyShovelBlockTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BodyItem",this.getStringID())
                .setString("BodyName","Block")
                .setFloat("attackSpeedMod",0.3F)
                .setFloat("DamageMod",0.35F)
                .setFloat("toolDpsMod",-0.50F)
                .setFloat("extraRange",-2)
                .setFloat("Rarity",2F));
        return self;
    }
}
