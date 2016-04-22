package tk.martijn_heil.elytraoptions.command;

import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.martijn_heil.nincore.api.NinCore;
import tk.martijn_heil.nincore.api.command.executors.NinSubCommandExecutor;
import tk.martijn_heil.nincore.api.entity.NinCommandSender;
import tk.martijn_heil.nincore.api.exceptions.TechnicalException;
import tk.martijn_heil.nincore.api.exceptions.ValidationException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.InvalidCommandSenderException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.PlayerNotFoundException;
import tk.martijn_heil.nincore.api.messaging.MessageColor;


public class StatusSubCmd extends NinSubCommandExecutor
{

    @Override
    public void execute(CommandSender commandSender, String[] args) throws ValidationException, TechnicalException
    {
        if(args != null && args.length == 1) // target specified
        {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) throw new PlayerNotFoundException(commandSender);

            EPlayer ep = ElytraOptions.getEPlayer(target);
            if(ep == null) throw new PlayerNotFoundException(commandSender);

            sendStatus(commandSender, ep);
        }
        else
        {
            if(!(commandSender instanceof Player)) throw new InvalidCommandSenderException(commandSender);

            EPlayer ep = ElytraOptions.getEPlayer((Player) commandSender);
            if(ep == null) throw new PlayerNotFoundException(commandSender);

            sendStatus(commandSender, ep);
        }
    }


    private static void sendStatus(CommandSender to, EPlayer about)
    {
        NinCommandSender nTo = NinCore.get().getEntityManager().getNinCommandSender(to);
        double remainingAirtime = ElytraOptions.getInstance().getConfig().getInt("airtime.max") - about.getAirTime();

        nTo.sendMessage(MessageColor.INFO, "Status information for player " + about.getPlayer().getName() + ":", ElytraOptions.getInstance());
        nTo.sendMessage(MessageColor.INFO, String.format("Using infinite: %s", about.isUsingInfinite()), ElytraOptions.getInstance());
        nTo.sendMessage(MessageColor.INFO, String.format("Using boost: %s", about.isUsingBoost()), ElytraOptions.getInstance());
        nTo.sendMessage(MessageColor.INFO, String.format("Using sound: %s", about.isUsingSound()), ElytraOptions.getInstance());
        nTo.sendMessage(MessageColor.INFO, String.format("Using trail: %s", about.isUsingTrail()), ElytraOptions.getInstance());
        nTo.sendMessage(MessageColor.INFO, String.format("Airtime remaining: %s", ((Double) remainingAirtime).toString()), ElytraOptions.getInstance());
        nTo.sendMessage(MessageColor.INFO, String.format("Current airtime: %s", ((Integer) about.getAirTime()).toString()), ElytraOptions.getInstance());
    }
}
