package gunsmith.items.parts.bodies.shotgun;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class BodyShotgunDoubleBarrel extends BodyShotgun {

    public BodyShotgunDoubleBarrel() {
        this.rarity = Rarity.EPIC;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyShotgunDoubleBarrelTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BodyItem",this.getStringID())
                .setFloat("SpreadMod",0.6F)
                .setFloat("DamageMod",-0.15F)
                .setInt("ReloadTime",920)
                .setInt("ShotTime",170)
                .setInt("ShotAmount",2)
                .setFloat("RangeMod",-0.25F)
                .setFloat("Rarity",2F)
                .setFloat("attackSpeedMod",0.10F)
                .setString("BodyName","Burst"));
        return self;
    }
}