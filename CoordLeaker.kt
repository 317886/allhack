package me.luna.trollhack.module.modules.misc

import me.luna.trollhack.TrollHackMod
import me.luna.trollhack.module.Category
import me.luna.trollhack.module.Module
import me.luna.trollhack.util.text.MessageSendUtils
import me.luna.trollhack.util.text.MessageSendUtils.sendServerMessage
import java.io.File

internal object CoordLeaker : Module(
    name = "CoordLeaker",
    description = "Leak your coord at chat box, file path: ${TrollHackMod.DIRECTORY}/CoordLeaker.txt",
    category = Category.MISC
) {
    private val file = File("${TrollHackMod.DIRECTORY}/CoordLeaker.txt")
    private var spammer = "[AutoLeak] My coord in the %world% are %coord%"

    init{
        onEnable {
            if (file.exists()) {
                try {
                    spammer = file.readText().trim()
                    run()
                }catch (e: Exception) {
                    MessageSendUtils.sendNoSpamErrorMessage("$chatName Failed loading file ${TrollHackMod.DIRECTORY}/CoordLeaker.txt, $e")
                    disable()
                }
            } else {
                file.createNewFile()
                file.writeText("[AutoLeak] My coord in the %world% are %coord%")
                run()
            }
        }
    }

    private fun run(){
        when (mc.player?.dimension) {
            -1 -> { // Nether
                spammer = spammer.replace("%world%","Nether")
            }
            0 -> { // Overworld
                spammer = spammer.replace("%world%","Overworld")
            }
            1 -> { // End
                spammer = spammer.replace("%world%","End")
            }
        }
        spammer = spammer.replace("%coord%",mc.player.posX.toInt().toString() + " " + mc.player.posY.toInt().toString() + " " + mc.player.posZ.toInt().toString() )
        sendServerMessage(spammer)
        disable()
    }
}
