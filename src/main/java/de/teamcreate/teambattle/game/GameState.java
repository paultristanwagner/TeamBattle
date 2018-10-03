package de.teamcreate.teambattle.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
@AllArgsConstructor
@Getter
public enum GameState {

    END( null, true, false, false, true ),
    INGAME( END, false, true, true, true ),
    PROTECTION( INGAME, true, true, true, true ),
    PREGAME( PROTECTION, true, false, false, false );

    private GameState nextState;
    private boolean peaceful;
    private boolean winable;
    private boolean spectatorChatEnabled;
    private boolean interactionAllowed;
}
