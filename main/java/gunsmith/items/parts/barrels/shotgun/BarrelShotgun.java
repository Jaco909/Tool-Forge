package gunsmith.items.parts.barrels.shotgun;

import gunsmith.items.parts.PartMatItem;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.matItem.MatItem;

import java.awt.*;

public class BarrelShotgun extends PartMatItem {

    public BarrelShotgun() {
        this.setItemCategory(new String[]{"parts", "shotgun", "shotgunBarrel"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"shotgunBarrel"});
    }
    public void drawIcon(InventoryItem item, PlayerMob perspective, int x, int y, int size, Color color) {
        if (color != null) {
            color = MergeFunction.MULTIPLY.merge(color, this.getDrawColor(item, perspective));
        } else {
            color = this.getDrawColor(item, perspective);
        }

        new GameSprite(GameTexture.fromFile("items/ShotgunBarrelOutline")).initDraw().color(color).size(size).draw(x, y);
        this.getItemSprite(item, perspective).initDraw().color(color).size(size).draw(x, y);
    }
    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "BarrelTip"));
        tooltips.add(Localization.translate("weapontooltip", "ShotgunTip"));
        //bonus display
        if (item.getGndData().hasKey("BonusActivate")) {
            tooltips.add(Localization.translate("bonusmodtip", "Random"));
        }
        if (item.getGndData().hasKey("BarrelBonus")) {
            if (item.getGndData().getString("BodyBonus").equalsIgnoreCase("BonusAttackSpeed")) {
                if (item.getGndData().getFloat("BarrelBonusValue") > 0) {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue")/10, "unit", "+"));
                } else {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue")/10, "unit", ""));
                }
            } else {
                if (item.getGndData().getFloat("BarrelBonusValue") > 0) {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue"), "unit", "+"));
                } else {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue"), "unit", ""));
                }
            }
        }
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setFloat("LowBonusPellets",1F)
                .setFloat("HighBonusPellets",2F)
                .setFloat("LowBonusVelocityMod",7F)
                .setFloat("HighBonusVelocityMod",30F)
                .setFloat("LowBonusAmmoUseAdd",-5)
                .setFloat("HighBonusAmmoUseAdd",-20)
                .setFloat("LowBonusSpreadMod",-8F)
                .setFloat("HighBonusSpreadMod",-33F)
                .setString("Type","ModularShotgun")
                .setString("Part","Barrel"));
        return self;
    }
}