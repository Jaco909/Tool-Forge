package gunsmith.items.tools;

import gunsmith.projectiles.modifiers.CrystalizeModifier;
import gunsmith.scripts.BuildupFireAttackHandler;
import gunsmith.scripts.BurstFireAttackHandler;
import gunsmith.scripts.MisfireAttackHandler;
import gunsmith.scripts.RipperAttackHandler;
import necesse.engine.input.Control;
import necesse.engine.localization.Localization;
import necesse.engine.network.gameNetworkData.GNDItem;
import necesse.engine.network.gameNetworkData.GNDItemGameDamage;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.*;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.gameDamageType.DamageType;
import necesse.entity.mobs.itemAttacker.ItemAttackSlot;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.entity.projectile.Projectile;
import necesse.entity.projectile.modifiers.ResilienceOnHitProjectileModifier;
import necesse.gfx.GameColor;
import necesse.gfx.GameResources;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawOptions.itemAttack.ItemAttackDrawOptions;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.enchants.ToolItemModifiers;
import necesse.inventory.item.*;
import necesse.inventory.item.bulletItem.BulletItem;
import necesse.level.maps.Level;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class ModularPistol extends ModularGun implements ItemInteractAction {
    public ModularPistol() {
        this.rarity = Rarity.COMMON;
        this.attackAnimTime.setBaseValue(400);
        this.attackXOffset = 12;
        this.attackYOffset = 14;
        this.attackDamage.setBaseValue(0.0F).setUpgradedValue(5,0f);
        this.attackRange.setBaseValue(820);
        this.velocity.setBaseValue(320);
        this.setItemCategory(new String[]{"TF", "pistol"});
    }
    public InventoryItem defaultBarrel(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelPistolIron")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("BarrelBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"BarrelBonus","BarrelBonusValue");
        }
        return defaultItem;
    }
    public InventoryItem defaultBody(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("BodyBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"BodyBonus","BodyBonusValue");
        }
        return ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPickaxeBasic")).getDefaultItem(null,1);
    }
    public InventoryItem defaultStock(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockPistolIron")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("StockBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"StockBonus","StockBonusValue");
        }
        return defaultItem;
    }

    //////////////////////////////////////////////
    //SETUP DISPLAY
    //////////////////////////////////////////////

    //Item name
    protected ListGameTooltips getDisplayNameTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = new ListGameTooltips();
        if (!item.getGndData().hasKey("StockItem")) {
            //recipe display
            tooltips.add(new StringTooltips("Pistol Template", new Color(255,255,255)));
            tooltips.add(new StringTooltips("I see you cheating :)", new Color(255, 0, 0)));
        } else {
            GameColor tiercolor = GameColor.ITEM_COMMON;
            if (item.getGndData().getFloat("Rarity",0F) >= 5F) {
                tiercolor = GameColor.ITEM_LEGENDARY;
            } else if (item.getGndData().getFloat("Rarity",0F) >= 4F) {
                tiercolor = GameColor.ITEM_QUEST;
            } else if (item.getGndData().getFloat("Rarity",0F) >= 3F) {
                tiercolor = GameColor.ITEM_RARE;
            } else if (item.getGndData().getFloat("Rarity",0F) >= 2F) {
                tiercolor = GameColor.ITEM_UNCOMMON;
            }
            if (item.getGndData().hasKey("name")) {
                tooltips.add(new StringTooltips(item.getGndData().getString("name","Wack"), tiercolor));

            } else {
                tooltips.add(new StringTooltips(Localization.translate("item", item.getGndData().getString("BodyItem") + "Name") + " " + Localization.translate("item", item.getGndData().getString("StockItem") + "Name") + " " + Localization.translate("item", this.getStringID()), tiercolor));
            }
            int upgradeLevel = this.getUpgradeLevel(item);
            if (upgradeLevel > 0) {
                int tier = upgradeLevel / 100;
                String tierString;
                if ((float)tier == (float)upgradeLevel / 100.0F) {
                    tierString = String.valueOf(tier);
                } else {
                    int extra = upgradeLevel - tier * 100;
                    tierString = tier + " +" + extra + "%";
                }

                tooltips.add(new StringTooltips(Localization.translate("item", "tier", "tiernumber", tierString), new Color(133, 49, 168)));
            }
        }
        return tooltips;
    }
    //tooltips
    protected void addExtraGunTooltips(ListGameTooltips tooltips, InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        if (!Control.INV_QUICK_TRASH.isDown()) {
            if (!Control.INV_QUICK_MOVE.isDown()) {
                super.addExtraGunTooltips(tooltips, item, perspective, blackboard);
            }
        }
    }
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = new ListGameTooltips();
        if (!Control.INV_QUICK_TRASH.isDown()) {
            if (!Control.INV_QUICK_MOVE.isDown()) {
                tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
            }
        }
        if (!item.getGndData().hasKey("StockItem")) {
            //recipe view
            return tooltips;
        } else {
            if (!Control.INV_QUICK_TRASH.isDown()) {
                if (!Control.INV_QUICK_MOVE.isDown()) {
                    this.addExtraGunTooltips(tooltips, item, perspective, blackboard);
                }
            }
            if (Control.INV_QUICK_TRASH.isDown()) {
                tooltips.add(Localization.translate("itemtooltip", "Barrel") + " " + Localization.translate("item", item.getGndData().getString("BarrelItem", "BarrelPistolIron")+"Name"));
                tooltips.add(Localization.translate("itemtooltip", "Body") + " " + Localization.translate("item", item.getGndData().getString("BodyItem", "BodyPistolBasic")+"Name"));
                tooltips.add(Localization.translate("itemtooltip", "Stock") + " " + Localization.translate("item", item.getGndData().getString("StockItem", "StockPistolIron")+"Name"));
            } else if (Control.INV_QUICK_MOVE.isDown()) {
                tooltips.add(Localization.translate("barreltooltip", "BarrelTip"));
                tooltips.add(Localization.translate("barreltooltip", item.getGndData().getString("BarrelItem", "BarrelPistolIron")+"Tip"));
                tooltips.add(Localization.translate("bodytooltip", "BodyTip"));
                tooltips.add(Localization.translate("bodytooltip", item.getGndData().getString("BodyItem", "BodyPistolBasic")+"Tip"));
                tooltips.add(Localization.translate("stocktooltip", "StockTip"));
                tooltips.add(Localization.translate("stocktooltip", item.getGndData().getString("StockItem", "StockPistolIron")+"Tip"));
            } else {
                if (this.getAmmoConsumeChance(null,item)<1) {
                    tooltips.add(Localization.translate("itemtooltip", "AmmoConsumeTip", "useage", Math.round((1 - getAmmoConsumeChance(null, item)) * 100)));
                }
                if (this.getManaCost(item)>0) {
                    float modifier = perspective == null ? (Float)BuffModifiers.MANA_USAGE.defaultBuffManagerValue : (Float)perspective.buffManager.getModifier(BuffModifiers.MANA_USAGE);
                    float currentManaCost = this.getManaCost(item) * modifier;
                    DoubleItemStatTip tip = new LocalMessageDoubleItemStatTip("itemtooltip", "manacosttip", "value", (double)currentManaCost, 1);
                    tooltips.add(tip.toTooltip(Color.green,Color.red,Color.white,false));
                }
                tooltips.add(Localization.translate("itemtooltip", "holdShift", "shift", "invquickmove"));
                tooltips.add(Localization.translate("itemtooltip", "holdCtrl", "ctrl", "invtrash"));
            }
            return tooltips;
        }
    }


    protected void addAmmoTooltips(ListGameTooltips tooltips, InventoryItem item) {
        if (!Control.INV_QUICK_TRASH.isDown()) {
            if (!Control.INV_QUICK_MOVE.isDown()) {
                super.addAmmoTooltips(tooltips, item);
            }
        }
    }

    public void showAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, int animAttack, int seed, GNDItemMap mapContent) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        if (level.isClient() && !bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Burst") && !bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Ripper") && !bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Economic")) {
            this.playFireSound(attackerMob,item);
        }
    }

    public void playFireSound(AttackAnimMob mob, InventoryItem item) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Magic")) {
            SoundManager.playSound(GameResources.firespell1, SoundEffect.effect(mob).volume(0.8f).pitch(GameRandom.globalRandom.getFloatBetween(1.3f, 1.37f)));
        } else {
            SoundManager.playSound(GameResources.handgun, SoundEffect.effect(mob).volume(0.7f).pitch(GameRandom.globalRandom.getFloatBetween(1.15f, 1.2f)));
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
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("BarrelItem", "BarrelPistolIron"))).initDraw().color(color).size(size).draw(x, y);
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("BodyItem", "BodyPistolBasic"))).initDraw().color(color).size(size).depth(-1F).draw(x, y);
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("StockItem", "StockPistolIron"))).initDraw().color(color).size(size).draw(x, y);
    }

    public void setDrawAttackRotation(InventoryItem item, ItemAttackDrawOptions drawOptions, float attackDirX, float attackDirY, float attackProgress) {
        super.setDrawAttackRotation(item, drawOptions, attackDirX, attackDirY, attackProgress);
        drawOptions.addedArmPosOffset(-3, 5);
    }

    public GameSprite getAttackSprite(InventoryItem item, PlayerMob player) {
        return new GameSprite(GameTexture.fromFile("player/weapons/handgun"));
    }

    public DamageType getDamageType(InventoryItem item) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Magic")) {
            return DamageTypeRegistry.MAGIC;
        } else {
            return DamageTypeRegistry.RANGED;
        }
    }


    //////////////////////////////////////////////
    //SETUP STATS
    //////////////////////////////////////////////

    //Damage, Armor Penetration, & Crit chance
    public GameDamage getFlatAttackDamage(InventoryItem item) {
        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelPistolIron")).getDefaultItem(null,1);
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        InventoryItem stockItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockPistolIron")).getDefaultItem(null,1);

        GNDItemMap gndData = item.getGndData();
        if (gndData.hasKey("damage")) {
            GNDItem gndItem = gndData.getItem("damage");
            if (gndItem instanceof GNDItemGameDamage) {
                return ((GNDItemGameDamage)gndItem).damage;
            }

            if (gndItem instanceof GNDItem.GNDPrimitive) {
                float damage = ((GNDItem.GNDPrimitive)gndItem).getFloat();
                return new GameDamage(this.getDamageType(item), damage);
            }
        }
        if (this.getUpgradeTier(item) > 0) {
            if ((stockItem.getGndData().hasKey("StockDamageUpgrade" + this.getUpgradeLevel(item)/100))) {
                return new GameDamage(this.getDamageType(item), (stockItem.getGndData().getFloat("StockDamageUpgrade" + this.getUpgradeLevel(item)/100, 16F) * barrelItem.getGndData().getFloat("DamageMod", 1F) * bodyItem.getGndData().getFloat("DamageMod", 1F)), (stockItem.getGndData().getFloat("ArmorPen", 0F) + barrelItem.getGndData().getFloat("ArmorPen", 0F) + bodyItem.getGndData().getFloat("ArmorPen", 0F)), (stockItem.getGndData().getFloat("CritChance", 0F) + barrelItem.getGndData().getFloat("CritChance", 0F) + bodyItem.getGndData().getFloat("CritChance", 0F)));
            } else {
                float upgMod = (stockItem.getGndData().getFloat("StockDamageUpgrade10",66F)-stockItem.getGndData().getFloat("StockDamageUpgrade1",50F))/4;
                return new GameDamage(this.getDamageType(item), ((stockItem.getGndData().getFloat("StockDamageUpgrade1", 50F)+(upgMod*(this.getUpgradeTier(item)-1))) * barrelItem.getGndData().getFloat("DamageMod", 1F) * bodyItem.getGndData().getFloat("DamageMod", 1F)), (stockItem.getGndData().getFloat("ArmorPen", 0F) + barrelItem.getGndData().getFloat("ArmorPen", 0F) + bodyItem.getGndData().getFloat("ArmorPen", 0F)), (stockItem.getGndData().getFloat("CritChance", 0F) + barrelItem.getGndData().getFloat("CritChance", 0F) + bodyItem.getGndData().getFloat("CritChance", 0F)));
            }
        } else {
            return new GameDamage(this.getDamageType(item), (stockItem.getGndData().getFloat("Damage", 16F) * barrelItem.getGndData().getFloat("DamageMod", 1F) * bodyItem.getGndData().getFloat("DamageMod", 1F)), (stockItem.getGndData().getFloat("ArmorPen", 0F) + barrelItem.getGndData().getFloat("ArmorPen", 0F) + bodyItem.getGndData().getFloat("ArmorPen", 0F)), (stockItem.getGndData().getFloat("CritChance", 0F) + barrelItem.getGndData().getFloat("CritChance", 0F) + bodyItem.getGndData().getFloat("CritChance", 0F)));
        }
    }

    //Attack Speed
    public float getAttackSpeedModifier(InventoryItem item, ItemAttackerMob attackerMob) {
        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelPistolIron")).getDefaultItem(null,1);
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        InventoryItem stockItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockPistolIron")).getDefaultItem(null,1);

        DamageType damageType = this.getDamageType(item);
        return damageType.calculateTotalAttackSpeedModifier(attackerMob, (Float)this.getEnchantment(item).applyModifierUnlimited(ToolItemModifiers.ATTACK_SPEED, (Float)ToolItemModifiers.ATTACK_SPEED.defaultBuffValue))*(stockItem.getGndData().getFloat("AttackSpeed",1F)*barrelItem.getGndData().getFloat("AttackSpeed",1F)*bodyItem.getGndData().getFloat("AttackSpeed",1F));
    }
    //Movement speed
    public float getAttackMovementMod(InventoryItem item) {
        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelPistolIron")).getDefaultItem(null,1);
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        InventoryItem stockItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockPistolIron")).getDefaultItem(null,1);

        if (0.5F+(barrelItem.getGndData().getFloat("MovementMod",0F)+bodyItem.getGndData().getFloat("MovementMod",0F)+stockItem.getGndData().getFloat("MovementMod",0F)) >= 1) {
            return 1;
        } else {
            return 0.5F+(barrelItem.getGndData().getFloat("MovementMod",0F)+bodyItem.getGndData().getFloat("MovementMod",0F)+stockItem.getGndData().getFloat("MovementMod",0F));
        }
    }

    public float getAmmoConsumeChance(ItemAttackerMob attackerMob, InventoryItem item) {
        return ModularBase.getAmmoConsumeChance(attackerMob,item,defaultBarrel(item),defaultBody(item),defaultStock(item),ammoConsumeChance);
    }

    public float getFlatManaCost(InventoryItem item) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        GNDItemMap gndData = bodyItem.getGndData();
        return gndData.hasKey("manaCost") ? gndData.getFloat("manaCost")+(this.getFlatAttackDamage(item).damage*0.05F) : 0F;
    }

    public void addManaCostTip(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, Mob mob) {
        float modifier = mob == null ? (Float)BuffModifiers.MANA_USAGE.defaultBuffManagerValue : (Float)mob.buffManager.getModifier(BuffModifiers.MANA_USAGE);
        float currentManaCost = this.getManaCost(currentItem) * modifier;
        DoubleItemStatTip tip = new LocalMessageDoubleItemStatTip("itemtooltip", "manacosttip", "value", (double)currentManaCost, 1);
        if (lastItem != null) {
            tip.setCompareValue((double)(this.getManaCost(lastItem) * modifier), false);
        }

        list.add(1000, tip);
    }

    public float getManaCost(InventoryItem item) {
        return this.getFlatManaCost(item) * this.getManaUsageModifier(item);
    }

    public void consumeMana(ItemAttackerMob attackerMob, InventoryItem item) {
        this.consumeMana(this.getManaCost(item), attackerMob);
    }

    /*public InventoryItem superOnAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int animAttack, int seed, GNDItemMap mapContent) {
        return super.onAttack(level, x, y, attackerMob, attackHeight, item, slot, animAttack, seed, mapContent);
    }*/
    /*public InventoryItem superOnAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int animAttack, int seed, GNDItemMap mapContent) {
        if (ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1).getGndData().getString("BodyName").equalsIgnoreCase("Burst")) {
            return super.onAttack(level, x, y, attackerMob, attackHeight, item, slot, animAttack, seed, mapContent);
        } else {
            return null;
        }
    }*/

    public InventoryItem onAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int animAttack, int seed, GNDItemMap mapContent) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Burst")) {
            attackerMob.startAttackHandler(new BurstFireAttackHandler(attackerMob, slot, item, this, seed, x, y, bodyItem.getGndData().getInt("ReloadTime",1700), bodyItem.getGndData().getInt("ShotTime",170), bodyItem.getGndData().getInt("ShotAmount",3)));
        } else if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Ripper")) {
            attackerMob.startAttackHandler(new RipperAttackHandler(attackerMob, slot, item, this, seed, x, y));
        } else if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Economic")) {
            attackerMob.startAttackHandler(new MisfireAttackHandler(attackerMob, slot, item, this, seed, x, y, bodyItem.getGndData().getFloat("MisfireChance",0.12F)));
        } else if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Chaingun")) {
            attackerMob.startAttackHandler(new BuildupFireAttackHandler(attackerMob, slot, item, this, seed, x, y));
        } else {
            super.onAttack(level, x, y, attackerMob, attackHeight, item, slot, animAttack, seed, mapContent);
        }
        return item;
    }


    //Knockback
    public int getKnockback(InventoryItem item, Attacker attacker) {
        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelPistolIron")).getDefaultItem(null,1);
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        InventoryItem stockItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockPistolIron")).getDefaultItem(null,1);

        int knockback = this.knockback.getValue(0);
        float buffMod = 1.0F;
        Mob attackOwner = attacker != null ? attacker.getAttackOwner() : null;
        if (attackOwner != null) {
            buffMod = (Float)attackOwner.buffManager.getModifier(BuffModifiers.KNOCKBACK_OUT);
        }

        return Math.round((float)knockback * (Float)this.getEnchantment(item).applyModifierLimited(ToolItemModifiers.KNOCKBACK, (Float)ToolItemModifiers.KNOCKBACK.defaultBuffManagerValue) * buffMod * barrelItem.getGndData().getFloat("KnockbackMod",1F) * bodyItem.getGndData().getFloat("KnockbackMod",1F) * stockItem.getGndData().getFloat("KnockbackMod",1F));
    }

    public int getProjectileVelocity(InventoryItem item, Mob mob) {
        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelPistolIron")).getDefaultItem(null,1);
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        InventoryItem stockItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockPistolIron")).getDefaultItem(null,1);

        int velocity = this.getFlatVelocity(item);
        return Math.round((Float)this.getEnchantment(item).applyModifierLimited(ToolItemModifiers.VELOCITY, (Float)ToolItemModifiers.VELOCITY.defaultBuffManagerValue) * (float)velocity * (Float)mob.buffManager.getModifier(BuffModifiers.PROJECTILE_VELOCITY) * barrelItem.getGndData().getFloat("ProjectileSpeedMod",1F) * bodyItem.getGndData().getFloat("ProjectileSpeedMod",1F) * stockItem.getGndData().getFloat("ProjectileSpeedMod",1F));
    }

    public Projectile getProjectile(InventoryItem item, BulletItem bulletItem, float x, float y, float targetX, float targetY, int range, ItemAttackerMob attackerMob) {
        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelPistolIron")).getDefaultItem(null,1);
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        InventoryItem stockItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockPistolIron")).getDefaultItem(null,1);

        return super.getProjectile(item, bulletItem, x, y + 5, targetX, targetY + 5, range+barrelItem.getGndData().getInt("RangeAdd",0)+bodyItem.getGndData().getInt("RangeAdd",0)+stockItem.getGndData().getInt("RangeAdd",0), attackerMob);
    }

    protected void fireProjectiles(Level level, int x, int y, ItemAttackerMob attackerMob, InventoryItem item, int seed, BulletItem bullet, boolean dropItem, GNDItemMap mapContent) {
        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelPistolIron")).getDefaultItem(null,1);
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        InventoryItem stockItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockPistolIron")).getDefaultItem(null,1);

        GameRandom random = new GameRandom((long)seed);
        GameRandom spreadRandom = new GameRandom((long)(seed + 10));
        int range;
        if (this.controlledRange) {
            Point newTarget = this.controlledRangePosition(new GameRandom((long)(seed + 10)), attackerMob, x, y, item, this.controlledMinRange, this.controlledInaccuracy);
            x = newTarget.x;
            y = newTarget.y;
            range = (int)attackerMob.getDistance((float)x, (float)y);
        } else {
            range = this.getAttackRange(item);
        }
        Projectile projectile = this.getProjectile(item, bullet, attackerMob.x, attackerMob.y, (float)x, (float)y, range, attackerMob);
        setModifier(projectile,item);
        projectile.dropItem = dropItem;
        projectile.getUniqueID(new GameRandom((long)seed));
        attackerMob.addAndSendAttackerProjectile(projectile, this.moveDist);
        if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Magic")) {
            this.consumeMana(attackerMob, item);
        }

        projectile.setAngle(projectile.getAngle() + (spreadRandom.nextFloat() - 0.5F) * (4.0F*barrelItem.getGndData().getFloat("SpreadMod",1F)*bodyItem.getGndData().getFloat("SpreadMod",1F)*stockItem.getGndData().getFloat("SpreadMod",1F)));
    }

    public boolean canLevelInteract(Level level, int x, int y, ItemAttackerMob attackerMob, InventoryItem item) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        Item bulletcheck = attackerMob.getFirstPlayerOwner().getInv().main.getFirstItem(level, attackerMob.getFirstPlayerOwner(), this.ammoItems(), "bulletammo");
        if (bulletcheck != null && bodyItem.getGndData().getString("BodyName","Basic").equalsIgnoreCase("Chaingun") && item.getGndData().getInt("ChargedShots") > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*public InventoryItem onLevelInteract(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int seed, GNDItemMap mapContent) {
        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelShotgunIron")).getDefaultItem(null,1);
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyShotgunBasic")).getDefaultItem(null,1);
        InventoryItem stockItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockShotgunIron")).getDefaultItem(null,1);

        ActiveBuff ab = new ActiveBuff("DoubleBarrelCooldownDebuff", attackerMob, 2F, attackerMob);
        attackerMob.buffManager.addBuff(ab, true);
        Item bullet;
        if (attackerMob.isPlayer) {
            bullet = attackerMob.getFirstPlayerOwner().getInv().main.getFirstItem(level, attackerMob.getFirstPlayerOwner(), this.ammoItems(), "bulletammo");
        } else {
            bullet = ItemRegistry.getItem("simplebullet");
        }
        int range;
        range = this.getAttackRange(item);
        GameRandom random = new GameRandom((long) seed);
        GameRandom spreadRandom = new GameRandom((long) (seed + 10));
        for (int i = 1; i <= (stockItem.getGndData().getInt("Pellets",4)+bodyItem.getGndData().getInt("Pellets",0)+barrelItem.getGndData().getInt("Pellets",0))*2; ++i) {
            Projectile projectile = this.getProjectile(item, (BulletItem) bullet, attackerMob.x, attackerMob.y, (float) x, (float) y, range, attackerMob);
            projectile.setAngle(projectile.getAngle() + (spreadRandom.nextFloat() - 0.5F) * 28.0F*barrelItem.getGndData().getFloat("SpreadMod",1F)*bodyItem.getGndData().getFloat("SpreadMod",1F)*stockItem.getGndData().getFloat("SpreadMod",1F));
            setModifier(projectile,item);
            projectile.dropItem = true;
            projectile.getUniqueID(new GameRandom((long) seed));
            attackerMob.addAndSendAttackerProjectile(projectile, this.moveDist);
        }
        if (attackerMob.isPlayer) {
            attackerMob.getFirstPlayerOwner().getInv().removeItems(attackerMob.getFirstPlayerOwner().getInv().main.getFirstItem(level, attackerMob.getFirstPlayerOwner(), this.ammoItems(), "bulletammo"), 1, true, true, true, true, "bulletammo");
        }
        SoundManager.playSound(GameResources.shotgun, SoundEffect.effect(attackerMob).volume(1.1f).pitch(GameRandom.globalRandom.getFloatBetween(0.8f, 0.85f)));
        SoundManager.playSound(GameResources.explosionLight, SoundEffect.effect(attackerMob).volume(0.75F).pitch(1F));
        return item;
    }*/

    public void setModifier(Projectile projectile, InventoryItem item) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Crystal")) {
            projectile.setModifier(new CrystalizeModifier(this.getResilienceGain(item)));
        } else {
            projectile.setModifier(new ResilienceOnHitProjectileModifier(this.getResilienceGain(item)));
        }
    }

    public float getItemCooldownPercent(InventoryItem item, PlayerMob perspective) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPistolBasic")).getDefaultItem(null,1);
        return 0;
    }

    public ItemControllerInteract getControllerInteract(Level level, PlayerMob player, InventoryItem item, boolean beforeObjectInteract, int interactDir, LinkedList<Rectangle> mobInteractBoxes, LinkedList<Rectangle> tileInteractBoxes) {
        Point2D.Float controllerAimDir = player.getControllerAimDir();
        Point levelPos = this.getControllerAttackLevelPos(level, controllerAimDir.x, controllerAimDir.y, player, item);
        return new ItemControllerInteract(levelPos.x, levelPos.y) {
            public DrawOptions getDrawOptions(GameCamera camera) {
                return null;
            }

            public void onCurrentlyFocused(GameCamera camera) {
            }
        };
    }

}
