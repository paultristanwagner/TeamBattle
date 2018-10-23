package de.teamcreate.teambattle.game;

import de.teamcreate.teambattle.TeamBattlePlugin;
import de.teamcreate.teambattle.countdown.PregameCountdown;
import de.teamcreate.teambattle.event.GameStateChangeEvent;
import de.teamcreate.teambattle.util.ItemUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
@Getter
public class Game {

    private TeamBattlePlugin teamBattlePlugin;

    private GameConfig gameConfig;
    private GameState gameState;
    private BorderHandler borderHandler;
    private TeamHandler teamHandler;
    private ReconnectHandler reconnectHandler;
    private PregameCountdown pregameCountdown;

    public Game( TeamBattlePlugin teamBattlePlugin ) {
        this.teamBattlePlugin = teamBattlePlugin;

        gameConfig = GameConfig.loadConfig( teamBattlePlugin );
        borderHandler = new BorderHandler( gameConfig );
        teamHandler = new TeamHandler( gameConfig.getTeams(), gameConfig.getTeamSize() );
        reconnectHandler = new ReconnectHandler( this );
        gameState = GameState.PREGAME;

        World world = gameConfig.getSpawnLocation().getWorld();
        world.setThundering( false );
        world.setStorm( false );
        world.setTime( 1000 );
        world.setGameRuleValue( "doDaylightCycle", "false" );
    }

    public void sendGameMessage( String message ) {
        Bukkit.broadcastMessage( TeamBattlePlugin.PREFIX + message );
    }

    public void sendGameMessage( Player receiver, String message ) {
        receiver.sendMessage( TeamBattlePlugin.PREFIX + message );
    }

    public void sendPlayerMessage( Player sender, String message ) {
        Bukkit.broadcastMessage( "§a" + sender.getName() + "§7: " + message );
    }

    public void sendSpectatorMessage( Player sender, String message ) {
        Bukkit.getOnlinePlayers().forEach( player -> {
            if ( player.getGameMode() == GameMode.SPECTATOR ) {
                player.sendMessage( "§7[§4§lX§7] " + sender.getName() + ": " + message );
            }
        } );
    }

    public void playGameSound( Sound sound, float v0, float v1 ) {
        Bukkit.getOnlinePlayers().forEach( player -> player.playSound( player.getLocation(), sound, v0, v1 ) );
    }

    public void setPlayer( Player player ) {
        resetPlayer( player );
        player.teleport( gameConfig.getSpawnLocation() );
        player.getInventory().setItem( 0, ItemUtils.TEAM_SELECT );
        player.getInventory().setItem( 4, ItemUtils.RULES );
        if ( player.isOp() ) {
            player.getInventory().setItem( 8, ItemUtils.OPERATOR );
        }
    }

    public void resetPlayers() {
        Bukkit.getOnlinePlayers().forEach( this::resetPlayer );
    }

    private void resetPlayer( Player player ) {
        player.setGameMode( GameMode.SURVIVAL );
        player.getInventory().clear();
        player.getInventory().setBoots( null );
        player.getInventory().setLeggings( null );
        player.getInventory().setChestplate( null );
        player.getInventory().setHelmet( null );
        player.getActivePotionEffects().stream().map( PotionEffect::getType ).forEach( player::removePotionEffect );
        player.setMaxHealth( 20.0 );
        player.setHealth( player.getMaxHealth() );
        player.setFoodLevel( 20 );
        player.setSaturation( 6 );
        player.setLevel( 0 );
        player.setExp( 0 );
    }

    public void setSpectator( Player player ) {
        player.setGameMode( GameMode.SPECTATOR );
        player.teleport( gameConfig.getSpawnLocation() );
        sendGameMessage( player, "Du bist nun Zuschauer!" );
    }

    public void attemptStart( Player operator ) {
        boolean bootAble = true;
        StringBuilder builder = new StringBuilder();
        for ( Player player : Bukkit.getOnlinePlayers() ) {
            TeamBattleTeam team = teamHandler.getTeam( player );
            if ( team == null ) {
                bootAble = false;
                builder.append( ", " ).append( player.getName() );
            }
        }
        if ( builder.length() > 0 ) {
            bootAble = false;
            sendGameMessage( operator, "§cDas Spiel kann nicht gestartet werden, da folgende§7/§cr Spieler noch keinem " +
                    "Team zugeordnet sind§7/§cist§7: §b" + builder.substring( 2 ) );
        }
        if ( teamHandler.getUsedTeams() <= 1 ) {
            sendGameMessage( operator, "§cDas Spiel kann nicht gestartet werden, da nicht genügend Teams teilnehmen!" );
            bootAble = false;
        }
        if ( bootAble ) {
            changeGameState();
        } else {
            operator.playSound( operator.getLocation(), Sound.NOTE_BASS, 1, 1 );
        }
    }

    public void changeGameState() {
        GameState newGameState = gameState.getNextState();
        if ( newGameState != null ) {
            changeGameState( newGameState );
        }
    }

    private void changeGameState( GameState newGameState ) {
        Bukkit.getPluginManager().callEvent( new GameStateChangeEvent( newGameState ) );
        this.gameState = newGameState;
    }

    public void startCountdown() {
        if ( pregameCountdown == null ) {
            pregameCountdown = new PregameCountdown( teamBattlePlugin );
            pregameCountdown.start();
        }
    }

    private void stopCountdown() {
        if ( pregameCountdown != null ) {
            pregameCountdown.stop();
        }
    }

    public void win( TeamBattleTeam winnerTeam ) {
        stopCountdown();
        List<Player> members = winnerTeam.getMembers();
        if ( members.size() == 1 ) {
            Player winner = members.get( 0 );
            sendGameMessage( "§eDer Spieler §b" + winner.getName() + " §ehat das Spiel gewonnen!" );
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for ( int i = 0; i < members.size(); i++ ) {
                if ( i < members.size() - 1 ) {
                    stringBuilder.append( " §7, " );
                } else {
                    stringBuilder.append( " §7und " );
                }
                Player member = members.get( i );
                stringBuilder.append( "§b" ).append( member.getName() );
            }
            String winners = stringBuilder.substring( 5 );
            sendGameMessage( "§eDie Spieler §b" + winners + " §ehaben das Spiel gewonnen!" );
        }
        playGameSound( Sound.FIREWORK_TWINKLE, 1, 1 );
        changeGameState( GameState.END );
    }
}