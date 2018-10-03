package de.teamcreate.teambattle.util;

import de.teamcreate.itemcommons.ItemBuilder;
import de.teamcreate.teambattle.game.TeamBattleTeam;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class ItemUtils {

    private ItemUtils() {
        throw new IllegalStateException( "Utility class" );
    }

    public static final ItemStack TEAM_SELECT = new ItemBuilder( Material.BED ).setName( "§aTeamauswahl" )
            .setDurability( 14 ).build();

    public static ItemStack getTeamItemStack( Player player, TeamBattleTeam teamBattleTeam ) {
        ItemBuilder itemBuilder = new ItemBuilder( Material.WOOL );
        if ( teamBattleTeam.isFull() ) {
            itemBuilder.setDurability( 14 );
        } else if ( !teamBattleTeam.getMembers().isEmpty() ) {
            itemBuilder.setDurability( 8 );
        }
        itemBuilder.setName( "§e" + teamBattleTeam.getTeamName() ).addLore(
                "§3" + teamBattleTeam.getMembers().size() + "§7/§3" + TeamBattleTeam.MAXIMUM_MEMBERS,
                "§7--------------------------"
        );
        List<Player> members = teamBattleTeam.getMembers();
        for ( int i = 1; i <= TeamBattleTeam.MAXIMUM_MEMBERS; i++ ) {
            if ( i <= members.size() ) {
                Player member = members.get( i - 1 );
                if ( !member.equals( player ) ) {
                    itemBuilder.addLore( "§7" + i + ". §e" + member.getName() );
                } else {
                    itemBuilder.addLore( "§7" + i + ". §5" + member.getName() );
                }
            } else {
                itemBuilder.addLore( "§7" + i + ". -");
            }
        }
        return itemBuilder.build();
    }
}
