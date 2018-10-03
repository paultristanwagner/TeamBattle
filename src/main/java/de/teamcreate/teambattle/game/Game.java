package de.teamcreate.teambattle.game;

import de.teamcreate.teambattle.TeamBattlePlugin;
import de.teamcreate.teambattle.countdown.PregameCountdown;
import de.teamcreate.teambattle.event.GameStateChangeEvent;
import de.teamcreate.teambattle.util.ItemUtils;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
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
    private Location spawnLocation;
    private GameState gameState;
    private TeamHandler teamHandler;
    private PregameCountdown pregameCountdown;

    public Game( TeamBattlePlugin teamBattlePlugin ) {
        this.teamBattlePlugin = teamBattlePlugin;
        this.teamHandler = new TeamHandler( 12 );
        this.gameState = GameState.PREGAME;
        fetchSpawnLocation();
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
        player.getInventory().setItem( 0, ItemUtils.TEAM_SELECT );
        player.getInventory().setItem( 4, ItemUtils.RULES );
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
        player.getAttribute( Attribute.GENERIC_MAX_HEALTH ).setBaseValue( 20.0 );
        player.setHealth( player.getAttribute( Attribute.GENERIC_MAX_HEALTH ).getBaseValue() );
        player.setFoodLevel( 20 );
        player.setSaturation( 6 );
        player.setLevel( 0 );
        player.setExp( 0 );
        player.teleport( spawnLocation );
    }

    public void setSpectator( Player player ) {
        player.setGameMode( GameMode.SPECTATOR );
        sendGameMessage( player, "Du bist nun Zuschauer!" );
        player.teleport( spawnLocation );
    }

    public void attemptStart( Player operator ) {
        boolean startable = true;
        for ( Player player : Bukkit.getOnlinePlayers() ) {
            TeamBattleTeam team = teamHandler.getTeam( player );
            if ( team == null ) {
                startable = false;
                sendGameMessage( operator, "§cDas Spiel kann nicht gestartet werden, da §e" + player.getName() +
                        " §cnoch keinem Team zugehörig ist!" );
            }
        }
        if ( startable ) {
            changeGameState();
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

    private void fetchSpawnLocation() {
        FileConfiguration fileConfiguration = teamBattlePlugin.getConfig();
        String worldName = fileConfiguration.getString( "gameSpawn.world", "world" );
        double x = fileConfiguration.getDouble( "gameSpawn.x", 0 );
        double y = fileConfiguration.getDouble( "gameSpawn.y", 5 );
        double z = fileConfiguration.getDouble( "gameSpawn.z", 0 );
        float yaw = ( (float) fileConfiguration.getDouble( "gameSpawn.yaw", 0 ) );
        float pitch = ( (float) fileConfiguration.getDouble( "gameSpawn.pitch", 0 ) );
        World world = Bukkit.getWorld( worldName );
        if ( world == null ) {
            world = Bukkit.getWorlds().get( 0 );
        }
        this.spawnLocation = new Location( world, x, y, z, yaw, pitch );
    }

    public void win( TeamBattleTeam winnerTeam ) {
        stopCountdown();
        List<Player> members = winnerTeam.getMembers();
        if ( members.size() == 1 ) {
            Player winner = members.get( 0 );
            sendGameMessage( "§eDer Spieler §b" + winner.getName() + " §ehat das Spiel gewonnen!" );
            playGameSound( Sound.ENTITY_FIREWORK_TWINKLE, 1, 1 );
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for ( int i = 0; i < members.size(); i++ ) {
                if ( i < members.size() - 1 ) {
                    stringBuilder.append( " §7und " );
                } else {
                    stringBuilder.append( " §7, " );
                }
                Player member = members.get( i );
                stringBuilder.append( "§b" ).append( member.getName() );
            }
            String winners = stringBuilder.substring( 5 );
            sendGameMessage( "§eDie Spieler §b" + winners + " §ehaben das Spiel gewonnen!" );
        }
        changeGameState( GameState.END );
    }
}
