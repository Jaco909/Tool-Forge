package gunsmith.events;

import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.engine.util.LineHitbox;
import necesse.entity.levelEvent.mobAbilityLevelEvent.MobAbilityLevelEvent;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.DeathMessageTable;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.particle.Particle.GType;
import necesse.entity.trails.LightningTrail;
import necesse.entity.trails.TrailVector;
import necesse.level.maps.LevelObjectHit;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.function.Function;

public class JenWandBlastEvent extends MobAbilityLevelEvent implements Attacker {
    //modified clone of LightningTrailEvent
    private static final int totalPoints = 12;
    private static final int distance = 70;
    private static final float distanceMod = 7.8F;
    private static final int ticksToComplete = 2;
    private int knockback;
    private boolean hit;
    private int startX;
    private int startY;
    private int targetX;
    private int targetY;
    private float xDir;
    private float yDir;
    private float resilienceGain;
    private int seed;
    private int tickCounter;
    private int pointCounter;
    private ArrayList<Point2D.Float> points;
    private ArrayList<Integer> hits;
    private LightningTrail trail;
    private float endX;
    private float endY;
    private float endX2;
    private float endY2;

    float closest;

    public JenWandBlastEvent() {
    }
    public Mob source;
    public JenWandBlastEvent(Mob owner, int knockback, float resilienceGain, int startX, int startY, int targetX, int targetY, int seed, Mob mob) {
        super(owner, new GameRandom((long)seed));
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.resilienceGain = resilienceGain;
        this.seed = seed;
        this.source = mob;
        this.knockback = knockback;
        this.hit = false;
    }
    public void applySpawnPacket(PacketReader reader) {
        super.applySpawnPacket(reader);
        this.startX = reader.getNextInt();
        this.startY = reader.getNextInt();
        this.targetX = reader.getNextInt();
        this.targetY = reader.getNextInt();
        this.seed = reader.getNextInt();
        this.tickCounter = reader.getNextShortUnsigned();
    }
    public void setupSpawnPacket(PacketWriter writer) {
        super.setupSpawnPacket(writer);
        writer.putNextInt(this.startX);
        writer.putNextInt(this.startY);
        writer.putNextInt(this.targetX);
        writer.putNextInt(this.targetY);
        writer.putNextInt(this.seed);
        writer.putNextShortUnsigned(this.tickCounter);
    }
    public void init() {
        super.init();
        float l = (float)(new Point(this.startX, this.startY)).distance((double)this.targetX, (double)this.targetY);
        this.xDir = (float)(this.targetX - this.startX) / l;
        this.yDir = (float)(this.targetY - this.startY) / l;
        this.points = this.generatePoints();
        this.trail = new LightningTrail(new TrailVector((float)this.startX, (float)this.startY, this.xDir, this.yDir, 42.5F, 15.0F), this.level, new Color(12, 195, 227));
        this.trail.addNewPoint(new TrailVector((Point2D.Float)this.points.get(0), this.xDir, this.yDir, this.trail.thickness, 18.0F));
        if (this.isClient()) {
            this.level.entityManager.addTrail(this.trail);
        }

        this.hits = new ArrayList();
    }

    public void clientTick() {
        if (!this.isOver()) {
            ++this.tickCounter;
            int expectedCounter = this.tickCounter * totalPoints / ticksToComplete;

            while(this.pointCounter < expectedCounter) {
                if (this.hit) {
                    /*this.endX = this.points.get(this.pointCounter).x;
                    this.endY = this.points.get(this.pointCounter).y;
                    Projectile projectile = new JenWandPushProjectile(this.level,this.owner, endX, endY, endX+(100*this.xDir), this.yDir+(100*this.yDir), 300, 300, new GameDamage(10,0), 1000);
                    GameRandom random = new GameRandom((long)seed);
                    projectile.getUniqueID(random);
                    this.getLevel().entityManager.projectiles.add(projectile);
                    System.out.println(this.getLevel().entityManager.projectiles.get(projectile.getUniqueID(), true).x);
                    System.out.println(this.getLevel().entityManager.projectiles.get(projectile.getUniqueID(), true).y);
                    System.out.println(this.getLevel().entityManager.projectiles.get(projectile.getUniqueID(), true).targetX);
                    System.out.println(this.getLevel().entityManager.projectiles.get(projectile.getUniqueID(), true).targetY);*/
                    this.over();
                    break;
                }
                ++this.pointCounter;
                if (this.pointCounter >= totalPoints) {
                    /*this.endX = this.points.get(this.pointCounter).x;
                    this.endY = this.points.get(this.pointCounter).y;
                    TrailVector pointsss = new TrailVector((Point2D.Float)this.points.get(this.pointCounter), this.xDir, this.yDir, this.trail.thickness, 18.0F);

                    //Projectile projectile = new JenWandPushProjectile(this.level,this.owner, endX, endY, endX+(100*this.xDir), this.yDir+(100*this.yDir), 300, 300, new GameDamage(10,0), 1000);
                    Projectile projectile = new LivingShottyLeafBuffed(this.level,this.owner, endX, endY, endX+1, endY+1, 300, 300, new GameDamage(GameDamage.DamageType.RANGED,10,0), 1000);
                    GameRandom random = new GameRandom((long)seed);
                    projectile.getUniqueID(random);
                    this.getLevel().entityManager.projectiles.add(projectile);
                    System.out.println(this.getLevel().entityManager.projectiles.get(projectile.getUniqueID(), true).x);
                    System.out.println(this.getLevel().entityManager.projectiles.get(projectile.getUniqueID(), true).y);
                    System.out.println(this.getLevel().entityManager.projectiles.get(projectile.getUniqueID(), true).targetX);
                    System.out.println(this.getLevel().entityManager.projectiles.get(projectile.getUniqueID(), true).targetY);*/
                    this.over();
                    break;
                }

                Point2D.Float point = (Point2D.Float)this.points.get(this.pointCounter);
                this.trail.addNewPoint(new TrailVector(point, this.xDir, this.yDir, this.trail.thickness, 18.0F));
                Point2D.Float lastPoint = (Point2D.Float)this.points.get(this.pointCounter - 1);
                Point2D.Float midPoint = new Point2D.Float((point.x + lastPoint.x) / 2.0F, (point.y + lastPoint.y) / 2.0F);
                Point2D.Float norm = GameMath.normalize(point.x - lastPoint.x, point.y - lastPoint.y);
                float distance = (float)point.distance(lastPoint);

                int j;
                for(j = 0; j < 2; ++j) {
                    this.level.entityManager.addParticle(midPoint.x + norm.x * GameRandom.globalRandom.nextFloat() * distance, midPoint.y + norm.y * GameRandom.globalRandom.nextFloat() * distance, GType.COSMETIC).movesConstant((float)(GameRandom.globalRandom.nextGaussian() * 4.0), (float)(GameRandom.globalRandom.nextGaussian() * 4.0)).color(this.trail.col).height(18.0F);
                }

                if (this.pointCounter == 4) {
                    for(j = 0; j < 20; ++j) {
                        this.level.entityManager.addParticle(lastPoint.x + norm.x * 4.0F, lastPoint.y + norm.y * 4.0F, GType.COSMETIC).movesConstant((float)(GameRandom.globalRandom.nextGaussian() * 20.0), (float)(GameRandom.globalRandom.nextGaussian() * 20.0)).color(this.trail.col).height(18.0F).lifeTime(50);
                    }
                }

                Line2D line = new Line2D.Double(lastPoint.getX(), lastPoint.getY(), point.getX(), point.getY());
                LineHitbox hitbox = new LineHitbox(line, 20.0F);
                this.handleHits(hitbox, (m) -> {
                    return m.canBeHit(this) && !this.hasHit(m);
                }, (Function)null);
            }

        }
    }

    public void serverTick() {
        if (!this.isOver()) {
            ++this.tickCounter;
            int expectedCounter = this.tickCounter * totalPoints / ticksToComplete;

            while(this.pointCounter < expectedCounter) {
                if (this.hit) {
                    this.over();
                    break;
                }
                ++this.pointCounter;
                if (this.pointCounter >= totalPoints) {
                    this.over();
                    break;
                }

                Point2D p1 = (Point2D)this.points.get(this.pointCounter - 1);
                Point2D p2 = (Point2D)this.points.get(this.pointCounter);
                Line2D line = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                LineHitbox hitbox = new LineHitbox(line, 20.0F);
                this.handleHits(hitbox, (m) -> {
                    return !this.hasHit(m);
                }, (Function)null);
            }

        }
    }

    private ArrayList<Point2D.Float> generatePoints() {
        ArrayList<Point2D.Float> out = new ArrayList();
        GameRandom random = new GameRandom((long)this.seed);
        Point2D.Float perp = new Point2D.Float(-this.yDir, this.xDir);
        float lastDist = 0.0F;
        Point2D.Float lastPoint = new Point2D.Float((float)this.startX, (float)this.startY);
        out.add(lastPoint);

        for(int i = 0; i < totalPoints; ++i) {
            float fluctuation = (random.nextFloat() - 0.5F) * lastDist;
            lastDist = (random.nextFloat() + 0.5F) * distanceMod;
            lastPoint = new Point2D.Float(lastPoint.x + this.xDir * lastDist - perp.x * fluctuation, lastPoint.y + this.yDir * lastDist - perp.y * fluctuation);
            out.add(lastPoint);
        }

        return out;
    }
    /*public boolean hasHitBool(){
        return hit;
    }*/

    public void clientHit(Mob target, Packet content) {
        super.clientHit(target, content);
        this.hit = true;
        this.hits.add(target.getUniqueID());
    }

    public void serverHit(Mob target, Packet content, boolean clientSubmitted) {
        super.serverHit(target, content, clientSubmitted);
        //target.isServerHit(new GameDamage(GameDamage.DamageType.MAGIC,1,1), 0.0F, 0.0F, this.knockback, this);
        this.hits.add(target.getUniqueID());
        /*if (target.canGiveResilience(this.owner) && this.resilienceGain != 0.0F) {
            this.owner.addResilience(this.resilienceGain);
            this.resilienceGain = 0.0F;
        }*/
    }

    public void hit(LevelObjectHit hit) {
        super.hit(hit);
        this.hit = true;
        hit.getLevelObject().attackThrough(new GameDamage(0), this);
    }

    public DeathMessageTable getDeathMessages() {
        return this.getDeathMessages("lightning", 2);
    }

    public GameMessage getAttackerName() {
        return (GameMessage)(this.owner != null ? this.owner.getAttackerName() : new LocalMessage("deaths", "unknownatt"));
    }

    public Mob getFirstAttackOwner() {
        return this.owner;
    }

    public boolean hasHit(Mob mob) {
        //this.hit = true;
        return this.hits.contains(mob.getUniqueID());
    }
}