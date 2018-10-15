package de.teamcreate.teambattle.util;

import de.teamcreate.itemcommons.ItemBuilder;
import de.teamcreate.teambattle.TeamBattlePlugin;
import de.teamcreate.teambattle.game.TeamBattleTeam;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * JavaDoc this file!
 * Created: 03.10.2018
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class ItemUtils {

    public static final ItemStack TEAM_SELECT = new ItemBuilder( Material.BED ).setName( "§aTeamauswahl" ).build();
    public static final ItemStack RULES = new ItemBuilder( Material.BOOK ).setName( "§5Regeln" ).build();
    public static final ItemStack OPERATOR = new ItemBuilder( Material.COMMAND ).setName( "§bAdministration" ).build();
    public static final ItemStack SPACER = new ItemBuilder( Material.STAINED_GLASS_PANE ).setName( " " ).setDurability( 3 ).build();
    public static final ItemStack PLUGIN = new ItemBuilder( Material.IRON_SWORD )
            .setName( "§eTeamBattle" ).addLore(
                    "",
                    "§bVersion§7: §e" + JavaPlugin.getPlugin( TeamBattlePlugin.class ).getDescription().getVersion(),
                    "§bAuthor: §5LetsPeee"
            ).addItemFlag( ItemFlag.HIDE_ATTRIBUTES ).build();
    public static final ItemStack START_GAME = new ItemBuilder( Material.REDSTONE_TORCH_ON ).setName( "§aSpiel starten" ).build();
    public static final ItemStack CONFIRM = new ItemBuilder( Material.STAINED_GLASS_PANE ).setName( "§aBestätigen" ).setDurability( 5 ).build();
    public static final ItemStack ABORT = new ItemBuilder( Material.STAINED_GLASS_PANE ).setName( "§cAbbrechen" ).setDurability( 14 ).build();
    public static final ItemStack FAIR_PLAY = new ItemBuilder( Material.GOLD_SWORD ).setName( "§cFaires Spiel" ).addItemFlag( ItemFlag.HIDE_ATTRIBUTES ).build();
    public static final ItemStack NO_NOTCH_APPLES = new ItemBuilder( Material.GOLDEN_APPLE ).setDurability( 1 ).setName( "§cKeine OP-Goldäpfel" ).build();
    public static final ItemStack NO_RIDING = new ItemBuilder( Material.SADDLE ).setName( "§cKeine Tiere reiten" ).build();
    public static final ItemStack NO_PORTALS = new ItemBuilder( Material.BEDROCK ).setName( "§cKeine Portale verwenden" ).build();
    public static final ItemStack NO_TOWERING = new ItemBuilder( Material.WOOD_AXE ).setName( "§cKein Towern" ).addItemFlag( ItemFlag.HIDE_ATTRIBUTES ).build();
    public static final ItemStack SURFACE = new ItemBuilder( Material.GRASS ).setName( "§cNach der Schutzzeit an die Oberfläche" ).build();

    private ItemUtils() {
        throw new IllegalStateException( "Utility class" );
    }

    public static ItemStack getTeamItemStack( Player player, TeamBattleTeam teamBattleTeam ) {
        ItemBuilder itemBuilder = new ItemBuilder( Material.WOOL );
        if ( teamBattleTeam.isFull() ) {
            itemBuilder.setDurability( 14 );
        } else if ( !teamBattleTeam.getMembers().isEmpty() ) {
            itemBuilder.setDurability( 8 );
        }
        itemBuilder.setName( "§e" + teamBattleTeam.getTeamName() ).addLore(
                "§3" + teamBattleTeam.getMembers().size() + "§7/§3" + TeamBattleTeam.MAXIMUM_MEMBERS,
                "§7--------------------------"
        );
        List<Player> members = teamBattleTeam.getMembers();
        for ( int i = 1; i <= TeamBattleTeam.MAXIMUM_MEMBERS; i++ ) {
            if ( i <= members.size() ) {
                Player member = members.get( i - 1 );
                if ( !member.equals( player ) ) {
                    itemBuilder.addLore( "§7" + i + ". §e" + member.getName() );
                } else {
                    itemBuilder.addLore( "§7" + i + ". §5" + member.getName() );
                }
            } else {
                itemBuilder.addLore( "§7" + i + ". -" );
            }
        }
        return itemBuilder.build();
    }
}
