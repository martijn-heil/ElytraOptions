package tk.martijn_heil.elytraoptions.tasks;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;

public class CurrentAirTimeIncreaser extends BukkitRunnable
{
    private Player target;
    private int maxCount;


    @Override
    public void run()
    {
        target.sendMessage(this.getColor(maxCount - EPlayer.fromPlayer(target).getAirTime()) +
                "You have started flying, you can fly for " +
                (maxCount - EPlayer.fromPlayer(target).getAirTime()) + " seconds.");

        if (!target.isGliding())
        {
            target.sendMessage(ChatColor.GREEN + "You have stopped flying");
            this.cancel();
        }


        switch (maxCount - EPlayer.fromPlayer(target).getAirTime())
        {
            case (600):
                this.sendCountDownMessage("10 minutes", this.getColor(600));
                break;
            case (300):
                this.sendCountDownMessage("5 minutes", this.getColor(300));
                break;
            case (240):
                this.sendCountDownMessage("4 minutes", this.getColor(240));
                break;
            case (180):
                this.sendCountDownMessage("3 minutes", this.getColor(180));
                break;
            case (120):
                this.sendCountDownMessage("2 minutes", this.getColor(120));
                break;
            case (60):
                this.sendCountDownMessage("1 minute", this.getColor(60));
                break;
            case (30):
                this.sendCountDownMessage("30 seconds", this.getColor(30));
                break;
            case (10):
                this.sendCountDownMessage("10 seconds", this.getColor(10));
                break;
            case (9):
                this.sendCountDownMessage("9 seconds", this.getColor(9));
                break;
            case (8):
                this.sendCountDownMessage("8 seconds", this.getColor(8));
                break;
            case (7):
                this.sendCountDownMessage("7 seconds", this.getColor(7));
                break;
            case (6):
                this.sendCountDownMessage("6 seconds", this.getColor(6));
                break;
            case (5):
                this.sendCountDownMessage("5 seconds", this.getColor(5));
                break;
            case (4):
                this.sendCountDownMessage("4 seconds", this.getColor(4));
                break;
            case (3):
                this.sendCountDownMessage("3 seconds", this.getColor(3));
                break;
            case (2):
                this.sendCountDownMessage("2 seconds", this.getColor(2));
                break;
            case (1):
                this.sendCountDownMessage("1 second", this.getColor(1));
                break;
        }


        if (!target.isGliding())
        {
            target.sendMessage(ChatColor.GREEN + "You have stopped flying");
            this.cancel();
        }


        if (maxCount - EPlayer.fromPlayer(target).getAirTime() <= 0)
        {
            target.setGliding(false);
            this.cancel();
        }


        EPlayer.fromPlayer(target).setAirTime(EPlayer.fromPlayer(target).getAirTime() + 1);
    }


    public CurrentAirTimeIncreaser(Player target, int maxCount)
    {
        this.target = target;
        this.maxCount = maxCount;
    }


    private void sendCountDownMessage(String msg, ChatColor color)
    {
        this.target.sendMessage(color + "You have " + msg + " of air time left.");
    }


    private ChatColor getColor(int count)
    {
        if(count > 60) return ChatColor.GREEN;

        else if (count > 10) return ChatColor.YELLOW;

        else if (count > 5) return ChatColor.RED;

        else if (count <= 5 ) return ChatColor.DARK_RED;

        return ChatColor.GREEN;
    }


    public static void schedule(Player target, int maxCount)
    {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ElytraOptions.getInstance(), () ->
        {
            // If the target is still gliding after 0.75 seconds, continue.
            // Prevents chat spam when flying really short, like when jumping 1.5 blocks high.
            if(target.isGliding())
            {
                new CurrentAirTimeIncreaser(target, maxCount).runTaskTimer(ElytraOptions.getInstance(), 0L, 20L);
            }
        }, 15L);
    }
}
