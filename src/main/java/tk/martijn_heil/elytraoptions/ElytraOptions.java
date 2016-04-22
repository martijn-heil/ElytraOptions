package tk.martijn_heil.elytraoptions;

import tk.martijn_heil.elytraoptions.command.*;
import tk.martijn_heil.elytraoptions.listeners.MainListener;
import tk.martijn_heil.elytraoptions.listeners.PlayerListener;
import tk.martijn_heil.elytraoptions.tasks.CurrentAirTimeDecreaser;
import tk.martijn_heil.elytraoptions.tasks.ElytraFlightBooster;
import tk.martijn_heil.elytraoptions.tasks.ElytraFlightTrailSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.martijn_heil.nincore.api.Core;
import tk.martijn_heil.nincore.api.command.NinCommand;
import tk.martijn_heil.nincore.api.command.builders.CommandBuilder;
import tk.martijn_heil.nincore.api.command.builders.SubCommandBuilder;
import tk.martijn_heil.nincore.api.util.DebugUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElytraOptions extends Core
{
    private volatile static ElytraOptions instance;
    private volatile static List<EPlayer> ePlayers = new ArrayList<>();
    private volatile static List<KillableRunnable> tasks = new ArrayList<>();


    @Override
    public void onEnableInner()
    {
        instance = this;

        this.getNinLogger().fine("Saving default config..");
        this.saveDefaultConfig();

        this.getNinLogger().info("Adding all online players to the player cache..");
        // Add all online players to the ePlayers list.
        for (Player p : Bukkit.getServer().getOnlinePlayers())
        {
            ePlayers.add(new EPlayer(p));
            this.getNinLogger().fine("Added a player to the player cache; " + DebugUtils.playerToDebugString(p));
        }


        this.getNinLogger().info("Registering event listeners..");
        // Register events.
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new MainListener(), this);

        if (this.getConfig().getBoolean("airtime.enabled"))
        {
            this.getNinLogger().info("Scheduling CurrentAirTimeDecreaser..");
            Bukkit.getScheduler().scheduleAsyncDelayedTask(ElytraOptions.instance,
                    new CurrentAirTimeDecreaser(this.getConfig().getLong("airtime.regenrate")));
        }


        if (this.getConfig().getBoolean("crafting_recipe.enabled"))
        {
            this.getNinLogger().info("Creating elytra crafting recipe..");

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


        this.getNinLogger().info("Creating ElytraOptions command..");

        NinCommand elytraoptions = new CommandBuilder(this)
        .setName("elytraoptions")
        .setUseStaticDescription(true)
        .construct();

        elytraoptions.addDefaultHelpSubCmd();
        elytraoptions.addDefaultInfoSubCmd();


        this.getNinLogger().info("Creating subcommands..");

         new SubCommandBuilder()
                .setName("trail")
                .setRequiredPermission("elytraoptions.flight.trail")
                .setUsage("<on|off=toggle> <player=you>")
                .setUseStaticDescription(true)
                .setStaticDescription("Enable/disable trail")
                .setExecutor(new ToggleTrailSubCmd())
                .setParentCommand(elytraoptions)
                .construct();


        new SubCommandBuilder()
                .setName("infinite")
                .setRequiredPermission("elytraoptions.flight.infinite")
                .setUsage("<on|off=toggle> <player=you>")
                .setUseStaticDescription(true)
                .setStaticDescription("Toggle infinite elytra flight")
                .setExecutor(new ToggleInfiniteSubCmd())
                .setParentCommand(elytraoptions)
                .construct();


        new SubCommandBuilder()
                .setName("boost")
                .setRequiredPermission("elytraoptions.flight.boost")
                .setUsage("<on|off=toggle> <player=you>")
                .setUseStaticDescription(true)
                .setStaticDescription("Toggle elytra flight boosting")
                .setExecutor(new ToggleBoostSubCmd())
                .setParentCommand(elytraoptions)
                .construct();


        new SubCommandBuilder()
                .setName("sound")
                .setRequiredPermission("elytraoptions.flight.boost")
                .setUsage("<on|off=toggle> <player=you>")
                .setUseStaticDescription(true)
                .setStaticDescription("Toggle boost sound")
                .setExecutor(new ToggleSoundSubCmd())
                .setParentCommand(elytraoptions)
                .construct();


        new SubCommandBuilder()
                .setName("status")
                .setUsage("<player=you>")
                .setUseStaticDescription(true)
                .setStaticDescription("Show your player status")
                .setExecutor(new StatusSubCmd())
                .setParentCommand(elytraoptions)
                .construct();


        this.getNinLogger().info("Scheduling tasks..");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ElytraFlightTrailSpawner(), 0L, 1L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ElytraFlightBooster(), 0L, 100L);
    }


    @Override
    public void onDisableInner()
    {
        this.getLogger().info("Killing all running tasks..");

        for (KillableRunnable r : tasks)
        {
            r.kill();
            this.getNinLogger().fine("Killed a running task. ( " + r.getClass().getName() + " )");
        }
    }


    @Nullable
    public synchronized static EPlayer getEPlayer(Player p)
    {
        for (EPlayer ep : ePlayers)
        {
            if(ep.getPlayer().equals(p)) return ep;
        }

        return null;
    }


    @NotNull
    public synchronized static List<EPlayer> getEPlayers()
    {
        return ePlayers;
    }


    public synchronized static void removeEPlayer(Player p)
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


    public synchronized static void addEPlayer(EPlayer p)
    {
        ePlayers.add(p);
    }


    public synchronized static List<KillableRunnable> getTasks()
    {
        return tasks;
    }


    @NotNull
    public synchronized static void addTask(KillableRunnable r)
    {
        tasks.add(r);
    }


    public synchronized static void removeTask(KillableRunnable r)
    {
        for (KillableRunnable r2: tasks)
        {
            if(r2.equals(r))
            {
                tasks.remove(r2);
            }
        }
    }


    public synchronized static ElytraOptions getInstance()
    {
        return instance;
    }
}