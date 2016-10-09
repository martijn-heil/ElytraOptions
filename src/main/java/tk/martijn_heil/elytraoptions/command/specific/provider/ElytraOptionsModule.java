package tk.martijn_heil.elytraoptions.command.specific.provider;


import com.sk89q.intake.parametric.AbstractModule;
import org.bukkit.Server;
import tk.martijn_heil.elytraoptions.EPlayer;
import tk.martijn_heil.elytraoptions.command.common.TargetOrSender;
import tk.martijn_heil.elytraoptions.command.common.bukkit.provider.PlayerProvider;

public class ElytraOptionsModule extends AbstractModule
{
    private final Server server;


    public ElytraOptionsModule(Server server)
    {
        this.server = server;
    }


    @Override
    protected void configure()
    {
        bind(EPlayer.class).toProvider(new EPlayerProvider(server, new PlayerProvider(server, false)));

        bind(EPlayer.class).annotatedWith(TargetOrSender.class).toProvider(new EPlayerProvider(server, new PlayerProvider(server, true)));
    }
}
