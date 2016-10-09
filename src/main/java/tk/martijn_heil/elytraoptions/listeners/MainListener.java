package tk.martijn_heil.elytraoptions.listeners;


import com.sk89q.intake.CommandException;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.dispatcher.Dispatcher;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.util.Vector;
import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;
import tk.martijn_heil.elytraoptions.tasks.CurrentAirTimeIncreaser;

public class MainListener implements Listener
{
    private static Dispatcher dispatcher = ElytraOptions.getInstance().getDispatcher();


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        EPlayer ep = ElytraOptions.getEPlayer(p);
        if (ep == null || p == null) return;

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
            if (ElytraOptions.getInstance().getConfig().getBoolean("disableElytraFlying") && e.isGliding() &&
                    !p.hasPermission("elytraoptions.bypass.block"))
            {
                sendError(p, "You are not allowed to fly using the elytra.");
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


//    @EventHandler(ignoreCancelled = true)
//    public void onCommandPreProcess(PlayerCommandPreprocessEvent e)
//    {
//        executeCommand(e.getPlayer(), e.getMessage());
//    }
//
//
//    @EventHandler(ignoreCancelled = true)
//    public void onCommandPreProcess(ServerCommandEvent e)
//    {
//        executeCommand(e.getSender(), e.getCommand());
//    }


    @EventHandler(ignoreCancelled = true)
    public void onTabComplete(TabCompleteEvent e)
    {
        try
        {
            e.setCompletions(dispatcher.getSuggestions(e.getBuffer(), new Namespace()));
        }
        catch (CommandException cx)
        {
            sendError(e.getSender(), cx.getMessage());
        }
    }


    private static void sendError(CommandSender p, String message)
    {
        p.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + message);
    }


//    private static void executeCommand(CommandSender sender, String cmd)
//    {
//        String command = cmd.startsWith("/") ? cmd.substring(1) : cmd;
//
//        for (String alias : dispatcher.getAliases())
//        {
//            if (command.startsWith(alias))
//            {
//                Namespace namespace = new Namespace();
//                namespace.put("sender", sender);
//                namespace.put("senderClass", sender.getClass());
//                namespace.put(Permissible.class, sender);
//
//                try
//                {
//                    dispatcher.call(command, namespace, Collections.emptyList());
//                }
//                catch (CommandException | AuthorizationException cx)
//                {
//                    sendError(sender, cx.getMessage());
//                }
//                catch (InvocationCommandException icx)
//                {
//                    icx.printStackTrace();
//                }
//                break;
//            }
//        }
//    }
}
