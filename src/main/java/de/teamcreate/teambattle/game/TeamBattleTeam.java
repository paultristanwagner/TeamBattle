package de.teamcreate.teambattle.game;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class TeamBattleTeam {

    private TeamHandler teamHandler;
    @Getter
    private int teamSize;
    @Getter
    private int teamId;
    @Getter
    private String teamName;
    @Getter
    private List<Player> members = new ArrayList<>();
    @Getter
    private Scoreboard scoreboard;


    TeamBattleTeam( TeamHandler teamHandler, int teamId, int teamSize ) {
        this.teamHandler = teamHandler;
        this.teamId = teamId;
        this.teamSize = teamSize;
        teamName = "Team #" + teamId;
    }

    void setupScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        teamHandler.getTeams().forEach( teamBattleTeam -> createTeam( scoreboard, teamBattleTeam ) );
    }

    private void createTeam( Scoreboard scoreboard, TeamBattleTeam teamBattleTeam ) {
        Team team = scoreboard.getTeam( String.valueOf( teamBattleTeam.getTeamName() ) );
        if ( team != null ) {
            team.unregister();
        }
        team = scoreboard.registerNewTeam( String.valueOf( teamBattleTeam.getTeamName() ) );
        if ( teamBattleTeam == this ) {
            team.setPrefix( "§aT" + teamBattleTeam.getTeamId() + " | " );
        } else {
            team.setPrefix( "T" + teamBattleTeam.getTeamId() + " | " );
        }
    }

    boolean isMember( Player player ) {
        return members.stream().anyMatch( member -> member.getUniqueId().equals( player.getUniqueId() ) );
    }

    void handleMemberAdd( TeamBattleTeam teamBattleTeam, Player player ) {
        if ( teamBattleTeam == this ) {
            members.add( player );
            player.setScoreboard( scoreboard );
        }
        Team team = scoreboard.getTeam( teamBattleTeam.getTeamName() );
        team.addEntry( player.getName() );
    }

    void handleMemberRemove( TeamBattleTeam teamBattleTeam, Player player ) {
        if ( teamBattleTeam == this ) {
            members.remove( player );
            player.setScoreboard( Bukkit.getScoreboardManager().getMainScoreboard() );
        }
        Team team = scoreboard.getTeam( teamBattleTeam.getTeamName() );
        team.removeEntry( player.getName() );
    }

    boolean arePlayersOnline() {
        return members.stream().allMatch( Player::isOnline );
    }

    public boolean isFull() {
        return members.size() == teamSize;
    }
}
