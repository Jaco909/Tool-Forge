package gunsmith.items.tools;

import gunsmith.events.DeflectingToolItemMobAbilityEvent;
import necesse.engine.GameLog;
import necesse.engine.input.Control;
import necesse.engine.localization.Localization;
import necesse.engine.network.client.ClientClient;
import necesse.engine.network.gameNetworkData.GNDItem;
import necesse.engine.network.gameNetworkData.GNDItemGameDamage;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.network.packet.PacketSummonFocus;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.engine.util.ObjectValue;
import necesse.entity.levelEvent.GlaiveShowAttackEvent;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.levelEvent.mobAbilityLevelEvent.ToolItemMobAbilityEvent;
import necesse.entity.mobs.*;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.entity.mobs.itemAttacker.FollowPosition;
import necesse.entity.mobs.itemAttacker.ItemAttackSlot;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.entity.mobs.summon.summonFollowingMob.attackingFollowingMob.DryadSpiritFollowingMob;
import necesse.entity.particle.Particle;
import necesse.entity.projectile.Projectile;
import necesse.gfx.GameColor;
import necesse.gfx.GameResources;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.enchants.ToolItemModifiers;
import necesse.inventory.item.*;
import necesse.inventory.item.toolItem.glaiveToolItem.GlaiveToolItem;
import necesse.inventory.item.toolItem.summonToolItem.SummonToolItem;
import necesse.inventory.lootTable.presets.GlaiveWeaponsLootTable;
import necesse.level.maps.Level;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.BiConsumer;

public class ModularGlaive extends GlaiveToolItem implements ItemInteractAction {
    public InventoryItem defaultBarrel(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","HeadGlaiveGold")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("BarrelBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"BarrelBonus","BarrelBonusValue");
        }
        return defaultItem;
    }
    public InventoryItem defaultBody(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyGlaiveBasic")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("BodyBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"BodyBonus","BodyBonusValue");
        }
        return defaultItem;
    }
    public InventoryItem defaultStock(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","ShaftGlaiveWood")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("StockBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"StockBonus","StockBonusValue");
        }
        return defaultItem;
    }

    public ModularGlaive() {
        super(500, null);
        this.rarity = Rarity.EPIC;
        this.attackAnimTime.setBaseValue(400);
        this.attackDamage.setBaseValue(60.0F).setUpgradedValue(1.0F, 65.0F);
        this.attackRange.setBaseValue(200);
        this.knockback.setBaseValue(75);
        this.width = 20.0F;
        this.attackXOffset = 58;
        this.attackYOffset = 58;
        this.canBeUsedForRaids = false;
        this.useForRaidsOnlyIfObtained = false;
    }

    //////////////////////////////////////////////
    ///SETUP DISPLAY
    //////////////////////////////////////////////
    protected ListGameTooltips getDisplayNameTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        return ModularBase.getDisplayNameTooltips(item,perspective,blackboard,defaultBarrel(item),defaultBody(item),defaultStock(item));
    }

    //tooltips
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
            if (Control.INV_QUICK_TRASH.isDown()) {
                tooltips.add(Localization.translate("itemtooltip", "Head") + " " + Localization.translate("item", item.getGndData().getString("BarrelItem", "HeadGlaiveGold")+"Name"),400);
                tooltips.add(Localization.translate("itemtooltip", "Grip") + " " + Localization.translate("item", item.getGndData().getString("BodyItem", "BodyGlaiveBasic")+"Name"),400);
                tooltips.add(Localization.translate("itemtooltip", "Shaft") + " " + Localization.translate("item", item.getGndData().getString("StockItem", "ShaftGlaiveWood")+"Name"),400);
            } else if (Control.INV_QUICK_MOVE.isDown()) {
                tooltips.add(Localization.translate("barreltooltip", "HeadTip"));
                if (item.getGndData().hasKey("BarrelBonus")) {
                    String color = "§#07BFE8";
                    if (item.getGndData().getFloat("BarrelBonusValue") >= defaultStock(item).getGndData().getFloat("High" + item.getGndData().getString("BarrelBonus"))) {
                        color = "§#E655E6";
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
                tooltips.add(Localization.translate("barreltooltip", item.getGndData().getString("BarrelItem", "HeadGlaiveGold")+"Tip"),400);
                tooltips.add(Localization.translate("bodytooltip", "GripTip"));
                if (item.getGndData().hasKey("BodyBonus")) {
                    String color = "§#07BFE8";
                    if (item.getGndData().getFloat("BodyBonusValue") >= defaultStock(item).getGndData().getFloat("High" + item.getGndData().getString("BodyBonus"))) {
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
                tooltips.add(Localization.translate("bodytooltip", item.getGndData().getString("BodyItem", "BodyGlaiveBasic")+"Tip"),400);
                tooltips.add(Localization.translate("stocktooltip", "ShaftTip"));
                if (item.getGndData().hasKey("StockBonus")) {
                    String color = "§#07BFE8";
                    if (item.getGndData().getFloat("StockBonusValue") >= defaultStock(item).getGndData().getFloat("High" + item.getGndData().getString("StockBonus"))) {
                        color = "§#E655E6";
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
                tooltips.add(Localization.translate("stocktooltip", item.getGndData().getString("StockItem", "ShaftGlaiveWood")+"Tip"),400);
            } else {
                if (item.getGndData().getString("BodyItem","").equalsIgnoreCase("BodyGlaiveDryad")) {
                    tooltips.add(Localization.translate("toolabilitytip", "BodyGlaiveDryadTip"), 400);
                }
                tooltips.add(Localization.translate("itemtooltip", "holdShift", "shift", "invquickmove"),400);
                tooltips.add(Localization.translate("itemtooltip", "holdCtrl", "ctrl", "invtrash"),400);
            }
            return tooltips;
        }
    }
    public void showAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, int animAttack, int seed, GNDItemMap mapContent) {
        super.showAttack(level, x, y, attackerMob, attackHeight, item, animAttack, seed, mapContent);
        InventoryItem barrelItem = defaultBarrel(item);
        InventoryItem bodyItem = defaultBody(item);
        InventoryItem stockItem = defaultStock(item);

        if (level.isClient()) {
            if (barrelItem.getGndData().getBoolean("ShowAttack") || bodyItem.getGndData().getBoolean("ShowAttack") || stockItem.getGndData().getBoolean("ShowAttack")) {
                level.entityManager.addLevelEventHidden(new GlaiveShowAttackEvent(attackerMob, x, y, seed, 10.0F) {
                    public void tick(float angle) {
                        GameRandom gameRandom = new GameRandom();
                        float colorModifier = gameRandom.getFloatBetween(0.0F, 1.0F);
                        Color randomColor = getParticleColor(colorModifier, item);
                        Point2D.Float angleDir = this.getAngleDir(angle);
                        if (barrelItem.getGndData().getString("AttackDisplay").equalsIgnoreCase("Slime") || bodyItem.getGndData().getString("AttackDisplay").equalsIgnoreCase("Slime") || stockItem.getGndData().getString("AttackDisplay").equalsIgnoreCase("Slime")) {
                            this.level.entityManager.addParticle(this.attackMob.x + angleDir.x * 85.0F + (float) this.attackMob.getCurrentAttackDrawXOffset(), this.attackMob.y + angleDir.y * 85.0F + (float) this.attackMob.getCurrentAttackDrawYOffset(), Particle.GType.COSMETIC).sprite(GameResources.bubbleParticle.sprite(0, 0, 12)).color(randomColor).movesConstant(angleDir.x * 40.0F, angleDir.y * 40.0F).lifeTime(400);
                            this.level.entityManager.addParticle(this.attackMob.x - angleDir.x * 85.0F + (float) this.attackMob.getCurrentAttackDrawXOffset(), this.attackMob.y - angleDir.y * 85.0F + (float) this.attackMob.getCurrentAttackDrawYOffset(), Particle.GType.COSMETIC).sprite(GameResources.bubbleParticle.sprite(0, 0, 12)).color(randomColor).movesConstant(angleDir.x * -40.0F, angleDir.y * -40.0F).lifeTime(400);
                        }
                        if (barrelItem.getGndData().getString("AttackDisplay").equalsIgnoreCase("Fire") || bodyItem.getGndData().getString("AttackDisplay").equalsIgnoreCase("Fire") || stockItem.getGndData().getString("AttackDisplay").equalsIgnoreCase("Fire")) {
                            this.level.entityManager.addParticle(this.attackMob.x + angleDir.x * 85.0F + (float) this.attackMob.getCurrentAttackDrawXOffset(), this.attackMob.y + angleDir.y * 85.0F + (float) this.attackMob.getCurrentAttackDrawYOffset(), Particle.GType.COSMETIC).sprite(GameResources.puffParticles.sprite(GameRandom.globalRandom.getIntBetween(1, 2), 0, 12)).color(new Color(187, GameRandom.globalRandom.getIntBetween(60, 100), 0)).sizeFadesInAndOut(8, 24, 50, 15).movesConstant(angleDir.x * GameRandom.globalRandom.getFloatBetween(5, 60), angleDir.y * GameRandom.globalRandom.getFloatBetween(5, 60)).lifeTime(400);
                            this.level.entityManager.addParticle(this.attackMob.x - angleDir.x * 85.0F + (float) this.attackMob.getCurrentAttackDrawXOffset(), this.attackMob.y - angleDir.y * 85.0F + (float) this.attackMob.getCurrentAttackDrawYOffset(), Particle.GType.COSMETIC).sprite(GameResources.puffParticles.sprite(GameRandom.globalRandom.getIntBetween(1, 2), 0, 12)).color(new Color(187, GameRandom.globalRandom.getIntBetween(60, 100), 0)).sizeFadesInAndOut(8, 24, 50, 15).movesConstant(angleDir.x * GameRandom.globalRandom.getFloatBetween(-5, -60), angleDir.y * GameRandom.globalRandom.getFloatBetween(-5, -60)).lifeTime(250);
                        }
                        if (barrelItem.getGndData().getString("AttackDisplay").equalsIgnoreCase("Cryo") || bodyItem.getGndData().getString("AttackDisplay").equalsIgnoreCase("Cryo") || stockItem.getGndData().getString("AttackDisplay").equalsIgnoreCase("Cryo")) {
                            this.level.entityManager.addParticle(this.attackMob.x + angleDir.x * 75.0F + (float)this.attackMob.getCurrentAttackDrawXOffset(), this.attackMob.y + angleDir.y * 75.0F + (float)this.attackMob.getCurrentAttackDrawYOffset(), Particle.GType.COSMETIC).color(new Color(0, 222, 218)).minDrawLight(150).givesLight(179.0F, 1.0F).lifeTime(400);
                            this.level.entityManager.addParticle(this.attackMob.x - angleDir.x * 75.0F + (float)this.attackMob.getCurrentAttackDrawXOffset(), this.attackMob.y - angleDir.y * 75.0F + (float)this.attackMob.getCurrentAttackDrawYOffset(), Particle.GType.COSMETIC).color(new Color(0, 222, 218)).minDrawLight(150).givesLight(179.0F, 1.0F).lifeTime(400);
                        }
                    }
                });
            }
        }
        Shape circle = new Ellipse2D.Float(attackerMob.x - (this.getAttackRange(item) * 1.48F) / 2, attackerMob.y - (this.getAttackRange(item) * 1.48F) / 2, this.getAttackRange(item) * 1.48F, this.getAttackRange(item) * 1.48F);
        if (bodyItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Fire")) {
            level.entityManager.mobs.streamArea(x, y, 1000).forEach(m ->
                    buffHit(circle, m, attackerMob)
            );
        }
        /*if (bodyItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Block")) {
            level.entityManager.projectiles.streamArea(x,y,1500).forEach(p ->
                    blockHit(circle,p,attackerMob,level)
            );
        }*/

    }

    public void draw(InventoryItem item, PlayerMob perspective, int x, int y, boolean inInventory) {
        super.draw(item, perspective, x, y, inInventory);
    }
    public Color getParticleColor(float modifier, InventoryItem item) {
        InventoryItem bodyItem = defaultBody(item);

        if (bodyItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Slime")) {
            return new Color((int)(70.0F * (1.0F + 1.8F * modifier)), (int)(178.0F * (1.0F + 0.3F * modifier)), (int)(170.0F * (1.0F + 0.2F * modifier)));
        } else if (bodyItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Fire")) {
            return new Color((int)(70.0F * (1.0F + 1.8F * modifier)), (int)(178.0F * (1.0F + 0.3F * modifier)), (int)(170.0F * (1.0F + 0.2F * modifier)));
        } else {
            return new Color((int)(70.0F * (1.0F + 1.8F * modifier)), (int)(178.0F * (1.0F + 0.3F * modifier)), (int)(170.0F * (1.0F + 0.2F * modifier)));
        }
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

    //////////////////////////////////////////////
    ///GLAIVE SPECIAL
    //////////////////////////////////////////////
    public InventoryItem onAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int animAttack, int seed, GNDItemMap mapContent) {
        if (animAttack == 0) {
            int animTime = this.getAttackAnimTime(item, attackerMob);
            if (defaultBody(item).getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Block")) {
                Shape circle = new Ellipse2D.Float(attackerMob.x - (this.getFlatAttackRange(item)) / 2, attackerMob.y - (this.getFlatAttackRange(item)) / 2, this.getFlatAttackRange(item), this.getFlatAttackRange(item));
                DeflectingToolItemMobAbilityEvent event = new DeflectingToolItemMobAbilityEvent(attackerMob, seed, item, x - attackerMob.getX(), y - attackerMob.getY() + attackHeight, animTime, animTime / 2,circle);
                attackerMob.addAndSendAttackerLevelEvent(event);
            } else {
                ToolItemMobAbilityEvent event = new ToolItemMobAbilityEvent(attackerMob, seed, item, x - attackerMob.getX(), y - attackerMob.getY() + attackHeight, animTime, animTime / 2);
                attackerMob.addAndSendAttackerLevelEvent(event);
            }
        }

        return item;
    }
    public void hitMob(InventoryItem item, ToolItemMobAbilityEvent event, Level level, Mob target, Mob attacker) {
        super.hitMob(item, event, level, target, attacker);
        InventoryItem barrelItem = defaultBarrel(item);
        InventoryItem bodyItem = defaultBody(item);
        InventoryItem stockItem = defaultStock(item);

        if (bodyItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Dryad")) {
            Buff dryadHaunted = BuffRegistry.Debuffs.DRYAD_HAUNTED;
            ActiveBuff ab = new ActiveBuff(dryadHaunted, target, 10000, attacker);
            ab.setStacks(1, 10000, attacker);
            target.buffManager.addBuff(ab, true);
            if (target.buffManager.getStacks(dryadHaunted) >= 10) {
                target.buffManager.removeBuff(dryadHaunted, true);
                spawnDryadSpirit(attacker);
            }
            ActiveBuff ab2 = new ActiveBuff(BuffRegistry.getBuff("GlaiveDryadStacks"), attacker, 10000, attacker);
            attacker.buffManager.addBuff(ab2, true);
        }
        if (barrelItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Frost")) {
            ActiveBuff ab = new ActiveBuff(BuffRegistry.Debuffs.FROSTSLOW, target, 4.0F, attacker);
            target.addBuff(ab, true);
        }
    }
    public static void spawnDryadSpirit(Mob owner) {
        if (owner != null && owner.isServer()) {
            int maxSummons = 5;
            DryadSpiritFollowingMob summonedMob = (DryadSpiritFollowingMob) MobRegistry.getMob("dryadspirit", owner.getLevel());
            ((ItemAttackerMob)owner).serverFollowersManager.addFollower("summonedmobtemp", summonedMob, FollowPosition.FLYING_CIRCLE_FAST, "summonedmob", 1.0F, (p) -> maxSummons, (BiConsumer)null, false);
            Point2D.Float spawnPoint = SummonToolItem.findSpawnLocation(summonedMob, owner.getLevel(), owner.x, owner.y);
            owner.getLevel().entityManager.addMob(summonedMob, spawnPoint.x, spawnPoint.y);
        }

    }
    /*public void blockHit(Shape circle, Projectile projectile, ItemAttackerMob attackerMob, Level level){
        if (circle.intersects(projectile.getHitbox()) && projectile.canHit(attackerMob)) {
            if (!attackerMob.buffManager.hasBuff(BuffRegistry.getBuff("GlaiveBlockingStacks"))) {
                if (level.isServer()) {
                    projectile.remove();
                    level.entityManager.projectiles.get(projectile.getUniqueID(), true).remove();
                    ActiveBuff ab = new ActiveBuff(BuffRegistry.getBuff("GlaiveBlockingStacks"), attackerMob, 5F, attackerMob);
                    attackerMob.buffManager.addBuff(ab, true);
                }
                if (level.isClient()) {
                    SoundManager.playSound(GameResources.flick, SoundEffect.effect(attackerMob).volume(1f).pitch(0.8F + (float) attackerMob.buffManager.getStacks(BuffRegistry.getBuff("GlaiveBlockingStacks")) / 10));
                }
            } else if (attackerMob.buffManager.getStacks(BuffRegistry.getBuff("GlaiveBlockingStacks")) != attackerMob.buffManager.getBuff(BuffRegistry.getBuff("GlaiveBlockingStacks")).getMaxStacks()) {
                if (level.isServer()) {
                    projectile.remove();
                    level.entityManager.projectiles.get(projectile.getUniqueID(), true).remove();
                    ActiveBuff ab = new ActiveBuff(BuffRegistry.getBuff("GlaiveBlockingStacks"), attackerMob, 5F, attackerMob);
                    attackerMob.buffManager.addBuff(ab, true);
                }
                if (level.isClient()) {
                    SoundManager.playSound(GameResources.flick, SoundEffect.effect(attackerMob).volume(1f).pitch(0.8F + (float) attackerMob.buffManager.getStacks(BuffRegistry.getBuff("GlaiveBlockingStacks")) / 10));
                }
            }
        } else {
            if (!attackerMob.buffManager.hasBuff(BuffRegistry.getBuff("GlaiveBlockingStacks"))) {
                projectile.canHitMobs = false;
            } else if (attackerMob.buffManager.getStacks(BuffRegistry.getBuff("GlaiveBlockingStacks")) != attackerMob.buffManager.getBuff(BuffRegistry.getBuff("GlaiveBlockingStacks")).getMaxStacks()) {
                projectile.canHitMobs = false;
            } else {
                projectile.canHitMobs = true;
            }
        }
    }*/
    public void buffHit(Shape circle, Mob mob, ItemAttackerMob attackerMob){
        if (circle.intersects(mob.getHitBox()) && mob.canBeHit(attackerMob)) {
            ActiveBuff ab = new ActiveBuff(BuffRegistry.getBuff("MagmaPickDebuff"), mob, 3.0F, attackerMob);
            mob.buffManager.addBuff(ab, true);
            mob.collisionHitCooldowns.resetCooldowns();
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

    public boolean canLevelInteract(Level level, int x, int y, ItemAttackerMob attackerMob, InventoryItem item) {
        InventoryItem bodyItem = defaultBody(item);

        return bodyItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Dryad");
    }

    public InventoryItem onLevelInteract(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int seed, GNDItemMap mapContent) {
        if (attackerMob.isPlayer) {
            runSummonFocus(level, x, y, (PlayerMob) attackerMob);
        }
        return item;
    }
    public static void runSummonFocus(Level level, int x, int y, PlayerMob player) {
        if (level.isClient()) {
            Mob newFocus = getNextSummonFocus(level, x, y, player);
            ClientClient me = level.getClient().getClient();
            if (me != null) {
                me.playerMob.summonFocusUniqueID = newFocus == null ? -1 : newFocus.getUniqueID();
                level.getClient().network.sendPacket(new PacketSummonFocus(newFocus));
            }
        }

    }
    public static Mob getNextSummonFocus(Level level, int x, int y, PlayerMob player) {
        if (level.isClient()) {
            Mob nextFocus = (Mob)level.entityManager.streamAreaMobsAndPlayersTileRange(x, y, 10).filter((m) -> m.canBeTargeted(player, player.getNetworkClient()) && m.getSelectBox().contains(x, y)).findFirst().orElse((Mob)null);
            if (nextFocus == null) {
                nextFocus = (Mob)level.entityManager.streamAreaMobsAndPlayersTileRange(x, y, 10).filter((m) -> m.canBeTargeted(player, player.getNetworkClient())).map((m) -> {
                    Rectangle selectBox = m.getSelectBox();
                    return new ObjectValue(m, (new Point2D.Double(selectBox.getCenterX(), selectBox.getCenterY())).distance((double)x, (double)y));
                }).filter((m) -> (Double)m.value < (double)75.0F).findBestDistance(1, Comparator.comparingDouble((m) -> (Double)m.value)).map((m) -> (Mob)m.object).orElse((Mob)null);
            }

            return nextFocus;
        } else {
            return null;
        }
    }

    public int getMobInteractAnimTime(InventoryItem item, ItemAttackerMob attackerMob) {
        return 0;
    }
    public int getLevelInteractAttackAnimTime(InventoryItem item, ItemAttackerMob attackerMob) {
        return 0;
    }

    public void showLevelInteract(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, int seed, GNDItemMap mapContent) {
        /*if (level.isClient()) {
            SoundManager.playSound(GameResources.shake, SoundEffect.effect(attackerMob).volume(0.9F).pitch(GameRandom.globalRandom.getFloatBetween(1.5F, 1.7F)));
        }*/

    }
}
