package de.teamcreate.teambattle.game;

import de.teamcreate.teambattle.TeamBattlePlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * JavaDoc this file!
 * Created: 23.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
@AllArgsConstructor
@Getter
public class GameConfig {

    private Location spawnLocation;
    private int arenaSize;
    private int finalSize;
    private int protectionMinutes;
    private int fightingMinutes;
    private int teams;
    private int teamSize;
    private GameRegion spawnRegion;

    static GameConfig loadConfig( TeamBattlePlugin plugin ) {
        FileConfiguration config = plugin.getConfig();
        Location spawnLocation = ( (Location) config.get( "gameSpawn" ) );
        int arenaSize = config.getInt( "arenaSize" );
        int finalSize = config.getInt( "finalSize" );
        int protectionMinutes = config.getInt( "protectionMinutes" );
        int fightingMinutes = config.getInt( "fightingMinutes" );
        int teams = config.getInt( "teams" );
        int teamSize = config.getInt( "teamSize" );
        GameRegion spawnRegion = new GameRegion( plugin, ( (Location) config.get( "spawnRegion.pos1" ) ),
                ( (Location) config.get( "spawnRegion.pos2" ) ) );
        return new GameConfig( spawnLocation, arenaSize, finalSize, protectionMinutes, fightingMinutes, teams, teamSize, spawnRegion );
    }
}
