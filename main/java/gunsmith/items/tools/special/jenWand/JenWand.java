package gunsmith.items.tools.special.jenWand;

import gunsmith.events.JenWandBlastEvent;
import gunsmith.gunsmith;
import gunsmith.projectiles.special.JenWandPushProjectile;
import necesse.engine.localization.Localization;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.network.packet.PacketLevelEvent;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.itemAttacker.ItemAttackSlot;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.entity.projectile.Projectile;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.projectileToolItem.magicProjectileToolItem.MagicProjectileToolItem;
import necesse.inventory.lootTable.LootTable;
import necesse.level.maps.Level;

import java.awt.*;

public class JenWand extends MagicProjectileToolItem {
    public int moveDist;
    public JenWand(){
        super(700, null);
        this.rarity = Rarity.LEGENDARY;
        this.attackAnimTime.setBaseValue(200);
        this.attackDamage.setBaseValue(0.0F).setUpgradedValue(1.0F, 0.0F);
        this.knockback.setBaseValue(300);
        this.attackXOffset = 20;
        this.attackYOffset = 20;
        this.moveDist = 20;
        this.attackCooldownTime.setBaseValue(400);
        this.velocity.setBaseValue(700);
        this.attackRange.setBaseValue(70);
        this.manaCost.setBaseValue(2.0F).setUpgradedValue(1.0F, 3.5F);
        this.itemAttackerProjectileCanHitWidth = 5.0F;
        this.canBeUsedForRaids = false;
        this.raidTicketsModifier = 0.5F;
        this.useForRaidsOnlyIfObtained = false;
    }
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "voidstafftip"));
        return tooltips;
    }

    public void showAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, int animAttack, int seed, GNDItemMap mapContent) {
        if (level.isClient()) {
            SoundManager.playSound(gunsmith.JenWandBlast, SoundEffect.effect(attackerMob).volume(0.5F).pitch(1.15F));
        }

    }

    public Point getItemAttackerAttackPosition(Level level, ItemAttackerMob attackerMob, Mob target, int seed, InventoryItem item) {
        return this.applyInaccuracy(attackerMob, item, new Point(target.getX(), target.getY()));
    }

    public InventoryItem onAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int animAttack, int seed, GNDItemMap mapContent) {
        x=x+GameRandom.globalRandom.getIntBetween(-2, 2);
        y=y+GameRandom.globalRandom.getIntBetween(-2, 2);

        LevelEvent event = new JenWandBlastEvent(attackerMob, this.knockback.getValue(this.getUpgradeTier(item)), 0.2f, attackerMob.getX(), attackerMob.getY(), x, y, GameRandom.globalRandom.getIntBetween(-5, 5), attackerMob);
        attackerMob.getLevel().entityManager.addLevelEventHidden(event);
        if (level.isServer()) {
            level.getServer().network.sendToClientsWithEntityExcept(new PacketLevelEvent(event), event, attackerMob.getFirstPlayerOwner().getServerClient());
        }
        Projectile projectile = new JenWandPushProjectile(level, attackerMob, attackerMob.x, attackerMob.y, (float)x, (float)y, (float)this.getProjectileVelocity(item, attackerMob), this.attackRange.getValue(this.getUpgradeTier(item)), this.getAttackDamage(item), this.getKnockback(item, attackerMob));
        projectile.getUniqueID(new GameRandom((long)seed));
        attackerMob.addAndSendAttackerProjectile(projectile, this.moveDist);
        this.consumeMana(attackerMob, item);
        return item;
    }
}
