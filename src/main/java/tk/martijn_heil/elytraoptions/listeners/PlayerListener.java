package tk.martijn_heil.elytraoptions.listeners;

import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        ElytraOptions.addEPlayer(new EPlayer(e.getPlayer()));
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        ElytraOptions.removeEPlayer(e.getPlayer());
    }
}
