package gunsmith.items.parts.barrels.shovel;

import gunsmith.items.parts.barrels.pickaxe.HeadPickaxe;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadShovelMagma extends HeadShovel {
    public HeadShovelMagma() {
        this.rarity = Rarity.RARE;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadShovelMagmaTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("toolTier",10)
                .setInt("attackDamage",27)
                .setFloat("HeadDPSUpgrade1",225F)
                .setFloat("HeadDPSUpgrade10",548F)
                .setFloat("HeadDamageUpgrade1",74F)
                .setFloat("HeadDamageUpgrade10",151F)
                .setInt("toolDpsFlat",205)
                .setFloat("Rarity",3F)
                .setString("BarrelName","Magma"));
        return self;
    }
}
