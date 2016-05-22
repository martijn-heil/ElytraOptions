package tk.martijn_heil.elytraoptions.tasks;


import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;
import org.bukkit.Effect;

public class ElytraFlightTrailSpawner implements Runnable
{
    @Override
    public void run()
    {
        // Don't change to lambda; looks terribly messy.
        for (EPlayer ep : ElytraOptions.getEPlayers())
        {
            if(ep.getPlayer().isGliding() && ep.isUsingTrail())
            {
                //ep.getPlayer().getWorld().spawnParticle(Particle.CLOUD, ep.getPlayer().getLocation(), 1, (double) 0.1F, (double) 0.1F, (double) 0.1F, (double) 0.1F);
                ep.getPlayer().getLocation().getWorld().spigot().playEffect(ep.getPlayer().getLocation(), Effect.CLOUD, 0, 0, 0.5F, 0.5F, 0.5F, 0.0F, 30, 30);
            }
        }
    }
}
