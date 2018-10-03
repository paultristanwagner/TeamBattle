package de.teamcreate.teambattle.game;

import de.teamcreate.teambattle.event.PlayerJoinTeamEvent;
import lombok.Getter;
import de.teamcreate.teambattle.event.PlayerQuitTeamEvent;
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

    public static final int MAXIMUM_MEMBERS = 3;

    private Scoreboard scoreboard;
    private Team scoreboardTeam;

    @Getter
    private String teamName;
    @Getter
    private List<Player> members = new ArrayList<>();

    public TeamBattleTeam( int teamId ) {
        this.teamName = "Team #" + teamId;
        setupScoreboard();
    }

    private void setupScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        scoreboardTeam = scoreboard.getTeam( teamName );
        if(scoreboardTeam != null) {
            scoreboardTeam.unregister();
        }
        scoreboardTeam = scoreboard.registerNewTeam( teamName );
        scoreboardTeam.setPrefix( "§a" );
    }

    public boolean isMember( Player player ) {
        return members.contains( player );
    }

    public void addMember( Player player ) {
        members.add( player );
        scoreboardTeam.addEntry( player.getName() );
        player.setScoreboard( scoreboard );
        Bukkit.getPluginManager().callEvent( new PlayerJoinTeamEvent( player, this ) );
    }

    public void removeMember( Player player ) {
        members.remove( player );
        scoreboardTeam.removeEntry( player.getName() );
        player.setScoreboard( Bukkit.getScoreboardManager().getMainScoreboard() );
        Bukkit.getPluginManager().callEvent( new PlayerQuitTeamEvent( player, this ) );
    }

    public void switchTeams( Player player, TeamBattleTeam newTeam ) {
        members.remove( player );
        scoreboardTeam.removeEntry( player.getName() );
        player.setScoreboard( Bukkit.getScoreboardManager().getMainScoreboard() );
        Bukkit.getPluginManager().callEvent( new PlayerQuitTeamEvent( player, this, true) );
        newTeam.addMember( player );
    }

    public boolean isFull() {
        return members.size() == MAXIMUM_MEMBERS;
    }
}
