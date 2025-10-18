package gunsmith.items.parts.barrels.shovel;

import gunsmith.items.parts.barrels.pickaxe.HeadPickaxe;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadShovelPlanetary extends HeadShovel {
    public HeadShovelPlanetary() {
        this.rarity = Rarity.EPIC;
    }

    /*protected ListGameTooltips getDisplayNameTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = new ListGameTooltips();
        tooltips.add(new StringTooltips(this.getDisplayName(item), this.getRarityColor(item)));
        if (perspective != null && !perspective.getClient().characterStats.items_obtained.isItemObtained(this.getStringID())) {
            tooltips.add(Localization.translate("loottooltip", "HeadPickaxePlanetary"));
        }
        return tooltips;
    }*/

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadShovelPlanetaryTip"));
        tooltips.add(Localization.translate("flavortooltip", "HeadShovelPlanetary"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("toolTier",11)
                .setInt("attackDamage",25)
                .setFloat("HeadDPSUpgrade1",320F)
                .setFloat("HeadDPSUpgrade10",600F)
                .setFloat("HeadDamageUpgrade1",74F)
                .setFloat("HeadDamageUpgrade10",149F)
                .setFloat("Rarity",3F)
                .setInt("toolDpsFlat",300)
                .setString("BarrelName","Planetary"));
        return self;
    }
}
