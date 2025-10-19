package gunsmith.items.tools;

import gunsmith.gunsmith;
import gunsmith.packets.RequestItemPacket;
import gunsmith.projectiles.modifiers.TriDotModifier;
import gunsmith.scripts.BurstFireAttackHandler;
import gunsmith.scripts.RGBToHex;
import necesse.engine.input.Control;
import necesse.engine.localization.Localization;
import necesse.engine.network.gameNetworkData.GNDItem;
import necesse.engine.network.gameNetworkData.GNDItemGameDamage;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.*;
import necesse.entity.mobs.buffs.ActiveBuff;
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
import gunsmith.projectiles.special.LivingShottyLeafBuffed;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class ModularShotgun extends ModularGun implements ItemInteractAction {
    public ModularShotgun() {
        this.rarity = Rarity.COMMON;
        this.attackAnimTime.setBaseValue(0);
        this.attackXOffset = 10;
        this.attackYOffset = 12;
        this.attackDamage.setBaseValue(0.0F).setUpgradedValue(5,0f);
        this.attackRange.setBaseValue(640);
        this.velocity.setBaseValue(350);
        this.knockback.setBaseValue(50);
        this.resilienceGain.setBaseValue(0.1F);
        this.setItemCategory(new String[]{"TF", "shotgun"});
    }

    public InventoryItem defaultBarrel(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelShotgunIron")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("BarrelBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"BarrelBonus","BarrelBonusValue");
        }
        return defaultItem;
    }
    public InventoryItem defaultBody(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyShotgunBasic")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("BodyBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"BodyBonus","BodyBonusValue");
        }
        return defaultItem;
    }
    public InventoryItem defaultStock(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockShotgunIron")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("StockBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"StockBonus","StockBonusValue");
        }
        return defaultItem;
    }

    //////////////////////////////////////////////
    ///SETUP DISPLAY
    //////////////////////////////////////////////

    //Item name
    protected ListGameTooltips getDisplayNameTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        return ModularBase.getDisplayNameTooltips(item,perspective,blackboard,defaultBarrel(item),defaultBody(item),defaultStock(item));
    }
    //tooltips
    protected void addExtraGunTooltips(ListGameTooltips tooltips, InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        Control statsKey = Control.getControl(gunsmith.settingsGetter.getSelection("viewStats").toString());
        Control partsKey = Control.getControl(gunsmith.settingsGetter.getSelection("viewParts").toString());

        if (!partsKey.isDown()) {
            if (!statsKey.isDown()) {
                super.addExtraGunTooltips(tooltips, item, perspective, blackboard);
            }
        }
    }
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        Control statsKey = Control.getControl(gunsmith.settingsGetter.getSelection("viewStats").toString());
        Control partsKey = Control.getControl(gunsmith.settingsGetter.getSelection("viewParts").toString());

        ListGameTooltips tooltips = new ListGameTooltips();
        if (!partsKey.isDown()) {
            if (!statsKey.isDown()) {
                tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
            }
        }


        if (!item.getGndData().hasKey("StockItem")) {
            //recipe view
            return tooltips;
        } else {
            if (!partsKey.isDown()) {
                if (!statsKey.isDown()) {
                    this.addExtraGunTooltips(tooltips, item, perspective, blackboard);
                }
            }
            if (partsKey.isDown() && partsKey != statsKey) {
                tooltips.add(Localization.translate("itemtooltip", "Barrel") + " " + Localization.translate("item", item.getGndData().getString("BarrelItem", "BarrelShotgunIron")+"Name"));
                tooltips.add(Localization.translate("itemtooltip", "Body") + " " + Localization.translate("item", item.getGndData().getString("BodyItem", "BodyShotgunBasic")+"Name"));
                tooltips.add(Localization.translate("itemtooltip", "Stock") + " " + Localization.translate("item", item.getGndData().getString("StockItem", "StockShotgunIron")+"Name"));
            } else if (statsKey.isDown() || (statsKey.isDown() && statsKey == partsKey) || (partsKey.isDown() && statsKey == partsKey)) {
                if (statsKey == partsKey) {
                    tooltips.add(Localization.translate("itemtooltip", "Barrel") + " " + Localization.translate("item", item.getGndData().getString("BarrelItem", "BarrelShotgunIron")+"Name"));
                    tooltips.add(Localization.translate("itemtooltip", "Body") + " " + Localization.translate("item", item.getGndData().getString("BodyItem", "BodyShotgunBasic")+"Name"));
                    tooltips.add(Localization.translate("itemtooltip", "Stock") + " " + Localization.translate("item", item.getGndData().getString("StockItem", "StockShotgunIron")+"Name"));
                }
                tooltips.add(Localization.translate("barreltooltip", "BarrelTip"));
                if (item.getGndData().hasKey("BarrelBonus")) {
                    String color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusColor"));
                    if (item.getGndData().getFloat("BarrelBonusValue") >= defaultStock(item).getGndData().getFloat("High" + item.getGndData().getString("BarrelBonus"))) {
                        color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusMaxColor"));
                    }
                    if (item.getGndData().getString("BarrelBonus").equalsIgnoreCase("BonusAttackSpeed")) {
                        if (item.getGndData().getFloat("BarrelBonusValue") > 0) {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue")/10, "unit", "+","color",color));
                        } else {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue")/10, "unit", "","color",color));
                        }
                    } else {
                        if (item.getGndData().getFloat("BarrelBonusValue") > 0) {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue"), "unit", "+","color",color));
                        } else {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue"), "unit", "","color",color));
                        }
                    }
                }
                tooltips.add(Localization.translate("barreltooltip", item.getGndData().getString("BarrelItem", "BarrelShotgunIron")+"Tip"));
                tooltips.add(Localization.translate("bodytooltip", "BodyTip"));
                if (item.getGndData().hasKey("BodyBonus")) {
                    String color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusColor"));
                    if (item.getGndData().getFloat("BodyBonusValue") >= defaultStock(item).getGndData().getFloat("High" + item.getGndData().getString("BodyBonus"))) {
                        color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusMaxColor"));
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
                tooltips.add(Localization.translate("bodytooltip", item.getGndData().getString("BodyItem", "BodyShotgunBasic")+"Tip"));
                tooltips.add(Localization.translate("stocktooltip", "StockTip"));
                if (item.getGndData().hasKey("StockBonus")) {
                    String color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusColor"));
                    if (item.getGndData().getFloat("StockBonusValue") >= item.item.getDefaultItem(null,1).getGndData().getFloat("High" + item.getGndData().getString("StockBonus"))) {
                        color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusMaxColor"));
                    }
                    if (item.getGndData().getString("StockBonus").equalsIgnoreCase("BonusAttackSpeed")) {
                        if (item.getGndData().getFloat("StockBonusValue") > 0) {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("StockBonus"), "value", item.getGndData().getFloat("StockBonusValue")/10, "unit", "+","color",color));
                        } else {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("StockBonus"), "value", item.getGndData().getFloat("StockBonusValue")/10, "unit", "","color",color));
                        }
                    } else {
                        if (item.getGndData().getFloat("StockBonusValue") > 0) {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("StockBonus"), "value", item.getGndData().getFloat("StockBonusValue"), "unit", "+","color",color));
                        } else {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("StockBonus"), "value", item.getGndData().getFloat("StockBonusValue"), "unit", "","color",color));
                        }
                    }
                }
                tooltips.add(Localization.translate("stocktooltip", item.getGndData().getString("StockItem", "StockShotgunIron")+"Tip"));
            } else {
                if (item.getGndData().getString("BodyItem","").equalsIgnoreCase("BodyShotgunDoubleBarrel")) {
                    tooltips.add(Localization.translate("toolabilitytip", "ShotgunDoubleBarrelTip"), 400);
                }
                if (this.getAmmoConsumeChance(null,item)<1) {
                    tooltips.add(Localization.translate("itemtooltip", "AmmoConsumeTip", "useage", Math.round((1 - getAmmoConsumeChance(null, item)) * 100)));
                }
                if (partsKey == statsKey) {
                    tooltips.add(Localization.translate("itemtooltip", "holdparts", "shift", statsKey.id));
                } else {
                    tooltips.add(Localization.translate("itemtooltip", "holdShift", "shift", statsKey.id));
                    tooltips.add(Localization.translate("itemtooltip", "holdCtrl", "ctrl", partsKey.id));
                }
            }
            return tooltips;
        }
    }


    protected void addAmmoTooltips(ListGameTooltips tooltips, InventoryItem item) {
        Control statsKey = Control.getControl(gunsmith.settingsGetter.getSelection("viewStats").toString());
        Control partsKey = Control.getControl(gunsmith.settingsGetter.getSelection("viewParts").toString());
        if (!partsKey.isDown()) {
            if (!statsKey.isDown()) {
                super.addAmmoTooltips(tooltips, item);
            }
        }
    }

    public void showAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, int animAttack, int seed, GNDItemMap mapContent) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyShotgunBasic")).getDefaultItem(null,1);
        if (level.isClient() && !bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Burst")) {
            this.playFireSound(attackerMob);
        }
    }
    public void playFireSound(AttackAnimMob mob) {
        SoundManager.playSound(GameResources.shotgun, SoundEffect.effect(mob).volume(0.7f).pitch(GameRandom.globalRandom.getFloatBetween(0.9f, 1f)));
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
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("BarrelItem", "BarrelShotgunIron"))).initDraw().color(color).size(size).draw(x, y);
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("StockItem", "StockShotgunIron"))).initDraw().color(color).size(size).draw(x, y);
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("BodyItem", "BodyShotgunBasic"))).initDraw().color(color).size(size).depth(-1F).draw(x, y);

        /// This is where we generate the attack texture
        /// This is the ONLY spot where OpenGL doesn't throw a fit about generating new textures
        File texture = new File(gunsmith.modDirectory + "/attackTextures/" + item.getGndData().getString("BarrelItem", "BarrelShotgunIron") + item.getGndData().getString("BodyItem", "BodyShotgunBasic") + item.getGndData().getString("StockItem", "StockShotgunIron") + ".png");
        if (!texture.exists()) {
            GameTexture baseWorld = GameTexture.fromFile("player/weapons/shotgun/ModularShotgunBase.png", true);
            GameTexture barrelMatTex = GameTexture.fromFile("player/weapons/shotgun/"+item.getGndData().getString("BarrelItem", "BarrelShotgunIron")+".png", true);
            GameTexture bodyMatTex = GameTexture.fromFile("player/weapons/shotgun/"+item.getGndData().getString("BodyItem", "BodyShotgunBasic")+".png", true);
            GameTexture stockMatTex = GameTexture.fromFile("player/weapons/shotgun/"+item.getGndData().getString("StockItem", "StockShotgunIron")+".png", true);
            GameTexture attackTex = new GameTexture("debug",baseWorld.getWidth(),baseWorld.getHeight());
            attackTex.merge(barrelMatTex, 0, 0, MergeFunction.NORMAL);
            attackTex.merge(stockMatTex, 0, 0, MergeFunction.NORMAL);
            attackTex.merge(bodyMatTex, 0, 0, MergeFunction.NORMAL);
            attackTex.makeFinal();
            attackTex.saveTextureImage(gunsmith.modDirectory + "/attackTextures/" + item.getGndData().getString("BarrelItem", "BarrelShotgunIron")+item.getGndData().getString("BodyItem", "BodyShotgunBasic")+item.getGndData().getString("StockItem", "StockShotgunIron"));
        }
    }

    public GameSprite getAttackSprite(InventoryItem item, PlayerMob player) {
        File texture = new File(gunsmith.modDirectory + "/attackTextures/"+item.getGndData().getString("BarrelItem", "BarrelShotgunIron")+item.getGndData().getString("BodyItem", "BodyShotgunBasic")+item.getGndData().getString("StockItem", "StockShotgunIron")+".png");
        GameTexture attTex = null;
        try {
            attTex = GameTexture.fromFileRawOutside(texture.getPath(), true);
        } catch (FileNotFoundException e) {
            if (!texture.exists()) {
                player.getClient().network.sendPacket(new RequestItemPacket(player.getPlayerSlot(), item));
            }
            return new GameSprite(GameTexture.fromFile("player/weapons/shotgun.png"));
        }
        if (!texture.exists()) {
            player.getClient().network.sendPacket(new RequestItemPacket(player.getPlayerSlot(),item));
            return new GameSprite(GameTexture.fromFile("player/weapons/shotgun.png"));
        } else {
            return new GameSprite(attTex);
        }
    }

    public GameSprite getWorldItemSprite(InventoryItem item, PlayerMob perspective) {
        return this.getAttackSprite(item, perspective);
    }

    //////////////////////////////////////////////
    ///SETUP STATS
    //////////////////////////////////////////////
    public float getAttackMovementMod(InventoryItem item) {
        return ModularBase.getAttackMovementMod(item,defaultBarrel(item),defaultBody(item),defaultStock(item));
    }
    public int getFlatAttackAnimTime(InventoryItem item) {
        return ModularBase.getFlatAttackAnimTime(item,defaultBarrel(item),defaultBody(item),defaultStock(item));
    }
    public float getAttackSpeedModifier(InventoryItem item, ItemAttackerMob attackerMob) {
        return ModularBase.getAttackSpeedModifier(item,attackerMob,defaultBarrel(item),defaultBody(item),defaultStock(item),this.damageType,this.getEnchantment(item));
    }
    public GameDamage getFlatAttackDamage(InventoryItem item) {
        return ModularBase.getFlatAttackDamage(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.getDamageType(item));
    }
    public int getFlatAttackRange(InventoryItem item) {
        return ModularBase.getFlatAttackRange(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.attackRange.getValue(this.getUpgradeTier(item)));
    }
    public int getKnockback(InventoryItem item, Attacker attacker) {
        return ModularBase.getKnockback(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.knockback,attacker,this.getEnchantment(item));
    }
    public float getFlatManaCost(InventoryItem item) {
        return ModularBase.getFlatManaCost(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this);
    }
    public float getResilienceGain(InventoryItem item) {
        return ModularBase.getResilienceGain(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this, this.resilienceGain);
    }
    public int getFlatLifeCost(InventoryItem item) {
        return ModularBase.getFlatLifeCost(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this,this.lifeCost.getValue(this.getUpgradeTier(item)));
    }
    public int getLifeSteal(InventoryItem item) {
        return ModularBase.getLifeSteal(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this,this.lifeSteal.getValue(this.getUpgradeTier(item)));
    }
    public float getAmmoConsumeChance(ItemAttackerMob attackerMob, InventoryItem item) {
        return ModularBase.getAmmoConsumeChance(attackerMob,item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.ammoConsumeChance);
    }
    public int getFlatVelocity(InventoryItem item) {
        return ModularBase.getFlatVelocity(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.velocity,this);
    }

    /*public int getProjectileVelocity(InventoryItem item, Mob mob) {
        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelShotgunIron")).getDefaultItem(null,1);
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyShotgunBasic")).getDefaultItem(null,1);
        InventoryItem stockItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockShotgunIron")).getDefaultItem(null,1);

        int velocity = this.getFlatVelocity(item);
        return Math.round((Float)this.getEnchantment(item).applyModifierLimited(ToolItemModifiers.VELOCITY, (Float)ToolItemModifiers.VELOCITY.defaultBuffManagerValue) * (float)velocity * (Float)mob.buffManager.getModifier(BuffModifiers.PROJECTILE_VELOCITY) * barrelItem.getGndData().getFloat("ProjectileSpeedMod",1F) * bodyItem.getGndData().getFloat("ProjectileSpeedMod",1F) * stockItem.getGndData().getFloat("ProjectileSpeedMod",1F));
    }*/

    //////////////////////////////////////////////
    //SHOTGUN SPECIAL
    //////////////////////////////////////////////
    public InventoryItem onAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int animAttack, int seed, GNDItemMap mapContent) {
        if (defaultBody(item).getGndData().getString("BodyName").equalsIgnoreCase("Burst")) {
            attackerMob.startAttackHandler(new BurstFireAttackHandler(attackerMob, slot, item, this, seed, x, y, defaultBody(item).getGndData().getInt("ReloadTime",1700), defaultBody(item).getGndData().getInt("ShotTime",170), defaultBody(item).getGndData().getInt("ShotAmount",3)));
        } else {
            super.onAttack(level, x, y, attackerMob, attackHeight, item, slot, animAttack, seed, mapContent);
        }
        return item;
    }

    public Projectile getProjectile(InventoryItem item, BulletItem bulletItem, float x, float y, float targetX, float targetY, int range, ItemAttackerMob attackerMob) {
        return super.getProjectile(item, bulletItem, x, y + 12.0F, targetX, targetY + 12.0F, range, attackerMob);
    }

    protected void fireProjectiles(Level level, int x, int y, ItemAttackerMob attackerMob, InventoryItem item, int seed, BulletItem bullet, boolean dropItem, GNDItemMap mapContent) {
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
        if (defaultBody(item).getGndData().getString("BodyName").equalsIgnoreCase("Living")) {
            Projectile projectile = new LivingShottyLeafBuffed(level, attackerMob, attackerMob.x, attackerMob.y, (float)x, (float)y, (float)this.getProjectileVelocity(item, attackerMob), range, this.getAttackDamage(item), this.getKnockback(item, attackerMob));
            setModifier(projectile,item);
            projectile.setDamage(this.getAttackDamage(item).modFinalMultiplier(1.75F));
            projectile.getUniqueID(new GameRandom((long)seed));
            attackerMob.addAndSendAttackerProjectile(projectile, this.moveDist);
            projectile.setAngle(projectile.getAngle() + (spreadRandom.nextFloat() - 0.5F) * ModularBase.getSpread(item,defaultBarrel(item),defaultBody(item),defaultStock(item),22));
        }

        for(int i = 1; i <= (ModularBase.getPellets(item,defaultBarrel(item),defaultBody(item),defaultStock(item))); ++i) {
            Projectile projectile = this.getProjectile(item, bullet, attackerMob.x, attackerMob.y, (float)x, (float)y, range, attackerMob);
            if (projectile.getClass().getSuperclass().getName().substring(projectile.getClass().getSuperclass().getName().length()-19).equalsIgnoreCase("FollowingProjectile") && !item.getGndData().getString("BarrelName", "Iron").equalsIgnoreCase("Void")) {
                int pellets = ModularBase.getPellets(item,defaultBarrel(item),defaultBody(item),defaultStock(item));
                projectile.setDamage(this.getAttackDamage(item).modFinalMultiplier(1-((pellets/2F)*0.1F)));
            }
            setModifier(projectile,item);
            projectile.dropItem = dropItem;
            projectile.getUniqueID(new GameRandom((long)seed));
            attackerMob.addAndSendAttackerProjectile(projectile, this.moveDist);
            projectile.setAngle(projectile.getAngle() + (spreadRandom.nextFloat() - 0.5F) * ModularBase.getSpread(item,defaultBarrel(item),defaultBody(item),defaultStock(item),22));
        }
    }

    public boolean canLevelInteract(Level level, int x, int y, ItemAttackerMob attackerMob, InventoryItem item) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyShotgunBasic")).getDefaultItem(null,1);
        Item bulletcheck = attackerMob.getFirstPlayerOwner().getInv().main.getFirstItem(level, attackerMob.getFirstPlayerOwner(), this.ammoItems(), "bulletammo");
        if (bulletcheck != null) {
            return !attackerMob.buffManager.hasBuff("DoubleBarrelCooldownDebuff") && bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Burst");
        } else {
            return false;
        }
    }

    public InventoryItem onLevelInteract(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int seed, GNDItemMap mapContent) {
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
        for (int i = 1; i <= (ModularBase.getPellets(item,barrelItem,bodyItem,stockItem))*2; ++i) {
            Projectile projectile = this.getProjectile(item, (BulletItem) bullet, attackerMob.x, attackerMob.y, (float) x, (float) y, range, attackerMob);
            projectile.setAngle(projectile.getAngle() + (spreadRandom.nextFloat() - 0.5F) * ModularBase.getSpread(item,barrelItem,bodyItem,stockItem,28));
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
    }

    public void setModifier(Projectile projectile, InventoryItem item) {
        projectile.setModifier(new ResilienceOnHitProjectileModifier(this.getResilienceGain(item)));
        if (item.getGndData().getString("BodyItem","BodyShotgunBasic").equalsIgnoreCase("BodyShotgunNaturesWrath")) {
            projectile.setModifier(new TriDotModifier(30F,this.getResilienceGain(item)));
        }
    }

    public float getItemCooldownPercent(InventoryItem item, PlayerMob perspective) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyShotgunBasic")).getDefaultItem(null,1);
        if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Burst")) {
            return perspective.buffManager.getBuffDurationLeftSeconds("DoubleBarrelCooldownDebuff") / 2.0F;
        } else {
            return 0;
        }
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
