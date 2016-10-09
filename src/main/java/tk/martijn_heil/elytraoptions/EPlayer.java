package tk.martijn_heil.elytraoptions;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class EPlayer
{

    @Getter         private Player player;
    @Getter @Setter private volatile int airTime = 0;
    @Getter @Setter private boolean usingTrail = false;
    @Getter @Setter private boolean usingBoost = false;
    @Getter @Setter private boolean usingInfinite = false;
    @Getter @Setter private boolean usingSound = true;



    public EPlayer(Player p)
    {
        this.player = p;
    }


    public static EPlayer fromPlayer(Player p)
    {
        return ElytraOptions.getEPlayer(p);
    }
}
