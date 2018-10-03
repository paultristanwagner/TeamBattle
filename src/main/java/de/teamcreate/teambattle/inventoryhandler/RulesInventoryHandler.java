package de.teamcreate.teambattle.inventoryhandler;

import de.teamcreate.inventorycommons.InventoryHandler;
import de.teamcreate.teambattle.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class RulesInventoryHandler extends InventoryHandler {

    public RulesInventoryHandler( JavaPlugin javaPlugin, Player player, InventoryHandler parent ) {
        super( javaPlugin, player, parent );
    }

    @Override
    protected void clickItem( InventoryClickEvent event ) {
        event.setCancelled( true );
    }

    @Override
    protected Inventory createInventory() {
        inventory = Bukkit.createInventory( null, 27, "§5Regeln§7:" );
        for ( int i = 0; i < 9; i++)
            inventory.setItem( i, ItemUtils.SPACER );
        for ( int i = 18; i < 27; i++)
            inventory.setItem( i, ItemUtils.SPACER );
        inventory.setItem( 9, ItemUtils.NO_NOTCH_APPLES );
        inventory.setItem( 10, ItemUtils.NO_RIDING );
        inventory.setItem( 11, ItemUtils.NO_PORTALS );
        inventory.setItem( 12, ItemUtils.SURFACE );
        inventory.setItem( 13, ItemUtils.NO_TOWERING );
        inventory.setItem( 14, ItemUtils.FAIR_PLAY );
        return inventory;
    }
}
