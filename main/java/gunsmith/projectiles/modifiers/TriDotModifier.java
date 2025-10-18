package gunsmith.projectiles.modifiers;

import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.projectile.Projectile;
import necesse.entity.projectile.modifiers.ProjectileModifier;
import necesse.level.maps.LevelObjectHit;

public class TriDotModifier extends ProjectileModifier {
    private float chance;
    private float resilienceGain;
    private boolean hasGained = false;
    public TriDotModifier() {
    }

    public TriDotModifier(float chance, float resilienceGain) {
        this.chance = chance;
        this.resilienceGain = resilienceGain;
    }

    public void setupSpawnPacket(PacketWriter writer) {
        super.setupSpawnPacket(writer);
        writer.putNextFloat(this.chance);
        writer.putNextFloat(this.resilienceGain);
        writer.putNextBoolean(this.hasGained);
    }

    public void applySpawnPacket(PacketReader reader) {
        super.applySpawnPacket(reader);
        this.chance = reader.getNextFloat();
        this.resilienceGain = reader.getNextFloat();
        this.hasGained = reader.getNextBoolean();
    }

    public void initChildProjectile(Projectile projectile, float childStrength, int childCount) {
        super.initChildProjectile(projectile, childStrength, childCount);
        projectile.setModifier(new TriDotModifier(this.chance,this.resilienceGain/(float)childCount));
    }

    public void doHitLogic(Mob mob, LevelObjectHit object, float x, float y) {
        super.doHitLogic(mob, object, x, y);
        if (this.chance >= GameRandom.globalRandom.getFloatBetween(1,100)) {
            if (mob != null) {
                int buffInt = GameRandom.globalRandom.getIntBetween(1,3);
                ActiveBuff ab;
                if (buffInt == 1) {
                    ab = new ActiveBuff("ablaze", mob, 8F, null);
                } else if (buffInt == 2) {
                    ab = new ActiveBuff("necroticpoison", mob, 6F, null);
                } else {
                    ab = new ActiveBuff("frostburn", mob, 6F, null);
                }
                mob.addBuff(ab, true);
            }
        }
        if (!this.hasGained) {
            Mob owner = this.projectile.getOwner();
            if (mob != null && owner != null && mob.canGiveResilience(owner)) {
                owner.addResilience(this.resilienceGain);
                this.hasGained = true;
            }
        }
    }
}