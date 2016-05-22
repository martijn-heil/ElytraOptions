package tk.martijn_heil.elytraoptions.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;
import tk.martijn_heil.elytraoptions.tasks.CurrentAirTimeIncreaser;
import tk.martijn_heil.nincore.api.entity.NinOnlinePlayer;

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
            Player p = (Player) e.getEntity();

            // If elytra flying is disabled, cancel the event.
            if(ElytraOptions.getInstance().getConfig().getBoolean("disableElytraFlying") && e.isGliding() &&
                    !p.hasPermission("elytraoptions.bypass.block"))
            {
                NinOnlinePlayer.fromPlayer(p).sendError("You are not allowed to fly using the elytra.",
                        ElytraOptions.getInstance());
                e.setCancelled(true);
                return;
            }

            // If the player starts gliding and airtime is enabled in config.
            if (e.isGliding() && !p.isGliding() && ElytraOptions.getInstance().getConfig().getBoolean("airtime.enabled") &&
                    !p.hasPermission("elytraoptions.cooldown.bypass"))
            {
                CurrentAirTimeIncreaser.schedule(p, ElytraOptions.getInstance().getConfig().getInt("airtime.max"));
            }
        }
    }
}
