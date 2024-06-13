package dev.c0b.bonetwerker.commands

import dev.c0b.bonetwerker.BoneTwerker
import org.bukkit.Bukkit.getLogger
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TBT(private val mainInstance: BoneTwerker): CommandExecutor {

    override fun onCommand(user: CommandSender, cmd: Command, p2: String, args: Array<out String>?): Boolean {
        // Check if sender has correct permissions
        if (user.hasPermission("BoneTwerker.tbt")) {

            // Flip status and save to config
            val flippedStatus: Boolean = mainInstance.config.get("enablePlugin") != true
            mainInstance.config.set("enablePlugin", flippedStatus)
            mainInstance.saveConfig()

            if (user is Player) {
                user.sendMessage("§f[ §aBoneTwerker §f] enablePlugin set to §e$flippedStatus")
            } else {
                getLogger().info("[ BoneTwerker ] enablePlugin set to $flippedStatus")
            }

        } else {
            user.sendMessage("§f[ §cBoneTwerker §f] You don't have permission to execute this command!")
        }

        return true
    }
}