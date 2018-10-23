package de.teamcreate.teambattle.game;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * JavaDoc this file!
 * Created: 23.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class ReconnectHandler {

    private Game game;
    private Map<UUID, ReconnectProfile> reconnectProfileMap;

    ReconnectHandler( Game game ) {
        this.game = game;
        reconnectProfileMap = Maps.newHashMap();
    }

    public void handleAccidentalDisconnect( Player player ) {
        reconnectProfileMap.put( player.getUniqueId(), new ReconnectProfile( player ) );
    }

    public void useReconnectProfile( Player player ) {
        TeamBattleTeam team = game.getTeamHandler().getTeam( player );
        UUID uniqueId = player.getUniqueId();
        team.getMembers().removeIf( member -> member.getUniqueId().equals( uniqueId ) );
        team.getMembers().add( player );
        player.setScoreboard( team.getScoreboard() );
        reconnectProfileMap.get( uniqueId ).use( player );
        reconnectProfileMap.remove( uniqueId );
    }

    public boolean hasReconnectProfile( Player player ) {
        return reconnectProfileMap.containsKey( player.getUniqueId() );
    }
}
