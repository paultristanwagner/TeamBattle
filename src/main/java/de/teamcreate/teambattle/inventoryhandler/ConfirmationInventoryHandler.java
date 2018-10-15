package de.teamcreate.teambattle.inventoryhandler;

import de.teamcreate.inventorycommons.InventoryHandler;
import de.teamcreate.teambattle.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * JavaDoc this file!
 * Created: 04.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class ConfirmationInventoryHandler extends InventoryHandler {

    public ConfirmationInventoryHandler( JavaPlugin javaPlugin, Player player, InventoryHandler parent ) {
        super( javaPlugin, player, parent );
    }

    @Override
    protected void clickItem( InventoryClickEvent event ) {
        event.setCancelled( true );
        if ( event.getCurrentItem().isSimilar( ItemUtils.ABORT ) ) {
            if ( parent != null ) {
                parent.openWithSoundEffect( Sound.NOTE_BASS );
            } else {
                player.closeInventory();
            }
        } else if ( event.getCurrentItem().isSimilar( ItemUtils.CONFIRM ) ) {
            player.performCommand( "teambattle start" );
            player.closeInventory();
        }
    }

    @Override
    protected Inventory createInventory() {
        inventory = Bukkit.createInventory( null, 27, "§cBestätigen§7:" );
        for ( int i = 0; i < 9; i++ )
            inventory.setItem( i, ItemUtils.SPACER );
        for ( int i = 18; i < 27; i++ )
            inventory.setItem( i, ItemUtils.SPACER );
        inventory.setItem( 11, ItemUtils.CONFIRM );
        inventory.setItem( 15, ItemUtils.ABORT );
        return inventory;
    }
}
