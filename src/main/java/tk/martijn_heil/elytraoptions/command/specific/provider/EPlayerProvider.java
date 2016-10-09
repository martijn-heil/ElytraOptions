package tk.martijn_heil.elytraoptions.command.specific.provider;


import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.ElytraOptions;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class EPlayerProvider implements Provider<EPlayer>
{
    private final Server server;
    private Provider<Player> playerProvider;


    public EPlayerProvider(Server server, Provider<Player> playerProvider)
    {
        this.server = server;
        this.playerProvider = playerProvider;
    }


    @Override
    public boolean isProvided()
    {
        return false;
    }


    @Nullable
    @Override
    public EPlayer get(CommandArgs commandArgs, List<? extends Annotation> list) throws ArgumentException, ProvisionException
    {
        Player player = playerProvider.get(commandArgs, list);
        EPlayer ep = ElytraOptions.getEPlayer(player);
        if(ep == null) throw new ArgumentParseException("Player not found.");
        return ep;
    }


    @Override
    public List<String> getSuggestions(String s)
    {
        List<String> eMatches = new ArrayList<>();
        for (Player p : server.matchPlayer(s))
        {
            EPlayer ep = ElytraOptions.getEPlayer(p);
            if(ep != null)
            {
                eMatches.add(ep.getPlayer().getName());
            }
        }

        return eMatches;
    }
}
