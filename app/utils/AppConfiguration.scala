
package utils

import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
/**
 *  @author kigaita
 * Global app configuration
 */
object AppConfiguration {
  val ParisTimeZone: DateTimeZone = DateTimeZone.forID("Europe/Paris")
  val DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
  val DateFormat = "yyyy-MM-dd"
  val DateFormatter = DateTimeFormat.forPattern(DateFormat)
  val TimeFormatter = DateTimeFormat.forPattern("HH:mm:ss")
  val HourPattern = "HH:mm"
  val HourFormatter = DateTimeFormat.forPattern("HH:mm")
}