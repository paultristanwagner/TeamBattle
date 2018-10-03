package de.teamcreate.teambattle.event;

import de.teamcreate.teambattle.game.TeamBattleTeam;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class PlayerQuitTeamEvent extends PlayerEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    @Getter
    private TeamBattleTeam team;
    @Getter
    private boolean switching;

    public PlayerQuitTeamEvent( Player who, TeamBattleTeam team ) {
        super( who );
        this.team = team;
        this.switching = false;
    }

    public PlayerQuitTeamEvent( Player who, TeamBattleTeam team, boolean switching ) {
        super( who );
        this.team = team;
        this.switching = switching;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
