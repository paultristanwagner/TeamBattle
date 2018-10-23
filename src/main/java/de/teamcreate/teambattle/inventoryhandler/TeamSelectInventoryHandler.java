package de.teamcreate.teambattle.inventoryhandler;

import de.teamcreate.inventorycommons.InventoryHandler;
import de.teamcreate.teambattle.TeamBattlePlugin;
import de.teamcreate.teambattle.game.Game;
import de.teamcreate.teambattle.game.TeamBattleTeam;
import de.teamcreate.teambattle.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class TeamSelectInventoryHandler extends InventoryHandler {

    private Game game;

    public TeamSelectInventoryHandler( TeamBattlePlugin teamBattlePlugin, Player player, InventoryHandler parent ) {
        super( teamBattlePlugin, player, parent );
        this.game = teamBattlePlugin.getGame();
        this.game.getTeamHandler().registerInventory( this );
    }

    @Override
    protected void clickItem( InventoryClickEvent event ) {
        event.setCancelled( true );
        if ( event.getInventory().getType() == InventoryType.CHEST ) {
            ItemStack itemStack = event.getCurrentItem();
            String displayName = itemStack.hasItemMeta() ? itemStack.getItemMeta().getDisplayName() : null;
            displayName = ChatColor.stripColor( displayName );
            TeamBattleTeam newTeam = game.getTeamHandler().getTeam( displayName );
            if ( newTeam != null ) {
                if ( !newTeam.isFull() ) {
                    TeamBattleTeam currentTeam = game.getTeamHandler().getTeam( player );
                    if ( currentTeam != null ) {
                        game.getTeamHandler().switchTeams( currentTeam, newTeam, player );
                    } else {
                        game.getTeamHandler().joinTeam( newTeam, player );
                    }
                    player.playSound( player.getLocation(), Sound.NOTE_PLING, 1, 1 );
                } else {
                    player.playSound( player.getLocation(), Sound.ITEM_BREAK, 1, 1 );
                }
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        this.game.getTeamHandler().unregisterInventory( this );
    }

    @Override
    protected Inventory createInventory() {
        int inventorySize = ( (int) ( Math.ceil( game.getGameConfig().getTeams() / 9.0 ) * 9 ) );
        inventory = Bukkit.createInventory( null, inventorySize, "§aTeamauswahl§7:" );
        fillInventory();
        return inventory;
    }

    public void fillInventory() {
        for ( int i = 0; i < game.getTeamHandler().getTeams().size(); i++ ) {
            TeamBattleTeam team = game.getTeamHandler().getTeams().get( i );
            inventory.setItem( i, ItemUtils.getTeamItemStack( player, team ) );
        }
    }
}
