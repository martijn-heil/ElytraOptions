package tk.martijn_heil.elytraoptions.command;


import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.martijn_heil.nincore.api.command.executors.NinSubCommandExecutor;
import tk.martijn_heil.nincore.api.exceptions.TechnicalException;
import tk.martijn_heil.nincore.api.exceptions.ValidationException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.AccessDeniedException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.InvalidCommandSenderException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.PlayerNotFoundException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.TooManyArgumentsException;
import tk.martijn_heil.nincore.api.messaging.MessageColor;

public class ToggleInfiniteSubCmd extends NinSubCommandExecutor
{

    @Override
    public void execute(CommandSender commandSender, String[] args) throws ValidationException, TechnicalException
    {
        if(args != null && args.length > 2) throw new TooManyArgumentsException(commandSender);


        if(args != null && args.length == 2) // target player is specified & value is specified.
        {
            if(!commandSender.hasPermission("elytraoptions.flight.infinite.others")) throw new AccessDeniedException(commandSender);

            Player target = Bukkit.getPlayer(args[1]);
            if(target == null || !target.isOnline()) throw new PlayerNotFoundException(commandSender);

            EPlayer ep = ElytraOptions.getEPlayer(target);
            if(ep == null) throw new PlayerNotFoundException(commandSender);

            // toggle it
            ep.setUsingInfinite(!ep.isUsingInfinite());

            ep.toNinOnlinePlayer().sendMessage(MessageColor.INFO, String.format("%s turned your infinite flight %s.", commandSender.getName(), BooleanUtils.toStringOnOff(ep.isUsingInfinite())), ElytraOptions.getInstance());
        }
        else if (args != null && args.length == 1 && !(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off"))) // target player is specified, value is not.
        {
            if(!commandSender.hasPermission("elytraoptions.flight.infinite.others")) throw new AccessDeniedException(commandSender);

            Player target = Bukkit.getPlayer(args[0]);
            if(target == null || !target.isOnline()) throw new PlayerNotFoundException(commandSender);

            EPlayer ep = ElytraOptions.getEPlayer(target);
            if(ep == null) throw new PlayerNotFoundException(commandSender);



            boolean newValue = (args[0].equalsIgnoreCase("on"));
            ep.setUsingInfinite(newValue);

            ep.toNinOnlinePlayer().sendMessage(MessageColor.INFO, String.format("Infinite flight is now turned %s", BooleanUtils.toStringOnOff(newValue)), ElytraOptions.getInstance());
        }
        else if (args != null && args.length == 1 && (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off"))) // target player is not specified, value is.
        {
            if(!(commandSender instanceof Player)) throw new InvalidCommandSenderException(commandSender);
            if(!commandSender.hasPermission("elytraoptions.flight.infinite")) throw new AccessDeniedException(commandSender);

            EPlayer ep = ElytraOptions.getEPlayer((Player) commandSender);
            if(ep == null) throw new PlayerNotFoundException(commandSender);

            boolean newValue = (args[0].equalsIgnoreCase("on"));
            ep.setUsingInfinite(newValue);

            ep.toNinOnlinePlayer().sendMessage(MessageColor.INFO, String.format("Infinite flight is now turned %s", BooleanUtils.toStringOnOff(newValue)), ElytraOptions.getInstance());
        }
        else if(args == null || args.length <= 0) // no arguments specified.
        {
            if(!(commandSender instanceof Player)) throw new InvalidCommandSenderException(commandSender); // sender must be a player.
            if(!commandSender.hasPermission("elytraoptions.flight.infinite")) throw new AccessDeniedException(commandSender);

            EPlayer ep = ElytraOptions.getEPlayer((Player) commandSender);
            if(ep == null) throw new PlayerNotFoundException(commandSender);

            // toggle it.
            ep.setUsingInfinite(!ep.isUsingInfinite());

            ep.toNinOnlinePlayer().sendMessage(MessageColor.INFO, String.format("Infinite flight is now turned %s", BooleanUtils.toStringOnOff(ep.isUsingInfinite())), ElytraOptions.getInstance());
        }
    }
}
