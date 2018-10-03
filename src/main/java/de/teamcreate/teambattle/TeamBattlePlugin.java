package de.teamcreate.teambattle;

import de.teamcreate.teambattle.executor.TeamBattleExecutor;
import lombok.Getter;
import de.teamcreate.teambattle.game.Game;
import de.teamcreate.teambattle.listener.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class TeamBattlePlugin extends JavaPlugin {

    public static final String PREFIX = "§7[§bTeamBattle§7] ";

    @Getter
    private Game game;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.game = new Game( this );
        getCommand( "teambattle" ).setExecutor( new TeamBattleExecutor( this ) );
        Bukkit.getPluginManager().registerEvents( new GameListener( this ), this );
    }
}
