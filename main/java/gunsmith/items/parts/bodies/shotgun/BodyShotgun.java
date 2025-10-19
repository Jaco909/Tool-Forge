package gunsmith.items.parts.bodies.shotgun;

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

public class BodyShotgun extends PartMatItem {

    public BodyShotgun() {
        this.setItemCategory(new String[]{"parts", "shotgun", "shotgunBody"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"shotgunBody"});
    }
    public void drawIcon(InventoryItem item, PlayerMob perspective, int x, int y, int size, Color color) {
        if (color != null) {
            color = MergeFunction.MULTIPLY.merge(color, this.getDrawColor(item, perspective));
        } else {
            color = this.getDrawColor(item, perspective);
        }

        new GameSprite(GameTexture.fromFile("items/ShotgunBodyOutline")).initDraw().color(color).size(size).draw(x, y);
        this.getItemSprite(item, perspective).initDraw().color(color).size(size).draw(x, y);
    }
    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("bodytooltip", "BodyTip"));
        tooltips.add(Localization.translate("weapontooltip", "ShotgunTip"));
        //bonus display
        if (item.getGndData().hasKey("BonusActivate")) {
            tooltips.add(Localization.translate("bonusmodtip", "Random"));
        }
        if (item.getGndData().hasKey("BodyBonus")) {
            String color = "§#07BFE8";
            if (item.getGndData().getFloat("BodyBonusValue") >= item.item.getDefaultItem(null,1).getGndData().getFloat("High" + item.getGndData().getString("BodyBonus"))) {
                color = "§#E655E6";
            }
            if (item.getGndData().getString("BodyBonus").equalsIgnoreCase("BonusAttackSpeed")) {
                if (item.getGndData().getFloat("BodyBonusValue") > 0) {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BodyBonus"), "value", item.getGndData().getFloat("BodyBonusValue")/10, "unit", "+","color",color));
                } else {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BodyBonus"), "value", item.getGndData().getFloat("BodyBonusValue")/10, "unit", "","color",color));
                }
            } else {
                if (item.getGndData().getFloat("BodyBonusValue") > 0) {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BodyBonus"), "value", item.getGndData().getFloat("BodyBonusValue"), "unit", "+","color",color));
                } else {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BodyBonus"), "value", item.getGndData().getFloat("BodyBonusValue"), "unit", "","color",color));
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
                .setString("Part","Body"));
        return self;
    }
}