package dev.c0b.bonetwerker.commands

import dev.c0b.bonetwerker.BoneTwerker
import org.bukkit.Bukkit.getLogger
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.math.absoluteValue

class SetRadius(private val mainInstance: BoneTwerker): CommandExecutor {

    override fun onCommand(user: CommandSender, cmd: Command, p2: String, args: Array<out String>?): Boolean {

        // Check if sender has correct permissions
        if (user.hasPermission("BoneTwerker.setradius")) {

            // Check if the supplied command arguments are not null or empty, signifying a set operation
            if(args != null && args.isNotEmpty()) {
                val radius: Double
                // Try to convert the supplied value to a double, and fail if not
                try {
                    radius = args[0].toDouble().absoluteValue
                } catch (ex: NumberFormatException) {
                    if (user is Player) {
                        user.sendMessage("§f[ §cBoneTwerker §f] " + args[0] + " is not a valid double!")
                    } else {
                        getLogger().info("[ BoneTwerker ] " + args[0] + " is not a valid double!")
                    }
                    return true
                }

                // Warn players if radius is too high
                if (radius >= 10.0) {
                    if (user is Player) {
                        user.sendMessage("§f[ §eBoneTwerker §f] A radius of §e$radius§f is quite high, its your funeral!")
                    } else {
                        getLogger().info("[ BoneTwerker ] A radius of $radius is quite high, its your funeral!")
                    }
                }

                // Save new radius
                mainInstance.config.set("radius", radius)
                mainInstance.saveConfig()

                if (user is Player) {
                    user.sendMessage("§f[ §aBoneTwerker §f] Radius set to §e$radius")
                } else {
                    getLogger().info("[ BoneTwerker ] Radius set to $radius")
                }

            } else {
                // Display current radius values
                if (user is Player) {
                    user.sendMessage("§f[ §aBoneTwerker §f] Radius set to §e" + mainInstance.config.get("radius"))
                } else {
                    getLogger().info("[ BoneTwerker ] Radius set to " + mainInstance.config.get("radius"))
                }

            }

        } else {
            user.sendMessage("§f[ §cBoneTwerker §f] You don't have permission to execute this command!")
        }

        return true
    }
}