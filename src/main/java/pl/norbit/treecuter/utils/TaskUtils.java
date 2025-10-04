package pl.norbit.treecuter.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import pl.norbit.treecuter.TreeCuter;
import pl.norbit.treecuter.config.SettingsExtra;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskUtils {
    private static final JavaPlugin inst = TreeCuter.getInstance();
    private static final BukkitScheduler scheduler = inst.getServer().getScheduler();
    private static final ScheduledThreadPoolExecutor asyncScheduler;
    static {
        AtomicInteger counter = new AtomicInteger(0);
        asyncScheduler = new ScheduledThreadPoolExecutor(SettingsExtra.GENERAL.ASYNC_THREADS, t -> {
            Thread thread = new Thread(t);
            thread.setName("TreeCuter Scheduler Thread - " + counter.getAndIncrement());
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setDaemon(false);
            return thread;
        });
        asyncScheduler.setMaximumPoolSize(SettingsExtra.GENERAL.ASYNC_THREADS);
    }

    private TaskUtils() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static void sync(Runnable runnable){
        scheduler.runTask(inst, runnable);
    }

    public static BukkitTask timer(Runnable runnable, long period){
        return scheduler.runTaskTimer(inst, runnable, 0L, period);
    }

    public static void async(Runnable runnable){
        asyncScheduler.execute(runnable);
    }

    public static void timerAsync(Runnable runnable, long period){
        asyncScheduler.scheduleAtFixedRate(runnable, 0L, period * 50, TimeUnit.MILLISECONDS);
    }
}
