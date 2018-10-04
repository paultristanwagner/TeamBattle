package de.teamcreate.teambattle.executor;

import de.teamcreate.teambattle.TeamBattlePlugin;
import de.teamcreate.teambattle.game.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class TeamBattleExecutor implements CommandExecutor {

    private TeamBattlePlugin teamBattlePlugin;
    private Game game;

    public TeamBattleExecutor( TeamBattlePlugin teamBattlePlugin ) {
        this.teamBattlePlugin = teamBattlePlugin;
        this.game = teamBattlePlugin.getGame();
    }

    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String label, String[] args ) {
        if ( !( commandSender instanceof Player ) ) {
            commandSender.sendMessage( "§cThis command can only be executed by players!" );
            return true;
        } else {
            Player player = ( (Player) commandSender );
            if ( args.length == 1 && args[0].equalsIgnoreCase( "start" ) ) {
                if ( player.hasPermission( "teambattle.operate" ) ) {
                    game.attemptStart( player );
                } else {
                    player.sendMessage( command.getPermissionMessage() );
                }
                return true;
            } else if ( args.length == 0 ) {
                game.sendGameMessage( player, "§ePlugin §ev" + teamBattlePlugin.getDescription().getVersion() + " §eby §5LetsPeee" );
                return true;
            }
        }
        return false;
    }
}
