package de.teamcreate.teambattle.game;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

/**
 * JavaDoc this file!
 * Created: 23.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
class ReconnectProfile {

    private double maximumHealth;
    private double health;
    private int foodLevel;
    private int level;
    private float exp;
    private float exhaustion;
    private float saturation;
    private float fallDistance;
    private int fireTicks;
    private GameMode gameMode;
    private Location location;
    private ItemStack[] inventoryContents;
    private ItemStack[] armorContents;
    private Collection<PotionEffect> activePotionEffects;

    ReconnectProfile( Player player ) {
        maximumHealth = player.getMaxHealth();
        health = player.getHealth();
        foodLevel = player.getFoodLevel();
        level = player.getLevel();
        exp = player.getExp();
        exhaustion = player.getExhaustion();
        saturation = player.getSaturation();
        fallDistance = player.getFallDistance();
        fireTicks = player.getFireTicks();
        gameMode = player.getGameMode();
        location = player.getLocation();
        inventoryContents = player.getInventory().getContents();
        armorContents = player.getInventory().getArmorContents();
        activePotionEffects = player.getActivePotionEffects();
    }

    void use( Player player ) {
        player.setMaxHealth( maximumHealth );
        player.setHealth( health );
        player.setFoodLevel( foodLevel );
        player.setLevel( level );
        player.setExp( exp );
        player.setGameMode( gameMode );
        player.setExhaustion( exhaustion );
        player.setSaturation( saturation );
        player.setFallDistance( fallDistance );
        player.setFireTicks( fireTicks );
        player.teleport( location );
        player.getInventory().setContents( inventoryContents );
        player.getInventory().setArmorContents( armorContents );
        activePotionEffects.forEach( player::addPotionEffect );
    }
}
