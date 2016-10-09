package tk.martijn_heil.elytraoptions.tasks;


import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ElytraFlightBooster implements Runnable
{
    @Override
    public void run()
    {
        for (EPlayer ep : ElytraOptions.getEPlayers())
        {
            Player p = ep.getPlayer();

            if(p.isGliding() && ep.isUsingBoost())
            {
                p.getLocation().getWorld().spigot().playEffect(p.getLocation(), Effect.FLAME, 0, 0, 0.2F, 0.2F, 0.2F, 0.0F, 1, 30);

                Vector pv = p.getLocation().getDirection();
                Vector v = pv.multiply(1.5F);
                p.setVelocity(v);

                if(ep.isUsingSound()) p.getWorld().playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);

                p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 16, (double) 0.1F, (double) 0.1F, (double) 0.1F, (double) 0.15F);
            }
        }
    }
}
