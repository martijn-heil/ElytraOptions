package tk.martijn_heil.elytraoptions.tasks;


import org.bukkit.scheduler.BukkitRunnable;
import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;

public class CurrentAirTimeDecreaser extends BukkitRunnable
{
    @Override
    public void run()
    {
        // Don't change to lambda; looks terribly messy.
        for (EPlayer ep : ElytraOptions.getEPlayers())
        {
            if ((!ep.getPlayer().isGliding()) && (ep.getAirTime() > 0))
            {
                ep.setAirTime(ep.getAirTime() - 1);
            }
        }
    }

    public CurrentAirTimeDecreaser(long interval)
    {
        this.runTaskTimer(ElytraOptions.getInstance(), 0L, interval);
    }
}
