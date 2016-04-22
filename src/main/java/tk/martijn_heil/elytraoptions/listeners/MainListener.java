package tk.martijn_heil.elytraoptions.listeners;


import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;
import tk.martijn_heil.elytraoptions.tasks.CurrentAirTimeIncreaser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class MainListener implements Listener
{
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        EPlayer ep = ElytraOptions.getEPlayer(p);
        if(ep == null || p == null) return;

        if (p.isGliding() && ep.isUsingInfinite())
        {
            Vector unitVector = new Vector(0.0D, p.getLocation().getDirection().getY(), 0.0D);
            p.setVelocity(p.getVelocity().add(unitVector.multiply(0.05D)));
        }
    }


    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            // If elytra flying is disabled, cancel the event.
            if(ElytraOptions.getInstance().getConfig().getBoolean("disableElytraFlying") && e.isGliding())
            {
                e.setCancelled(true);
                return;
            }

            Player p = (Player) e.getEntity();

            if (e.isGliding() && !p.isGliding() && ElytraOptions.getInstance().getConfig().getBoolean("airtime.enabled") &&
                    !p.hasPermission("elytraoptions.cooldown.bypass"))
            {
                Bukkit.getScheduler().scheduleAsyncDelayedTask(ElytraOptions.getInstance(),
                        new CurrentAirTimeIncreaser(p, ElytraOptions.getInstance().getConfig().getInt("airtime.max")));
            }
        }
    }
}
