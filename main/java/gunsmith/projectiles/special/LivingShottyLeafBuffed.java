package gunsmith.projectiles.special;

import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.projectile.LivingShottyLeafProjectile;
import necesse.level.maps.Level;

public class LivingShottyLeafBuffed extends LivingShottyLeafProjectile {
    public LivingShottyLeafBuffed(){
    }

    public LivingShottyLeafBuffed(Level level, Mob owner, float x, float y, float targetX, float targetY, float speed, int distance, GameDamage damage, int knockback) {
        this.setLevel(level);
        this.setOwner(owner);
        this.x = x;
        this.y = y;
        this.setTarget(targetX, targetY);
        this.speed = speed;
        this.distance = distance;
        this.setDamage(damage);
        this.knockback = knockback;
    }
    public void init() {
        super.init();
        this.height = 18.0F;
        this.piercing = 2;
        this.setWidth(22.0F, true);
        this.trailOffset = -25.0F;
    }
}
