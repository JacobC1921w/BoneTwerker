package dev.c0b.bonetwerker.listeners

import dev.c0b.bonetwerker.BoneTwerker
import org.bukkit.Instrument
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Note
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import kotlin.math.pow
import kotlin.random.Random

class onPlayerSneakEvent(private val mainInstance: BoneTwerker): Listener {

    // List of blocks we want to apply bonemeal to
    private val blockList = listOf(
        Material.ACACIA_SAPLING,
        Material.BIRCH_SAPLING,
        Material.CHERRY_SAPLING,
        Material.CRIMSON_FUNGUS,
        Material.DARK_OAK_SAPLING,
        Material.FARMLAND,
        Material.JUNGLE_SAPLING,
        Material.MANGROVE_PROPAGULE,
        Material.OAK_SAPLING,
        Material.SPRUCE_SAPLING,
        Material.WARPED_FUNGUS,
    )

    @EventHandler
    fun onPlayerSneak(event: PlayerToggleSneakEvent) {
        // Make sure the plugin is enabled in config
        if (mainInstance.config.get("enablePlugin") == true) {

            // Make sure player is sneaking, otherwise the following code gets called on sneak, and off sneak
            if (event.isSneaking) {

                // Retrieve values from config files
                val chance = mainInstance.config.get("chance") as Double
                val radius = mainInstance.config.get("radius") as Double

                // Generate a random number between 0 and 100 for chance, make sure the config chance is equal or greater
                val randomNumber = Random.nextDouble(0.0, 100.0)
                if (randomNumber <= chance) {

                    // Play an F#2 on the bass for success!
                    event.player.playNote(event.player.location, Instrument.BASS_GUITAR, Note.sharp(2, Note.Tone.F))

                    // Iterate over blocks in a set radius, and check if the blocks are in the block list
                    for (block in getBlocksInRadius(event.player, radius)) {
                        if (blockList.contains(block.type)) {

                            // If it's farmland, we're gonna bonemeal the block on top (i.e. the seeds). Otherwise just bonemeal the block
                            if (block.type == Material.FARMLAND) {
                                event.player.world.getBlockAt(block.x, block.y + 1, block.z).applyBoneMeal(BlockFace.UP)
                            } else {
                                block.applyBoneMeal(BlockFace.UP)
                            }
                        }
                    }
                } else {

                    // Play an F#0 on the bass for failure :(
                    event.player.playNote(event.player.location, Instrument.BASS_GUITAR, Note.sharp(0, Note.Tone.F))
                }
            }
        }
    }

    /**
     * Gets a list of blocks within a specified radius around a player.
     * This method iterates through a cubic area centered on the player's location,
     * extending outwards by the given radius. For each block in the area, it calculates
     * the squared distance between the block's center and the player's center. If the
     * squared distance is less than or equal to the squared radius, the block is added
     * to a list and returned.
     * @param player The player around whom to get blocks.
     * @param radius The radius of the cubic area to search, in blocks.
     * @return A list of blocks within the specified radius of the player.
     */
    private fun getBlocksInRadius(player: Player, radius: Double): List<Block> {
        val location = player.location
        val centerX = location.blockX
        val centerY = location.blockY
        val centerZ = location.blockZ
        val blockList = mutableListOf<Block>()

        for (x in centerX - radius.toInt()..centerX + radius.toInt()) {
            for (y in centerY - radius.toInt()..centerY + radius.toInt()) {
                for (z in centerZ - radius.toInt()..centerZ + radius.toInt()) {
                    val distanceSquared = (x - centerX.toDouble()).pow(2.0) +
                            (y - centerY.toDouble()).pow(2.0) +
                            (z - centerZ.toDouble()).pow(2.0)
                    if (distanceSquared <= radius.pow(2.0)) {
                        val blockLocation = Location(player.world, x.toDouble(), y.toDouble(), z.toDouble())
                        blockList.add(blockLocation.block)
                    }
                }
            }
        }
        return blockList
    }
}