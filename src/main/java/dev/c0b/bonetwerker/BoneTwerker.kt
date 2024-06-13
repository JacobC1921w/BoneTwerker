package dev.c0b.bonetwerker

import dev.c0b.bonetwerker.commands.SetChance
import dev.c0b.bonetwerker.commands.SetRadius
import dev.c0b.bonetwerker.commands.TBT
import dev.c0b.bonetwerker.listeners.onPlayerSneakEvent
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class BoneTwerker : JavaPlugin() {
    override fun onEnable() {
        // If config files don't exist (i.e. on first run), create them
        saveDefaultConfig()

        // Command executers
        getCommand("setchance")?.setExecutor(SetChance(this))
        getCommand("setradius")?.setExecutor(SetRadius(this))
        getCommand("tbt")?.setExecutor(TBT(this))

        // Event listeners
        Bukkit.getPluginManager().registerEvents(onPlayerSneakEvent(this), this)
    }
}
