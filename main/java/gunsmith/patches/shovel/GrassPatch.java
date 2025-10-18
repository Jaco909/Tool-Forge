package gunsmith.patches.shovel;

import gunsmith.items.tools.ModularShovel;
import necesse.engine.GameEvents;
import necesse.engine.events.loot.TileLootTableDropsEvent;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ItemRegistry;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.pickup.ItemPickupEntity;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.inventory.item.miscItem.PouchItem;
import necesse.inventory.lootTable.LootItemInterface;
import necesse.level.gameObject.GameObject;
import necesse.level.gameObject.RockOreObject;
import necesse.level.gameTile.GameTile;
import necesse.level.maps.Level;
import necesse.level.maps.LevelObject;
import necesse.level.maps.LevelTile;
import net.bytebuddy.asm.Advice;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

@ModMethodPatch(target = GameTile.class, name = "onDestroyed", arguments = {Level.class,int.class,int.class,Attacker.class,ServerClient.class,ArrayList.class})
public class GrassPatch {
    @Advice.OnMethodEnter(
            skipOn = Advice.OnNonDefaultValue.class
    )
    static boolean onEnter() {
        return true;
    }
    @Advice.OnMethodExit()
    static void onExit(@Advice.This GameTile gameTile, @Advice.Argument(0) Level level, @Advice.Argument(1) int x, @Advice.Argument(2) int y, @Advice.Argument(3) Attacker attacker, @Advice.Argument(4) ServerClient client, @Advice.Argument(5) ArrayList<ItemPickupEntity> itemsDropped) {
        if (itemsDropped != null) {
            TileLootTableDropsEvent dropsEvent = new TileLootTableDropsEvent(gameTile, level, x, y, new Point(x * 32 + 16, y * 32 + 16), gameTile.getDroppedItems(level, x, y));
            level.onTileLootTableDropped(dropsEvent);
            if (gameTile.getStringID().contains("grass") && attacker != null && attacker.getAttackOwner() instanceof PlayerMob) {
                InventoryItem shovel = client.playerMob.getSelectedItem();
                if (shovel.item instanceof ModularShovel && ItemRegistry.getItem(shovel.getGndData().getString("BodyItem", "BodyShovelBasic")).getDefaultItem(client.playerMob, 1).getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Grass")) {
                    if (!((ModularShovel) shovel.item).getInternalInventory(shovel).isSlotClear(0) && ((ModularShovel) shovel.item).getInternalInventory(shovel).getItem(0).item.getStringID().equalsIgnoreCase("landfill")) {
                        InventoryItem tile = new InventoryItem(ItemRegistry.getItem(gameTile.getTileItem().getStringID()), 1);
                        ItemPickupEntity itemDropped = tile.getPickupEntity(level, (float)dropsEvent.dropPos.x, (float)dropsEvent.dropPos.y);
                        level.entityManager.pickups.add(itemDropped);
                        itemsDropped.add(itemDropped);
                        ((ModularShovel) shovel.item).getInternalInventory(shovel).removeItems(level,attacker.getFirstPlayerOwner(),ItemRegistry.getItem("landfill"),1,"grassdig");
                    } else {
                        if (dropsEvent.dropPos != null && dropsEvent.drops != null) {
                            for(InventoryItem item : dropsEvent.drops) {
                                ItemPickupEntity itemDropped = item.getPickupEntity(level, (float)dropsEvent.dropPos.x, (float)dropsEvent.dropPos.y);
                                level.entityManager.pickups.add(itemDropped);
                                itemsDropped.add(itemDropped);
                            }
                        }
                    }
                } else {
                    if (dropsEvent.dropPos != null && dropsEvent.drops != null) {
                        for(InventoryItem item : dropsEvent.drops) {
                            ItemPickupEntity itemDropped = item.getPickupEntity(level, (float)dropsEvent.dropPos.x, (float)dropsEvent.dropPos.y);
                            level.entityManager.pickups.add(itemDropped);
                            itemsDropped.add(itemDropped);
                        }
                    }
                }
            } else {
                if (dropsEvent.dropPos != null && dropsEvent.drops != null) {
                    for(InventoryItem item : dropsEvent.drops) {
                        ItemPickupEntity itemDropped = item.getPickupEntity(level, (float)dropsEvent.dropPos.x, (float)dropsEvent.dropPos.y);
                        level.entityManager.pickups.add(itemDropped);
                        itemsDropped.add(itemDropped);
                    }
                }
            }
            if (dropsEvent.dropPos != null && dropsEvent.drops != null) {
                for(InventoryItem item : dropsEvent.drops) {
                    ItemPickupEntity itemDropped = item.getPickupEntity(level, (float)dropsEvent.dropPos.x, (float)dropsEvent.dropPos.y);
                    level.entityManager.pickups.add(itemDropped);
                    itemsDropped.add(itemDropped);
                }
            }
        }

        if (client != null) {
            client.newStats.tiles_mined.increment(1);
        }

        if (!level.isServer()) {
            gameTile.spawnDestroyedParticles(level, x, y);
        }

        level.setTile(x, y, gameTile.getDestroyedTile());
    }
}
