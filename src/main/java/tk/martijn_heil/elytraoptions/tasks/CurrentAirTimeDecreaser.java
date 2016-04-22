package tk.martijn_heil.elytraoptions.tasks;


import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;
import tk.martijn_heil.elytraoptions.KillableRunnable;

public class CurrentAirTimeDecreaser implements KillableRunnable
{
    private volatile boolean isRunning = true;
    private long interval;

    @Override
    public void run()
    {
        while (isRunning)
        {
            for (EPlayer ep : ElytraOptions.getEPlayers())
            {
                if((!ep.getPlayer().isGliding()) && (ep.getAirTime() > 0))
                {
                    ep.setAirTime(ep.getAirTime() - 1);
                }
            }


            try
            {
                Thread.sleep(interval);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }


    public void kill()
    {
        isRunning = false;
    }


    public CurrentAirTimeDecreaser(long interval)
    {
        ElytraOptions.addTask(this);
        this.interval = interval;
    }
}
