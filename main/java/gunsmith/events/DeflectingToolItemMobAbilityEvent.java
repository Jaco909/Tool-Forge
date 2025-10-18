package gunsmith.events;

import gunsmith.packets.DeflectedProjectilePacket;
import gunsmith.packets.ReflectedProjectilePacket;
import gunsmith.projectiles.ReflectedProjectile;
import necesse.engine.GameLog;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.ClientClient;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameRandom;
import necesse.engine.util.GameUtils;
import necesse.entity.levelEvent.mobAbilityLevelEvent.ToolItemMobAbilityEvent;
import necesse.entity.mobs.AttackAnimMob;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.particle.Particle;
import necesse.entity.projectile.Projectile;
import necesse.entity.projectile.bulletProjectile.BouncingBulletProjectile;
import necesse.gfx.GameResources;
import necesse.inventory.InventoryItem;
import necesse.level.maps.CollisionFilter;
import necesse.level.maps.Level;
import necesse.level.maps.LevelObjectHit;
import necesse.level.maps.hudManager.HudDrawElement;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static necesse.engine.util.GameUtils.getAttackerClient;

public class DeflectingToolItemMobAbilityEvent extends ToolItemMobAbilityEvent {
    public int seed;
    public InventoryItem item;
    public Shape circle;

    public DeflectingToolItemMobAbilityEvent() {
    }

    public DeflectingToolItemMobAbilityEvent(AttackAnimMob mob, int seed, InventoryItem toolItem, int aimX, int aimY, int duration, int hitCooldown, HashMap<Integer, Long> mobHits) {
        super(mob, seed, toolItem, aimX, aimY, duration, hitCooldown, mobHits);
    }

    public DeflectingToolItemMobAbilityEvent(AttackAnimMob mob, int seed, InventoryItem toolItem, int aimX, int aimY, int duration, int hitCooldown) {
        this(mob, seed, toolItem, aimX, aimY, duration, hitCooldown, (HashMap)null);
    }
    public DeflectingToolItemMobAbilityEvent(AttackAnimMob mob, int seed, InventoryItem toolItem, int aimX, int aimY, int duration, int hitCooldown, Shape circle) {
        this(mob, seed, toolItem, aimX, aimY, duration, hitCooldown, (HashMap)null);
        this.circle = circle;
    }

    public void setupSpawnPacket(PacketWriter writer) {
        super.setupSpawnPacket(writer);
    }

    public void applySpawnPacket(PacketReader reader) {
        super.applySpawnPacket(reader);
    }

    public void init() {
        super.init();
    }

    protected boolean anyHitboxIntersectsProjectile(Iterable<Shape> hitBoxes, Projectile target) {
        GameLog.debug.println("wack");
        boolean hitDetected = false;
        Rectangle targetHitbox = target.getHitbox();
        for(Shape hitBox : hitBoxes) {
            if (hitBox.intersects(targetHitbox)) {
                hitDetected = true;
            }
        }
        if (hitDetected) {
            if (target.canHitMobs) {
                return true;
            } else {
                CollisionFilter collisionFilter = this.owner.modifyChasingCollisionFilter((new CollisionFilter()).projectileCollision(), null);
                return !this.level.collides(new Line2D.Float(this.owner.x, this.owner.y, target.x, target.y), collisionFilter);
            }
        } else {
            return false;
        }
    }

    protected Stream<Mob> streamTargets(Shape hitbox) {
        Stream<Mob> targets;
        if (this.allowNullOwner && this.owner == null) {
            targets = Stream.concat(this.level.entityManager.mobs.streamInRegionsShape(hitbox, 1), this.level.entityManager.players.streamInRegionsShape(hitbox, 1));
        } else {
            targets = GameUtils.streamTargets(this.owner, this.level, hitbox);
        }
        Stream<Projectile> projectiles = Stream.concat(this.level.entityManager.projectiles.streamInRegionsShape(this.circle, 0), this.level.entityManager.projectiles.streamInRegionsShape(this.circle, 0).filter(projectile -> projectile.canHit(this.owner)));
        if (this.owner != null) {
            if (this.getFirstPlayerOwner().getSelectedItem().getGndData().getString("BodyItem", "BodyGlaiveBasic").equalsIgnoreCase("BodyGlaiveBlock")) {
                if (!this.owner.buffManager.hasBuff(BuffRegistry.getBuff("GlaiveBlockingStacks")) || this.owner.buffManager.getStacks(BuffRegistry.getBuff("GlaiveBlockingStacks")) != this.owner.buffManager.getBuff(BuffRegistry.getBuff("GlaiveBlockingStacks")).getMaxStacks()) {
                    try {
                        Projectile blocked = projectiles.filter(projectile -> this.circle.contains(projectile.getPositionPoint())).findFirst().get();
                        for (int i = 0; i < 9; i++) {
                            level.entityManager.addParticle(blocked.x + (float) (GameRandom.globalRandom.nextGaussian() * (double) 6.0F), blocked.y + (float) (GameRandom.globalRandom.nextGaussian() * (double) 8.0F), Particle.GType.IMPORTANT_COSMETIC).color(new Color(255,255,255)).height(16.0F).lifeTime(850 + GameRandom.globalRandom.getIntBetween(0, 100)).givesLight(85).size((options, lifeTime, timeAlive, lifePercent) -> options.size(12)).sizeFades(1, 10).fadesAlphaTime(1, 500);
                        }
                        this.owner.getFirstPlayerOwner().getClient().network.sendPacket(new DeflectedProjectilePacket(blocked.getUniqueID(),level.getIdentifier()));
                        ActiveBuff ab = new ActiveBuff(BuffRegistry.getBuff("GlaiveBlockingStacks"), this.owner, 5F, this.owner);
                        this.owner.buffManager.addBuff(ab, true);
                        SoundManager.playSound(GameResources.flick, SoundEffect.effect(this.owner).volume(1f).pitch(0.8F + (float) this.owner.buffManager.getStacks(BuffRegistry.getBuff("GlaiveBlockingStacks")) / 10));
                    } catch (Exception e) {
                        //null
                    }

                }
            } else if (this.getFirstPlayerOwner().getSelectedItem().getGndData().getString("BodyItem", "BodyGlaiveBasic").equalsIgnoreCase("BodyShovelBlock")) {
                try {
                    Projectile blocked = projectiles.filter(projectile -> this.circle.contains(projectile.getPositionPoint())).filter(projectile -> projectile.canHit(this.owner)).filter(projectile -> projectile.getOwner() != this.owner).filter(projectile -> !(projectile instanceof ReflectedProjectile)).findFirst().get();
                    if (!(blocked instanceof ReflectedProjectile)) {
                        for (int i = 0; i < 9; i++) {
                            level.entityManager.addParticle(blocked.x + (float) (GameRandom.globalRandom.nextGaussian() * (double) 6.0F), blocked.y + (float) (GameRandom.globalRandom.nextGaussian() * (double) 8.0F), Particle.GType.IMPORTANT_COSMETIC).color(new Color(255, 255, 255)).height(16.0F).lifeTime(850 + GameRandom.globalRandom.getIntBetween(0, 100)).givesLight(85).size((options, lifeTime, timeAlive, lifePercent) -> options.size(12)).sizeFades(1, 10).fadesAlphaTime(1, 500);
                        }
                        this.owner.getFirstPlayerOwner().getClient().network.sendPacket(new ReflectedProjectilePacket(blocked.getUniqueID(), level.getIdentifier(), this.owner.getFirstPlayerOwner().getPlayerSlot()));
                        this.owner.getLevel().entityManager.projectiles.get(blocked.getUniqueID(),true).remove();
                        SoundManager.playSound(GameResources.cling, SoundEffect.effect(this.owner).volume(1.3f).pitch(2F));
                        SoundManager.playSound(GameResources.bounce, SoundEffect.effect(this.owner).volume(0.4f).pitch(2F));
                    }
                } catch (Exception e) {
                    //null
                }
            }
        }

        return targets;
    }
    public Stream<Projectile> streamProjectileTargets(Mob attacker, Level level, Shape hitBounds, int extraRegionRange) {
        GameLog.debug.println("wack33");
        if (attacker != null && level != null) {
            NetworkClient attackerClient = getAttackerClient(attacker);
            Stream<Projectile> targets;
            if (hitBounds == null) {
                return level.entityManager.projectiles.stream();
            } else {
                return level.entityManager.projectiles.streamInRegionsShape(hitBounds, extraRegionRange);
            }
        } else {
            return Stream.empty();
        }
    }
    protected Stream<Projectile> streamProjectileTargets(Shape hitbox) {
        GameLog.debug.println("wack3");
        Stream<Projectile> targets;
        if (this.allowNullOwner && this.owner == null) {
            targets = Stream.concat(this.level.entityManager.projectiles.streamInRegionsShape(hitbox, 1), this.level.entityManager.projectiles.streamInRegionsShape(hitbox, 1));
        } else {
            targets = streamProjectileTargets(this.owner, this.level, hitbox,0);
        }

        return targets;
    }

    protected void handleHits(Iterable<Shape> hitboxes, Predicate<Mob> canHit, Function<Mob, Packet> attackContentSupplier, Function<Projectile, Packet> attackContentSuppliers) {
        GameLog.debug.println("wack4");
        Predicate<Projectile> canHitP = null;
        if (this.isClient()) {
            if (this.handlingClient != null) {
                this.streamTargets(this.getHitboxesBounds(hitboxes)).filter((m) -> !m.isPlayer || m == this.owner).filter(canHit).filter((m) -> this.anyHitboxIntersects(hitboxes, m)).forEach((m) -> this.clientHit(m, attackContentSupplier == null ? null : (Packet)attackContentSupplier.apply(m)));
                this.streamProjectileTargets(this.getHitboxesBounds(hitboxes)).filter((p) -> !this.clientHandlesHit || !p.canHit(this.owner)).filter(canHitP).filter((p) -> this.anyHitboxIntersectsProjectile(hitboxes, p)).forEach((p) -> this.clientHitProjectile(p, attackContentSupplier == null ? null : (Packet)attackContentSuppliers.apply(p), false));
            } else if (this.clientHandlesHit) {
                ClientClient client = this.level.getClient().getClient();
                if (client != null && client.hasSpawned() && !client.isDead() && this.canHitLocalClient(client) && canHit.test(client.playerMob) && this.anyHitboxIntersects(hitboxes, client.playerMob)) {
                    this.clientHit(client.playerMob, attackContentSupplier == null ? null : (Packet)attackContentSupplier.apply(client.playerMob));
                }
            }
        } else if (this.isServer()) {
            if (this.handlingClient == null) {
                this.streamTargets(this.getHitboxesBounds(hitboxes)).filter((m) -> !this.clientHandlesHit || !m.isPlayer).filter(canHit).filter((m) -> this.anyHitboxIntersects(hitboxes, m)).forEach((m) -> this.serverHit(m, attackContentSupplier == null ? null : (Packet)attackContentSupplier.apply(m), false));
                //this.streamProjectileTargets(this.getHitboxesBounds(hitboxes)).filter((p) -> !this.clientHandlesHit || !p.canHit(this.owner)).filter(canHitP).filter((p) -> this.anyHitboxIntersectsProjectile(hitboxes, p)).forEach((p) -> this.serverHitProjectile(p, null, false));
            }

            if (this.hitsObjects) {
                for(Shape hitbox : hitboxes) {
                    for(LevelObjectHit hit : this.level.getCollisions(hitbox, (new CollisionFilter()).attackThroughCollision((tp) -> this.canHitObjectFilter(tp.object())))) {
                        if (!hit.invalidPos() && this.canHit(hit)) {
                            this.hit(hit);
                        }
                    }
                }
            }
        }

    }

    protected void handleHits(Shape hitbox, Predicate<Mob> canHit, Function<Mob, Packet> attackContentSupplier) {
        GameLog.debug.println("wack5");
        Function<Projectile, Packet> attackContentSupplieras = null;
        this.handleHits(Collections.singleton(hitbox), canHit, attackContentSupplier, attackContentSupplieras);
    }
    public void clientHitProjectile(Projectile target, Packet content, boolean clientSubmitted) {
        GameLog.debug.println("wack6");
        target.remove();
        level.entityManager.projectiles.get(target.getUniqueID(), true).remove();
    }
}

