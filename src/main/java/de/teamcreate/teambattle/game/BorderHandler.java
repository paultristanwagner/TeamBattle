package de.teamcreate.teambattle.game;

import org.bukkit.WorldBorder;

/**
 * JavaDoc this file!
 * Created: 23.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class BorderHandler {

    private GameConfig gameConfig;
    private WorldBorder worldBorder;

    BorderHandler( GameConfig gameConfig ) {
        this.gameConfig = gameConfig;
        worldBorder = gameConfig.getSpawnLocation().getWorld().getWorldBorder();
        worldBorder.setCenter( gameConfig.getSpawnLocation() );
        worldBorder.setSize( gameConfig.getArenaSize() );
        worldBorder.setDamageAmount( 0.2 );
        worldBorder.setWarningDistance( 20 );
    }

    public void startShrinking() {
        worldBorder.setSize( gameConfig.getFinalSize(), gameConfig.getFightingMinutes() * 60L );
    }

    public void stopShrinking() {
        worldBorder.setSize( worldBorder.getSize() );
    }
}
