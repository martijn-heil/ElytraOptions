package tk.martijn_heil.elytraoptions.command;


import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import org.bukkit.command.CommandSender;
import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;
import tk.martijn_heil.elytraoptions.command.common.Sender;
import tk.martijn_heil.elytraoptions.command.common.TargetOrSender;

public class ElytraOptionsCommands
{
    @Command(aliases = "status", desc = "Show your player status", usage = "<value> <player=self>")
    @Require("elytraoptions.command.status")
    public void status(@Sender CommandSender sender, @TargetOrSender("elytraoptions.command.status.other")EPlayer about)
    {
        double remainingAirtime = ElytraOptions.getInstance().getConfig().getInt("airtime.max") - about.getAirTime();

        sender.sendMessage("Status information for player " + about.getPlayer().getName() + ":");
        sender.sendMessage( String.format("Using infinite: %s", about.isUsingInfinite()));
        sender.sendMessage(String.format("Using boost: %s", about.isUsingBoost()));
        sender.sendMessage(String.format("Using sound: %s", about.isUsingSound()));
        sender.sendMessage(String.format("Using trail: %s", about.isUsingTrail()));
        sender.sendMessage(String.format("Airtime remaining: %s", ((Double) remainingAirtime).toString()));
        sender.sendMessage(String.format("Current airtime: %s", ((Integer) about.getAirTime()).toString()));
    }

    @Command(aliases = "boost", desc = "Show your player status", usage = "<value> <player=self>")
    @Require("elytraoptions.command.boost")
    public void boost(boolean value, @TargetOrSender("elytraoptions.command.boost.other") EPlayer ep)
    {
        ep.setUsingBoost(value);
    }

    @Command(aliases = "infinite", desc = "Show your player status", usage = "<value> <player=self>")
    @Require("elytraoptions.command.infinite")
    public void infinite(boolean value, @TargetOrSender("elytraoptions.command.infinite.other") EPlayer ep)
    {
        ep.setUsingInfinite(value);
    }

    @Command(aliases = "trail", desc = "Show your player status", usage = "<value> <player=self>")
    @Require("elytraoptions.command.trail")
    public void trail(boolean value, @TargetOrSender("elytraoptions.command.trial.other") EPlayer ep)
    {
        ep.setUsingTrail(value);
    }

    @Command(aliases = "sound", desc = "Show your player status", usage = "<value> <player=self>")
    @Require("elytraoptions.command.sound")
    public void sound(boolean value, @TargetOrSender("elytraoptions.command.sound.other") EPlayer ep)
    {
        ep.setUsingSound(value);
    }
}
