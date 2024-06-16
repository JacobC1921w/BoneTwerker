package dev.c0b.bonetwerker.commands

import com.destroystokyo.paper.utils.PaperPluginLogger
import dev.c0b.bonetwerker.BoneTwerker
import kotlin.math.absoluteValue
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetChance(private val mainInstance: BoneTwerker) : CommandExecutor {

    override fun onCommand(
        user: CommandSender,
        cmd: Command,
        p2: String,
        args: Array<out String>?
    ): Boolean {
        // Make sure sender has permissions
        if (user.hasPermission("BoneTwerker.setchance")) {

            // Check if the supplied command arguments are not null or empty, signifying a set
            // operation
            if (args != null && args.isNotEmpty()) {
                val chance: Double

                // Try to convert the supplied value to a double, and fail if not
                try {
                    chance = args[0].toDouble().absoluteValue
                } catch (ex: NumberFormatException) {
                    if (user is Player) {
                        user.sendMessage(
                            "§f[ §cBoneTwerker §f] " + args[0] + " is not a valid double!"
                        )
                    } else {
                        PaperPluginLogger.getGlobal()
                            .info("[ BoneTwerker ] " + args[0] + " is not a valid double!")
                    }
                    return true
                }

                // Fail if chance is over 100
                if (chance > 100) {
                    if (user is Player) {
                        user.sendMessage("§f[ §cBoneTwerker §f] Chance cannot be greater than 100!")
                    } else {
                        PaperPluginLogger.getGlobal()
                            .info("[ BoneTwerker ] Chance cannot be greater than 100!")
                    }
                    return true
                }

                // Save new chance values
                mainInstance.config.set("chance", chance)
                mainInstance.saveConfig()

                if (user is Player) {
                    user.sendMessage("§f[ §aBoneTwerker §f] Chance set to §e$chance")
                } else {
                    PaperPluginLogger.getGlobal().info("[ BoneTwerker ] Chance set to $chance")
                }
            } else {

                // Display current chance values
                if (user is Player) {
                    user.sendMessage(
                        "§f[ §aBoneTwerker §f] Chance set to §e" + mainInstance.config.get("chance")
                    )
                } else {
                    PaperPluginLogger.getGlobal()
                        .info("[ BoneTwerker ] Chance set to " + mainInstance.config.get("chance"))
                }
            }
        } else {
            user.sendMessage(
                "§f[ §cBoneTwerker §f] You don't have permission to execute this command!"
            )
        }

        return true
    }
}
