import java.util.Locale

fun Long.millisToPlayerDurationFormat(): String {
    val minutes = (this / 1000) / 60
    val seconds = (this / 1000) % 60

    val formattedMinutes = if (minutes >= 100) {
        minutes.toString()
    } else {
        String.format(Locale.getDefault(), "%02d", minutes)
    }

    val formattedSeconds = String.format(Locale.getDefault(), "%02d", seconds)

    return "$formattedMinutes:$formattedSeconds"
}
