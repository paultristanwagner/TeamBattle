package de.teamcreate.teambattle.event;

import de.teamcreate.teambattle.game.GameState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
@AllArgsConstructor
public class GameStateChangeEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    @Getter
    private GameState newState;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
