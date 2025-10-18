package gunsmith.items.ammo.bullets;

import necesse.engine.input.Control;
import necesse.engine.localization.Localization;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.entity.projectile.bulletProjectile.BouncingBulletProjectile;
import necesse.entity.projectile.bulletProjectile.HandGunBulletProjectile;
import necesse.gfx.GameColor;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.InventorySlot;
import necesse.inventory.item.Item;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.bulletItem.BulletItem;

import java.awt.*;
import java.util.Iterator;

public class ModularBullet extends BulletItem {
    public int damage;
    public int armorPen;
    public float critChance;

    public ModularBullet(int stackAmount) {
        super(5000);
        this.setItemCategory(new String[]{"equipment", "ammo"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"equipment", "ammo"});
        this.keyWords.add("bullet");
    }

    public ModularBullet() {
        this(5000);
        this.damage = 1;
        this.rarity = Rarity.EPIC;
    }

    protected ListGameTooltips getDisplayNameTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = new ListGameTooltips();
        if (!item.getGndData().hasKey("HeadItem")) {
            //recipe display
            tooltips.add(new StringTooltips("Bullet Template", new Color(255,255,255)));
            tooltips.add(new StringTooltips("I see you cheating :)", new Color(255, 0, 0)));
        } else {
            GameColor tiercolor = GameColor.ITEM_NORMAL;
            tooltips.add(new StringTooltips(Localization.translate("item", item.getGndData().getString("HeadItem") + "Name") + " " + Localization.translate("item", item.getGndData().getString("EndItem") + "Name") + " " + Localization.translate("item", this.getStringID()), tiercolor));
        }
        return tooltips;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = this.getBaseTooltips(item, perspective, blackboard);
        if (!item.getGndData().hasKey("HeadItem")) {
            //recipe view
            return tooltips;
        } else {
            InventoryItem headItem = ItemRegistry.getItem(item.getGndData().getString("HeadItem","debug")).getDefaultItem(perspective,1);
            InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","debug")).getDefaultItem(perspective,1);
            InventoryItem endItem = ItemRegistry.getItem(item.getGndData().getString("EndItem","debug")).getDefaultItem(perspective,1);
            tooltips.add(Localization.translate("itemtooltip", "damagetip", "value", (this.damage+headItem.getGndData().getFloat("BaseDamage",0)+bodyItem.getGndData().getFloat("BaseDamage",0)+endItem.getGndData().getFloat("BaseDamage",0))*headItem.getGndData().getFloat("DamageMod",1)*bodyItem.getGndData().getFloat("DamageMod",1)*endItem.getGndData().getFloat("DamageMod",1)));
            if (headItem.getGndData().hasKey("Tooltip")) {
                tooltips.add(Localization.translate("bullettooltip", headItem.getGndData().getString("Tooltip","debug")));
            }
            if (bodyItem.getGndData().hasKey("Tooltip")) {
                tooltips.add(Localization.translate("bullettooltip", bodyItem.getGndData().getString("Tooltip","debug")));
            }
            if (endItem.getGndData().hasKey("Tooltip")) {
                tooltips.add(Localization.translate("bullettooltip", endItem.getGndData().getString("Tooltip","debug")));
            }
            if (Control.INV_QUICK_TRASH.isDown()) {
                tooltips.add(Localization.translate("itemtooltip", "Tip") + " " + Localization.translate("item", item.getGndData().getString("HeadItem")+"Name"));
                tooltips.add(Localization.translate("itemtooltip", "Primer") + " " + Localization.translate("item", item.getGndData().getString("BodyItem")+"Name"));
                tooltips.add(Localization.translate("itemtooltip", "Shell") + " " + Localization.translate("item", item.getGndData().getString("EndItem")+"Name"));
            } else if (Control.INV_QUICK_MOVE.isDown()) {
                tooltips.add(Localization.translate("bullettooltip", "HeadTip"));
                tooltips.add(Localization.translate("bullettooltip", item.getGndData().getString("HeadItem")+"Tip"));
                tooltips.add(Localization.translate("bullettooltip", "BodyTip"));
                tooltips.add(Localization.translate("bullettooltip", item.getGndData().getString("BodyItem")+"Tip"));
                tooltips.add(Localization.translate("bullettooltip", "EndTip"));
                tooltips.add(Localization.translate("bullettooltip", item.getGndData().getString("EndItem")+"Tip"));
            } else {
                tooltips.add(Localization.translate("itemtooltip", "holdShift", "shift", "invquickmove"));
                tooltips.add(Localization.translate("itemtooltip", "holdCtrl", "ctrl", "invtrash"));
            }
            return tooltips;
        }
    }

    public void draw(InventoryItem item, PlayerMob perspective, int x, int y, boolean inInventory) {
        super.draw(item, perspective, x, y, inInventory);
    }

    public void drawIcon(InventoryItem item, PlayerMob perspective, int x, int y, int size, Color color) {
        if (color != null) {
            color = MergeFunction.MULTIPLY.merge(color, this.getDrawColor(item, perspective));
        } else {
            color = this.getDrawColor(item, perspective);
        }
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("HeadItem", "BarrelShotgunIron"))).initDraw().color(color).size(size).depth(-1F).draw(x, y);
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("EndItem", "StockShotgunIron"))).initDraw().color(color).size(size).draw(x, y);
    }

    public GameSprite getAttackSprite(InventoryItem item, PlayerMob player) {
        return new GameSprite(GameTexture.fromFile("items/simplebullet"));
    }


    public InventoryItem getBulletItem(PlayerMob player) {
        InventoryItem invItem = null;
        if (player.getFirstAvailableBullet("debug").getStringID().equalsIgnoreCase(this.getStringID())) {
            for (int i = 0; i <= player.getInv().main.getSize(); i++) {
                InventoryItem slot = player.getInv().main.getItem(i);
                if (player.getInv().main.getItem(i) != null && player.getInv().main.getItem(i).item == player.getFirstAvailableBullet("debug")) {
                    invItem = player.getInv().main.getItem(i);
                    return invItem;
                }
            }
        } else {
            invItem = new InventoryItem(this,1);
            return invItem;
        }
        return invItem;
    }

    public float getAmmoConsumeChance() {
        return 1.0F;
    }

    public GameDamage modDamage(GameDamage damage) {
        return damage.add((float)this.damage, this.armorPen, this.critChance);
    }

    public float modVelocity(float velocity) {
        return velocity;
    }

    public int modRange(int range) {
        return range;
    }

    public int modKnockback(int knockback) {
        return knockback;
    }

    public Projectile getProjectile(float x, float y, float targetX, float targetY, float velocity, int range, GameDamage damage, int knockback, Mob owner) {
        if (owner instanceof PlayerMob) {
            InventoryItem bullet = getBulletItem((PlayerMob)owner);
            if (bullet != null) {
                InventoryItem headItem = ItemRegistry.getItem(bullet.getGndData().getString("HeadItem","debug")).getDefaultItem(null,1);
                InventoryItem bodyItem = ItemRegistry.getItem(bullet.getGndData().getString("BodyItem","debug")).getDefaultItem(null,1);
                InventoryItem endItem = ItemRegistry.getItem(bullet.getGndData().getString("EndItem","debug")).getDefaultItem(null,1);
                damage = new GameDamage(damage.type,(damage.damage+headItem.getGndData().getFloat("BaseDamage",0)+bodyItem.getGndData().getFloat("BaseDamage",0)+endItem.getGndData().getFloat("BaseDamage",0))*headItem.getGndData().getFloat("DamageMod",1)*bodyItem.getGndData().getFloat("DamageMod",1)*endItem.getGndData().getFloat("DamageMod",1),(damage.armorPen+headItem.getGndData().getFloat("BaseArmorPen",0)+bodyItem.getGndData().getFloat("BaseArmorPen",0)+endItem.getGndData().getFloat("BaseArmorPen",0))*headItem.getGndData().getFloat("ArmorPenMod",1)*bodyItem.getGndData().getFloat("ArmorPenMod",1)*endItem.getGndData().getFloat("ArmorPenMod",1),damage.baseCritChance,damage.playerDamageMultiplier,damage.finalDamageMultiplier);
                knockback = knockback + headItem.getGndData().getInt("KnockbackAdd",0) + bodyItem.getGndData().getInt("KnockbackAdd",0) + endItem.getGndData().getInt("KnockbackAdd",0);
                velocity = velocity * headItem.getGndData().getFloat("VelocityMod",1) * bodyItem.getGndData().getFloat("VelocityMod",1) * endItem.getGndData().getFloat("VelocityMod",1);
                range = range + headItem.getGndData().getInt("RangeAdd",0) + bodyItem.getGndData().getInt("RangeAdd",0) + endItem.getGndData().getInt("RangeAdd",0);
                return new HandGunBulletProjectile(x, y, targetX, targetY, velocity, range, damage, knockback, owner);
            } else {
                return new HandGunBulletProjectile(x, y, targetX, targetY, velocity, range, damage, knockback, owner);
            }
        } else {
            return new HandGunBulletProjectile(x, y, targetX, targetY, velocity, range, damage, knockback, owner);
        }
    }

    public boolean overrideProjectile() {
        return true;
    }

    public String getTranslatedTypeName() {
        return Localization.translate("item", "bullet");
    }
}
