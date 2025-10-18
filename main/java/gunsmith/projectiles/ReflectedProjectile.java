package gunsmith.projectiles;

import necesse.engine.GameLog;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.entity.projectile.bulletProjectile.BulletProjectile;
import necesse.entity.trails.Trail;
import necesse.gfx.GameResources;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.EntityDrawable;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameSprite;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

public class ReflectedProjectile extends BulletProjectile {
    private GameDamage damage;
    public Color oldColor;

    public ReflectedProjectile(){
    }

    public ReflectedProjectile(float x, float y, float targetX, float targetY, float speed, int distance, GameDamage damage, int knockback, Mob owner){
        super(x, y, targetX, targetY, speed*2.5F, distance, damage, knockback, owner);
        this.damage = damage;
        this.oldColor = Color.white;
    }

    public ReflectedProjectile(float x, float y, float targetX, float targetY, float speed, int distance, GameDamage damage, int knockback, Mob owner, Color color){
        super(x, y, targetX, targetY, speed*2.5F, distance, damage, knockback, owner);
        this.damage = damage;
        this.oldColor = color;
    }

    public void init() {
        super.init();
        this.height = 18.0F;
        this.heightBasedOnDistance = true;
        this.setWidth(8.0F);
    }

    public GameDamage getDamage() {
        return new GameDamage(this.damage.type,this.damage.damage*1.5F,this.damage.armorPen+1,this.damage.baseCritChance*1.15F,this.damage.playerDamageMultiplier,this.damage.finalDamageMultiplier);
    }

    public Trail getTrail() {
        Trail trail = new Trail(this, this.getLevel(), Color.white, 22.0F, 100, this.getHeight());
        trail.sprite = new GameSprite(GameResources.chains, 7, 0, 32);
        return trail;
    }
    protected Color getWallHitColor() {
        return Color.white;
    }

    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, OrderableDrawables overlayList, Level level, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        if (!this.removed()) {
            GameLight light = level.getLightLevel(this);
            int drawX = camera.getDrawX(this.x) - this.texture.getWidth() / 2;
            int drawY = camera.getDrawY(this.y);
            final TextureDrawOptions options = this.texture.initDraw().light(light).rotate(this.getAngle(), this.texture.getWidth() / 2, 0).pos(drawX, drawY - (int)this.getHeight());
            list.add(new EntityDrawable(this) {
                public void draw(TickManager tickManager) {
                    options.draw();
                }
            });
            this.addShadowDrawables(tileList, drawX, drawY, light, this.getAngle(), 0);
        }
    }
}
