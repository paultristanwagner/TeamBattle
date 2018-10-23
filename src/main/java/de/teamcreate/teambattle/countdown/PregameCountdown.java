package de.teamcreate.teambattle.countdown;

import de.teamcreate.teambattle.TeamBattlePlugin;
import de.teamcreate.teambattle.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class PregameCountdown {

    private TeamBattlePlugin teamBattlePlugin;
    private Game game;
    private BukkitTask bukkitTask;
    private int startMinutes;
    private int seconds;

    public PregameCountdown( TeamBattlePlugin teamBattlePlugin ) {
        this.teamBattlePlugin = teamBattlePlugin;
        game = teamBattlePlugin.getGame();
        startMinutes = game.getGameConfig().getProtectionMinutes();
        seconds = startMinutes * 60;
    }

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimer( teamBattlePlugin, this::run, 20L, 20L );
        Bukkit.getScheduler().runTaskLater( teamBattlePlugin, game.getGameConfig().getSpawnRegion()::dissolve, 15 * 20L );
        game.sendGameMessage( "§eIhr habt nun §a" + startMinutes + " Minuten §eZeit um euch auf die Kriegsphase vorzubereiten!" );
        game.sendGameMessage( "§eViel Spaß! c:" );
        game.playGameSound( Sound.LEVEL_UP, 1, 1 );
    }

    private void run() {
        seconds--;
        if ( seconds <= 0 ) {
            game.changeGameState();
            bukkitTask.cancel();
        } else if ( seconds % 300 == 0 || seconds == 180 || seconds == 120 ) {
            int minutes = seconds / 60;
            game.sendGameMessage( "§eDie Kriegsphase beginnt in §5" + minutes + " §eMinuten!" );
            game.playGameSound( Sound.NOTE_PLING, 1, 1 );
        } else if ( seconds == 60 || seconds == 30 || seconds == 20 || seconds == 10 || seconds == 5 || seconds == 4 ||
                seconds == 3 || seconds == 2 ) {
            game.sendGameMessage( "§cDie Kriegsphase beginnt in §5" + seconds + " §cSekunden!" );
            game.playGameSound( Sound.NOTE_PLING, 1, 1 );
        } else if ( seconds == 1 ) {
            game.sendGameMessage( "§cDie Kriegsphase beginnt in einer Sekunde!" );
            game.playGameSound( Sound.NOTE_PLING, 1, 1 );
        }
    }

    public void stop() {
        if ( bukkitTask != null ) {
            bukkitTask.cancel();
        }
    }
}
