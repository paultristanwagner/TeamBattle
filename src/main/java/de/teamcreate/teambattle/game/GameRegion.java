package de.teamcreate.teambattle.game;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

/**
 * JavaDoc this file!
 * Created: 23.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
@AllArgsConstructor
public class GameRegion {

    private static final Random random = new Random();
    private JavaPlugin javaPlugin;
    private Location location0;
    private Location location1;

    public void dissolve() {
        List<Block> blocks = Lists.newArrayList();
        int minX = Math.min( location0.getBlockX(), location1.getBlockX() );
        int minY = Math.min( location0.getBlockY(), location1.getBlockY() );
        int minZ = Math.min( location0.getBlockZ(), location1.getBlockZ() );
        int maxX = Math.max( location0.getBlockX(), location1.getBlockX() );
        int maxY = Math.max( location0.getBlockY(), location1.getBlockY() );
        int maxZ = Math.max( location0.getBlockZ(), location1.getBlockZ() );
        for ( int x = minX; x <= maxX; x++ ) {
            for ( int y = minY; y <= maxY; y++ ) {
                for ( int z = minZ; z <= maxZ; z++ ) {
                    Block block = location0.getWorld().getBlockAt( x, y, z );
                    if ( block.getType() != Material.AIR ) {
                        blocks.add( block );
                    }
                }
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if ( blocks.isEmpty() ) {
                    cancel();
                    return;
                }
                int index = random.nextInt( blocks.size() );
                Block block = blocks.get( index );
                blocks.remove( index );
                block.setType( Material.AIR, false );
                if ( random.nextDouble() < 0.1 ) {
                    location0.getWorld().playEffect( block.getLocation(), Effect.EXPLOSION_HUGE, null );
                }
            }
        }.runTaskTimer( javaPlugin, 0L, 1L );
    }
}
