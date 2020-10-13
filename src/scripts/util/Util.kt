package scripts.util

import org.tribot.api2007.Player
import org.tribot.api2007.types.RSPlayer
import java.awt.Image
import java.net.URL
import javax.imageio.ImageIO

fun RSPlayer.isStunned() = Player.getPosition().y == 1000
fun RSPlayer.isAnimating() = Player.getAnimation() != -1
fun readImageUrl(url: String): Image? = ImageIO.read(URL(url))