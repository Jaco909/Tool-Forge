package gunsmith.forms.gunsmith;

import necesse.engine.GameTileRange;
import necesse.engine.GlobalData;
import necesse.engine.Settings;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.RecipeTechRegistry;
import necesse.entity.objectEntity.interfaces.OEInventory;
import necesse.inventory.Inventory;
import necesse.inventory.InventoryItem;
import necesse.inventory.InventoryRange;
import necesse.inventory.container.Container;
import necesse.inventory.container.customAction.EmptyCustomAction;
import necesse.inventory.container.object.CraftingStationContainer;
import necesse.inventory.container.slots.ContainerSlot;
import necesse.inventory.item.upgradeUtils.UpgradableItem;
import necesse.inventory.item.upgradeUtils.UpgradedItem;
import necesse.inventory.recipe.CanCraft;
import necesse.inventory.recipe.Recipe;
import necesse.level.maps.Level;
import necesse.level.maps.multiTile.MultiTile;
import gunsmith.objects.gunsmithStation.GunsmithStationObjectEntity;

import java.awt.*;
import java.util.*;

public class GunsmithContainer extends Container {
    public final int BARREL_SLOT;
    public final int BODY_SLOT;
    public final int STOCK_SLOT;
    public String NAME;
    public static int MAX_NAME_LENGTH = 40;
    public final GunsmithStationObjectEntity upgradeEntity;
    public final EmptyCustomAction craftButton;
    public final GameTileRange ingredientRange;
    private LinkedHashSet<Inventory> nearbyInventories = new LinkedHashSet();

    public GunsmithContainer(final NetworkClient client, int uniqueSeed, GunsmithStationObjectEntity upgradeEntity, PacketReader reader) {
        super(client, uniqueSeed);
        this.upgradeEntity = upgradeEntity;
        this.BARREL_SLOT = this.addSlot(new GunPartItemContainerSlot(upgradeEntity.inventory, 0));
        this.addInventoryQuickTransfer(this.BARREL_SLOT, this.BARREL_SLOT);
        this.BODY_SLOT = this.addSlot(new GunPartItemContainerSlot(upgradeEntity.inventory, 1));
        this.addInventoryQuickTransfer(this.BODY_SLOT, this.BODY_SLOT);
        this.STOCK_SLOT = this.addSlot(new GunPartItemContainerSlot(upgradeEntity.inventory, 2));
        this.NAME = "";
        this.addInventoryQuickTransfer(this.STOCK_SLOT, this.STOCK_SLOT);
        MultiTile multiTile = upgradeEntity.getLevelObject().getMultiTile();
        Rectangle tileRectangle = multiTile.getTileRectangle(0, 0);
        this.ingredientRange = new GameTileRange(CraftingStationContainer.nearbyCraftTileRange, tileRectangle);
        this.nearbyInventories.addAll(this.craftInventories);
        //Iterator var7 = this.getNearbyInventories(upgradeEntity.getLevel(), upgradeEntity.getTileX(), upgradeEntity.getTileY(), this.ingredientRange, OEInventory::canUseForNearbyCrafting).iterator();

        /*while (var7.hasNext()) {
            InventoryRange inventoryRange = (InventoryRange) var7.next();
            this.nearbyInventories.add(inventoryRange.inventory);
        }*/

        this.craftButton = (EmptyCustomAction) this.registerAction(new EmptyCustomAction() {
            protected void run() {
                if (sameType()) {
                    InventoryItem result = getCraftedItem();
                    if (GunsmithContainer.this.getSlot(GunsmithContainer.this.CLIENT_DRAGGING_SLOT).isClear()) {
                        GunsmithContainer.this.getSlot(GunsmithContainer.this.CLIENT_DRAGGING_SLOT).setItem(result);
                        GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).setItem((InventoryItem) null);
                        GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).setItem((InventoryItem) null);
                        GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).setItem((InventoryItem) null);
                    } else {
                        GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).setItem(result);
                        GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).setItem((InventoryItem) null);
                        GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).setItem((InventoryItem) null);
                    }
                }
            }
        });
    }

    public boolean sameType() {
        if (GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).isClear() || GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).isClear() || GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).isClear()) {
            return false;
        } else {
            if (GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).getItem().item.getDefaultItem(null, 1).getGndData().getString("Type").equalsIgnoreCase("ToolShaft") && (GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().item.getDefaultItem(null, 1).getGndData().getString("Type").equalsIgnoreCase("ModularPickaxe") || GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().item.getDefaultItem(null, 1).getGndData().getString("Type").equalsIgnoreCase("ModularShovel") || GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().item.getDefaultItem(null, 1).getGndData().getString("Type").equalsIgnoreCase("ModularAxe"))) {
                return GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().item.getDefaultItem(null, 1).getGndData().getString("Type").equalsIgnoreCase(GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).getItem().item.getDefaultItem(null, 1).getGndData().getString("Type"));
            } else {
                return GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().item.getDefaultItem(null, 1).getGndData().getString("Type").equalsIgnoreCase(GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).getItem().item.getDefaultItem(null, 1).getGndData().getString("Type")) && GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).getItem().item.getDefaultItem(null, 1).getGndData().getString("Type").equalsIgnoreCase(GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).getItem().item.getDefaultItem(null, 1).getGndData().getString("Type"));
            }
        }
    }

    public CanCraft canUpgrade(UpgradedItem upgradedItem, boolean countAllIngredients) {
        if (upgradedItem != null) {
            Recipe recipe = new Recipe("air", RecipeTechRegistry.NONE, upgradedItem.cost);
            return this.canCraftRecipe(recipe, this.getCraftInventories(), countAllIngredients);
        } else {
            return null;
        }
    }

    private boolean useNearbyInventories() {
        return this.client.isServer() ? this.client.craftingUsesNearbyInventories : (Boolean) Settings.craftingUseNearby.get();
    }

    public Collection<Inventory> getCraftInventories() {
        return (Collection) (this.useNearbyInventories() ? this.nearbyInventories : super.getCraftInventories());
    }

    public UpgradedItem getUpgradedItem() {
        ContainerSlot slot = this.getSlot(this.BARREL_SLOT);
        if (!slot.isClear()) {
            InventoryItem slotItem = slot.getItem();
            if (slotItem.item instanceof UpgradableItem && ((UpgradableItem) slotItem.item).getCanBeUpgradedError(slotItem) == null) {
                return ((UpgradableItem) slotItem.item).getUpgradedItem(slotItem);
            }
        }

        return null;
    }

    public InventoryItem getCraftedItem() {
        if (sameType()) {
            InventoryItem result = new InventoryItem(ItemRegistry.getItem(GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().item.getDefaultItem(null,1).getGndData().getString("Type")),1);
            result.getGndData().setString("BarrelItem",GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().item.getDefaultItem(null,1).getGndData().getString("BarrelItem"));
            result.getGndData().setString("BodyItem",GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).getItem().item.getDefaultItem(null,1).getGndData().getString("BodyItem"));
            result.getGndData().setString("StockItem",GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).getItem().item.getDefaultItem(null,1).getGndData().getString("StockItem"));

            GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().getGndData().copyKeysToTarget(result.getGndData(),"BarrelBonus","BarrelBonusValue");
            GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).getItem().getGndData().copyKeysToTarget(result.getGndData(),"BodyBonus","BodyBonusValue");
            GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).getItem().getGndData().copyKeysToTarget(result.getGndData(),"StockBonus","StockBonusValue");

            /*if (GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().getGndData().hasKey("Bonus")) {
                result.getGndData().setString("BarrelBonus",GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().getGndData().getString("Bonus"));
            }
            if (GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).getItem().getGndData().hasKey("Bonus")) {
                result.getGndData().setString("BodyBonus",GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).getItem().getGndData().getString("Bonus"));
            }
            if (GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).getItem().getGndData().hasKey("Bonus")) {
                result.getGndData().setString("StockBonus",GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).getItem().getGndData().getString("Bonus"));
            }*/

            if (GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().item.getDefaultItem(null,1).getGndData().hasKey("upgradeLevel") || GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).getItem().item.getDefaultItem(null,1).getGndData().hasKey("upgradeLevel") || GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).getItem().item.getDefaultItem(null,1).getGndData().hasKey("upgradeLevel")) {
                int[] arr = {GunsmithContainer.this.getSlot(GunsmithContainer.this.BARREL_SLOT).getItem().item.getDefaultItem(null,1).getGndData().getInt("upgradeLevel",1),GunsmithContainer.this.getSlot(GunsmithContainer.this.BODY_SLOT).getItem().item.getDefaultItem(null,1).getGndData().getInt("upgradeLevel",1),GunsmithContainer.this.getSlot(GunsmithContainer.this.STOCK_SLOT).getItem().item.getDefaultItem(null,1).getGndData().getInt("upgradeLevel",1)};
                int upgValue = 0;
                for (int i = 0; i <= 2; i++) {
                    if (i == 0) {
                        upgValue = arr[i];
                    } else if (arr[i] > upgValue) {
                        upgValue = arr[i];
                    }
                }
                result.getGndData().setInt("upgradeLevel",upgValue);
            }
            return result;
        } else {
            return null;
        }
    }


    public void tick() {
        super.tick();
        if (this.client.isClient() && (Boolean) Settings.craftingUseNearby.get()) {
            boolean updateCraftable = false;

            Inventory inv;
            for (Iterator var2 = this.nearbyInventories.iterator(); var2.hasNext(); inv.clean()) {
                inv = (Inventory) var2.next();
                if (inv.isDirty()) {
                    updateCraftable = true;
                }
            }

            if (updateCraftable) {
                GlobalData.updateCraftable();
            }
        }

    }

    public boolean isValid(ServerClient client) {
        if (!super.isValid(client)) {
            return false;
        } else {
            return !this.upgradeEntity.removed();
        }
    }

    public static void openAndSendContainer(int containerID, ServerClient client, Level level, int tileX, int tileY, Packet extraContent) {
        if (!level.isServer()) {
            throw new IllegalStateException("Level must be a server level");
        } else {
            Packet packet = new Packet();
            PacketWriter writer = new PacketWriter(packet);
            if (extraContent != null) {
                writer.putNextContentPacket(extraContent);
            }

            PacketOpenContainer p = PacketOpenContainer.LevelObject(containerID, tileX, tileY, packet);
            ContainerRegistry.openAndSendContainer(client, p);
        }
    }

    public static void openAndSendContainer(int containerID, ServerClient client, Level level, int tileX, int tileY) {
        openAndSendContainer(containerID, client, level, tileX, tileY, (Packet) null);
    }
}