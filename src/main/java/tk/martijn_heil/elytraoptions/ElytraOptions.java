package tk.martijn_heil.elytraoptions;

import com.sk89q.intake.Intake;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.fluent.CommandGraph;
import com.sk89q.intake.parametric.Injector;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.parametric.provider.PrimitivesModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.martijn_heil.elytraoptions.command.ElytraOptionsCommands;
import tk.martijn_heil.elytraoptions.command.common.CommonCommands;
import tk.martijn_heil.elytraoptions.command.common.bukkit.BukkitAuthorizer;
import tk.martijn_heil.elytraoptions.command.common.bukkit.RootCommand;
import tk.martijn_heil.elytraoptions.command.common.bukkit.provider.BukkitModule;
import tk.martijn_heil.elytraoptions.command.common.bukkit.provider.sender.BukkitSenderModule;
import tk.martijn_heil.elytraoptions.command.specific.provider.ElytraOptionsModule;
import tk.martijn_heil.elytraoptions.listeners.MainListener;
import tk.martijn_heil.elytraoptions.listeners.PlayerListener;
import tk.martijn_heil.elytraoptions.tasks.CurrentAirTimeDecreaser;
import tk.martijn_heil.elytraoptions.tasks.ElytraFlightBooster;
import tk.martijn_heil.elytraoptions.tasks.ElytraFlightTrailSpawner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElytraOptions extends JavaPlugin
{
    private static ElytraOptions instance;
    private static List<EPlayer> ePlayers = new ArrayList<>();
    private static Dispatcher dispatcher;


    @Override
    public void onEnable()
    {
        instance = this;

        this.getLogger().fine("Saving default config..");
        this.saveDefaultConfig();

        this.getLogger().info("Adding all online players to the player cache..");
        // Add all online players to the ePlayers list.
        for (Player p : Bukkit.getServer().getOnlinePlayers())
        {
            ePlayers.add(new EPlayer(p));
            this.getLogger().fine("Added a player to the player cache; " + p.toString());
        }

        if (this.getConfig().getBoolean("airtime.enabled"))
        {
            this.getLogger().info("Scheduling CurrentAirTimeDecreaser..");
            new CurrentAirTimeDecreaser(this.getConfig().getLong("airtime.regenrate"));
        }


        if (this.getConfig().getBoolean("crafting_recipe.enabled"))
        {
            this.getLogger().info("Creating elytra crafting recipe..");

            List<String> pattern = this.getConfig().getStringList("crafting_recipe.pattern");
            ConfigurationSection ingredients = this.getConfig().getConfigurationSection("crafting_recipe.ingredients");

            ItemStack i = new ItemStack(Material.ELYTRA);
            ShapedRecipe recipe = new ShapedRecipe(i);

            recipe.shape(pattern.get(0), pattern.get(1), pattern.get(2));


            for (String entry : ingredients.getKeys(false))
            {
                recipe.setIngredient(Character.toUpperCase(entry.charAt(0)),
                        Material.valueOf(this.getConfig().getString("crafting_recipe.ingredients." + entry)));
            }

            Bukkit.addRecipe(recipe);
        }


        Injector injector = Intake.createInjector();
        injector.install(new PrimitivesModule());
        injector.install(new BukkitModule(Bukkit.getServer()));
        injector.install(new ElytraOptionsModule(Bukkit.getServer()));
        injector.install(new BukkitSenderModule());

        ParametricBuilder builder = new ParametricBuilder(injector);
        builder.setAuthorizer(new BukkitAuthorizer());

        /*
            This is a bit hacky, CommonCommands depends on the dispatcher it belongs to, but when we *have* to register
            the CommonCommands, the dispatcher is not initialized yet. So we have to do late-initialization as you can see below
         */
        CommonCommands cm = new CommonCommands();

        dispatcher = new CommandGraph()
                .builder(builder)
                .commands()
                .group("elytraoptions", "eo")
                .registerMethods(new ElytraOptionsCommands())
                .registerMethods(cm)
                .parent()
                .graph()
                .getDispatcher();

        cm.lateInit(dispatcher); // Important, don't forget this!


        try
        {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(this.getName().toLowerCase(), RootCommand.of(dispatcher));
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }


        this.getLogger().info("Registering event listeners..");
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new MainListener(), this);

        this.getLogger().info("Scheduling tasks..");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ElytraFlightTrailSpawner(), 0L, 1L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ElytraFlightBooster(), 0L, 50L);
    }


    @Nullable
    public static EPlayer getEPlayer(Player p)
    {
        for (EPlayer ep : ePlayers)
        {
            if(ep.getPlayer().equals(p)) return ep;
        }

        return null;
    }


    @NotNull
    public static List<EPlayer> getEPlayers()
    {
        return ePlayers;
    }


    public static void removeEPlayer(Player p)
    {
        Iterator<EPlayer> iter = ePlayers.iterator();

        while(iter.hasNext())
        {
            EPlayer ep = iter.next();

            if (ep.getPlayer().equals(p))
            {
                iter.remove();
            }
        }
    }


    public static void addEPlayer(EPlayer p)
    {
        ePlayers.add(p);
    }


    public static ElytraOptions getInstance()
    {
        return instance;
    }

    public Dispatcher getDispatcher()
    {
        return dispatcher;
    }
}