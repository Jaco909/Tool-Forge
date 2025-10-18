package gunsmith.items.parts.barrels.pistol;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BarrelPistolGilded extends BarrelPistol {

    public BarrelPistolGilded() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "BarrelPistolGildedTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("MovementMod",0.12F)
                .setFloat("DamageMod",0.86F)
                .setFloat("ProjectileSpeedMod",1.2F)
                .setFloat("Rarity",0.7F)
                .setString("BarrelName","Gilded")
                .setString("Type","ModularPistol")
                .setString("Part","Barrel"));
        return self;
    }
}