package de.teamcreate.teambattle.listener;

import de.teamcreate.teambattle.TeamBattlePlugin;
import de.teamcreate.teambattle.event.GameStateChangeEvent;
import de.teamcreate.teambattle.event.PlayerJoinTeamEvent;
import de.teamcreate.teambattle.event.PlayerQuitTeamEvent;
import de.teamcreate.teambattle.game.Game;
import de.teamcreate.teambattle.game.GameState;
import de.teamcreate.teambattle.game.TeamBattleTeam;
import de.teamcreate.teambattle.inventoryhandler.OperatorInventoryHandler;
import de.teamcreate.teambattle.inventoryhandler.RulesInventoryHandler;
import de.teamcreate.teambattle.inventoryhandler.TeamSelectInventoryHandler;
import de.teamcreate.teambattle.util.ItemUtils;
import org.bukkit.*;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityMountEvent;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class GameListener implements Listener {

    private TeamBattlePlugin teamBattlePlugin;
    private Game game;

    public GameListener( TeamBattlePlugin teamBattlePlugin ) {
        this.teamBattlePlugin = teamBattlePlugin;
        this.game = teamBattlePlugin.getGame();
    }

    @EventHandler
    public void onPlayerCommandPreProcess( PlayerCommandPreprocessEvent event ) {
        if ( event.getMessage().toLowerCase().startsWith( "/me" ) ) {
            event.setCancelled( true );
            event.getPlayer().sendMessage( "§cNerv nicht!" );
        }
    }

    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent event ) {
        event.setJoinMessage( null );
        Player player = event.getPlayer();
        if ( game.getGameState() != GameState.PREGAME ) {
            if ( game.getReconnectHandler().hasReconnectProfile( player ) ) {
                game.getReconnectHandler().useReconnectProfile( player );
                game.sendGameMessage( "§7Der Spieler §e" + player.getName() + " §7ist wieder zurück im Spiel!" );
            } else {
                game.setSpectator( player );
            }
        } else {
            game.setPlayer( player );
        }
    }

    @EventHandler
    public void onPlayerChat( AsyncPlayerChatEvent event ) {
        Player player = event.getPlayer();
        event.setCancelled( true );
        if ( game.getGameState().isSpectatorChatEnabled() && game.getTeamHandler().getTeam( player ) == null ) {
            game.sendSpectatorMessage( player, event.getMessage() );
        } else {
            game.sendPlayerMessage( player, event.getMessage() );
        }
    }

    @EventHandler
    public void onEntityMount( EntityMountEvent event ) {
        if ( event.getEntity() instanceof Player &&
                ( event.getMount() instanceof Horse || event.getMount() instanceof Pig ) ) {
            event.setCancelled( true );
            game.sendGameMessage( ( (Player) event.getEntity() ), "§cDu darfst keine Tiere reiten!" );
        }
    }

    @EventHandler
    public void onPlayerDeath( PlayerDeathEvent event ) {
        event.setDeathMessage( null );
        Player player = event.getEntity();
        TeamBattleTeam team = game.getTeamHandler().getTeam( player );
        if ( team != null ) {
            if ( player.getKiller() != null ) {
                Player killer = player.getKiller();
                game.sendGameMessage( "§7Der Spieler §c" + player.getName() + " §7wurde von §a" + killer.getName() + " §7getötet!" );
                killer.playSound( killer.getLocation(), Sound.LEVEL_UP, 63, 63 );
            } else {
                game.sendGameMessage( "§7Der Spieler §c" + player.getName() + " §7ist gestorben!" );
            }
            game.getTeamHandler().quitTeam( team, player );
            Bukkit.getScheduler().runTask( teamBattlePlugin, () -> {
                if ( player.isOnline() ) {
                    game.setSpectator( player );
                }
            } );
        }
    }

    @EventHandler
    public void onPlayerQuit( PlayerQuitEvent event ) {
        Player player = event.getPlayer();
        event.setQuitMessage( null );
        if ( game.getGameState() == GameState.PREGAME ) {
            TeamBattleTeam team = game.getTeamHandler().getTeam( player );
            if ( team != null ) {
                game.getTeamHandler().quitTeam( team, player );
            }
        } else if ( game.getGameState().isWinAble() && game.getTeamHandler().getTeam( player ) != null ) {
            if ( player.getKiller() != null ) {
                player.setHealth( 0 );
            } else {
                game.getReconnectHandler().handleAccidentalDisconnect( player );
                game.sendGameMessage( "§7Der Spieler §e" + player.getName() + " §7hat das Spiel unerwartet verlassen." );
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem( PlayerDropItemEvent event ) {
        Player player = event.getPlayer();
        if ( player.getGameMode() != GameMode.CREATIVE && !game.getGameState().isInteractionAllowed() ) {
            event.setCancelled( true );
        }
    }

    @EventHandler
    public void onPlayerItemPickup( PlayerPickupItemEvent event ) {
        Player player = event.getPlayer();
        if ( player.getGameMode() != GameMode.CREATIVE && !game.getGameState().isInteractionAllowed() ) {
            event.setCancelled( true );
        }
    }

    @EventHandler
    public void onItemCraft( CraftItemEvent event ) {
        if ( event.getWhoClicked() instanceof Player && event.getRecipe().getResult().getType() == Material.GOLDEN_APPLE &&
                event.getRecipe().getResult().getDurability() == (short) 1 ) {
            event.setCancelled( true );
            game.sendGameMessage( ( (Player) event.getWhoClicked() ), "§cDu darfst keine OP-Goldäpfel craften!" );
        }
    }

    @EventHandler
    public void onPlayerInteract( PlayerInteractEvent event ) {
        Player player = event.getPlayer();
        if ( player.getGameMode() != GameMode.CREATIVE && !game.getGameState().isInteractionAllowed() ) {
            event.setCancelled( true );
            ItemStack itemStack = event.getItem();
            Action action = event.getAction();
            if ( itemStack != null && ( action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR ) ) {
                if ( event.getItem().isSimilar( ItemUtils.TEAM_SELECT ) ) {
                    new TeamSelectInventoryHandler( teamBattlePlugin, player, null ).openWithSoundEffect( Sound.NOTE_PLING );
                } else if ( event.getItem().isSimilar( ItemUtils.RULES ) ) {
                    new RulesInventoryHandler( teamBattlePlugin, player, null ).openWithSoundEffect( Sound.NOTE_PLING );
                } else if ( event.getItem().isSimilar( ItemUtils.OPERATOR ) ) {
                    new OperatorInventoryHandler( teamBattlePlugin, player, null ).openWithSoundEffect( Sound.NOTE_PLING );
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity( PlayerInteractEntityEvent event ) {
        Player player = event.getPlayer();
        if ( player.getGameMode() != GameMode.CREATIVE && !game.getGameState().isInteractionAllowed() ) {
            event.setCancelled( true );
        }
    }

    @EventHandler( priority = EventPriority.LOW )
    public void onInventoryClick( InventoryClickEvent event ) {
        if ( event.getWhoClicked() instanceof Player ) {
            Player player = ( (Player) event.getWhoClicked() );
            if ( player.getGameMode() != GameMode.CREATIVE && !game.getGameState().isInteractionAllowed() ) {
                event.setCancelled( true );
            }
        }
    }

    @EventHandler
    public void onPlayerBreakBlock( BlockBreakEvent event ) {
        Player player = event.getPlayer();
        if ( player.getGameMode() != GameMode.CREATIVE && !game.getGameState().isInteractionAllowed() ) {
            event.setCancelled( true );
        }
    }

    @EventHandler
    public void onPlayerPlaceBlock( BlockPlaceEvent event ) {
        Player player = event.getPlayer();
        if ( player.getGameMode() != GameMode.CREATIVE && !game.getGameState().isInteractionAllowed() ) {
            event.setCancelled( true );
        }
    }

    @EventHandler
    public void onEntityDamageByEntity( EntityDamageByEntityEvent event ) {
        if ( event.getDamager() instanceof Player ) {
            Player damager = (Player) event.getDamager();
            if ( !game.getGameState().isInteractionAllowed() ) {
                event.setCancelled( true );
            } else if ( event.getEntity() instanceof Player ) {
                Player damaged = (Player) event.getEntity();
                if ( game.getGameState().isPeaceful() || game.getTeamHandler().areAllied( damaged, damager ) ) {
                    event.setCancelled( true );
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage( EntityDamageEvent event ) {
        if ( event.getEntity() instanceof Player && game.getGameState().isPeaceful() ) {
            event.setCancelled( true );
        }
    }

    @EventHandler
    public void onPlayerRespawn( PlayerRespawnEvent event ) {
        event.setRespawnLocation( game.getGameConfig().getSpawnLocation() );
    }

    @EventHandler
    public void onFoodLevelChange( FoodLevelChangeEvent event ) {
        if ( game.getGameState().isPeaceful() ) {
            event.setCancelled( true );
        }
    }

    @EventHandler
    public void onWeatherChange( WeatherChangeEvent event ) {
        if ( game.getGameState() == GameState.PREGAME ) {
            event.setCancelled( true );
        }
    }

    @EventHandler
    public void onGameStateChange( GameStateChangeEvent event ) {
        GameState gameState = event.getNewState();
        if ( gameState == GameState.PROTECTION ) {
            World world = game.getGameConfig().getSpawnLocation().getWorld();
            world.setGameRuleValue( "doDaylightCycle", "true" );
            game.resetPlayers();
            game.startCountdown();
        } else if ( gameState == GameState.INGAME ) {
            game.getBorderHandler().startShrinking();
            game.sendGameMessage( "§cDie Kriegsphase hat begonnen!" );
            game.playGameSound( Sound.ENDERDRAGON_GROWL, 1, 1 );
        } else if ( gameState == GameState.END ) {
            game.getBorderHandler().stopShrinking();
        }
    }

    @EventHandler
    public void onPlayerJoinTeam( PlayerJoinTeamEvent event ) {
        if ( game.getGameState() == GameState.PREGAME ) {
            game.getTeamHandler().updateInventories();
        }
    }

    @EventHandler
    public void onPlayerQuitTeam( PlayerQuitTeamEvent event ) {
        if ( game.getGameState() == GameState.PREGAME && !event.isSwitching() ) {
            game.getTeamHandler().updateInventories();
        } else if ( game.getGameState().isWinAble() ) {
            TeamBattleTeam winnerTeam = game.getTeamHandler().getRemainingTeam();
            if ( winnerTeam != null ) {
                game.win( winnerTeam );
            }
        }
    }
}
