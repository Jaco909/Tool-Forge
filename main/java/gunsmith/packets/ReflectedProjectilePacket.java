package gunsmith.packets;

import gunsmith.projectiles.ReflectedProjectile;
import necesse.engine.GameLog;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ProjectileRegistry;
import necesse.engine.util.GameRandom;
import necesse.engine.util.LevelIdentifier;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.entity.projectile.bulletProjectile.BouncingBulletProjectile;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.gameObject.SeedObject;
import necesse.level.maps.Level;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

public class ReflectedProjectilePacket extends Packet {

    public final int uniqueID;
    public final LevelIdentifier levelID;
    public final int playerID;

    public ReflectedProjectilePacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.uniqueID = reader.getNextInt();
        this.levelID = new LevelIdentifier(reader.getNextString());
        this.playerID = reader.getNextInt();
    }

    public ReflectedProjectilePacket(int uniqueID, LevelIdentifier levelID, int playerID) {
        this.uniqueID = uniqueID;
        this.levelID = levelID;
        this.playerID = playerID;
        PacketWriter writer = new PacketWriter(this);
        writer.putNextInt(uniqueID);
        writer.putNextString(levelID.stringID);
        writer.putNextInt(playerID);
    }
    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        Projectile old = server.world.getLevel(this.levelID).entityManager.projectiles.get(uniqueID,true);
        Projectile reflect = null;
        if (old.getOwner() != null) {
            reflect = new ReflectedProjectile(old.getX(),old.getY(),old.getOwner().x,old.getOwner().y,old.speed,old.distance,old.getDamage(),old.knockback,server.getPlayer(playerID));
        } else {
            if (server.getPlayer(playerID).getDir() == 0) {
                reflect = new ReflectedProjectile(old.getX(),old.getY(),old.getX()+GameRandom.globalRandom.getIntBetween(-100,100),old.getY()-GameRandom.globalRandom.getIntBetween(10,100),old.speed,old.distance,old.getDamage(),old.knockback,server.getPlayer(playerID));

            } else if (server.getPlayer(playerID).getDir() == 1) {
                reflect = new ReflectedProjectile(old.getX(),old.getY(),old.getX()+GameRandom.globalRandom.getIntBetween(10,100),old.getY()+GameRandom.globalRandom.getIntBetween(-100,100),old.speed,old.distance,old.getDamage(),old.knockback,server.getPlayer(playerID));

            }  else if (server.getPlayer(playerID).getDir() == 2) {
                reflect = new ReflectedProjectile(old.getX(),old.getY(),old.getX()+GameRandom.globalRandom.getIntBetween(-100,100),old.getY()+GameRandom.globalRandom.getIntBetween(10,100),old.speed,old.distance,old.getDamage(),old.knockback,server.getPlayer(playerID));

            } else {
                reflect = new ReflectedProjectile(old.getX(),old.getY(),old.getX()-GameRandom.globalRandom.getIntBetween(10,100),old.getY()+GameRandom.globalRandom.getIntBetween(-100,100),old.speed,old.distance,old.getDamage(),old.knockback,server.getPlayer(playerID));
            }
        }
        if (reflect.speed*2 > 10) {
            server.world.getLevel(this.levelID).entityManager.projectiles.add(reflect);
        }
        old.remove();
    }

    public void processClient(NetworkPacket packet, Client client) {
        if (client.getLevel().getIdentifier() == this.levelID && client.getLevel().entityManager.projectiles.get(this.uniqueID,true) != null) {
            client.getLevel().entityManager.projectiles.get(this.uniqueID,true).remove();
        }
    }
}