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
public class OperatorInventoryHandler extends InventoryHandler {

    public OperatorInventoryHandler( JavaPlugin javaPlugin, Player player, InventoryHandler parent ) {
        super( javaPlugin, player, parent );
    }

    @Override
    protected void clickItem( InventoryClickEvent event ) {
        event.setCancelled( true );
        if ( event.getCurrentItem().isSimilar( ItemUtils.START_GAME ) ) {
            new ConfirmationInventoryHandler( javaPlugin, player, this ).openWithSoundEffect( Sound.BLOCK_NOTE_PLING );
        }
    }

    @Override
    protected Inventory createInventory() {
        inventory = Bukkit.createInventory( null, 27, "§5TeamBattle §7- §5Administration" );
        for ( int i = 0; i < 9; i++ )
            inventory.setItem( i, ItemUtils.SPACER );
        for ( int i = 18; i < 27; i++ )
            inventory.setItem( i, ItemUtils.SPACER );
        inventory.setItem( 11, ItemUtils.PLUGIN );
        inventory.setItem( 15, ItemUtils.START_GAME );
        return inventory;
    }
}
