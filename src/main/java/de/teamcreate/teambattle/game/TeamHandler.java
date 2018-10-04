package de.teamcreate.teambattle.game;

import com.google.common.collect.Lists;
import de.teamcreate.teambattle.inventoryhandler.TeamSelectInventoryHandler;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class TeamHandler {

    @Getter
    private List<TeamBattleTeam> teams = Lists.newArrayList();
    @Getter
    private List<TeamSelectInventoryHandler> teamSelectInventoryHandlers = Lists.newArrayList();

    public TeamHandler( int teams ) {
        for ( int i = 1; i <= teams; i++ ) {
            this.teams.add( new TeamBattleTeam( i ) );
        }
    }

    public TeamBattleTeam getTeam( Player player ) {
        return teams.stream().filter( teamBattleTeam -> teamBattleTeam.isMember( player ) ).findFirst().orElse( null );
    }

    public TeamBattleTeam getTeam( String teamName ) {
        return teams.stream().filter( teamBattleTeam -> teamBattleTeam.getTeamName().equals( teamName ) ).findFirst().orElse( null );
    }

    public int getUsedTeams() {
        return (int) teams.stream().filter( team -> !team.getMembers().isEmpty() ).count();
    }

    public void updateInventories() {
        teamSelectInventoryHandlers.forEach( TeamSelectInventoryHandler::fillInventory );
    }

    public void registerInventory( TeamSelectInventoryHandler teamSelectInventoryHandler ) {
        teamSelectInventoryHandlers.add( teamSelectInventoryHandler );
    }

    public void unregisterInventory( TeamSelectInventoryHandler teamSelectInventoryHandler ) {
        teamSelectInventoryHandlers.remove( teamSelectInventoryHandler );
    }

    public TeamBattleTeam getRemainingTeam() {
        TeamBattleTeam remaining = null;
        for ( TeamBattleTeam team : teams ) {
            if ( !team.getMembers().isEmpty() ) {
                if ( remaining != null ) {
                    remaining = null;
                    break;
                }
                remaining = team;
            }
        }
        return remaining;
    }

    public boolean areAllied( Player player0, Player player1 ) {
        TeamBattleTeam player0Team = getTeam( player0 );
        TeamBattleTeam player1Team = getTeam( player1 );
        if ( player0Team != null && player1Team != null ) {
            return player0Team == player1Team;
        }
        return false;
    }
}
