import spark.kotlin.get
import spark.kotlin.staticFiles
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun String.runCommand(workingDir: File): String? {
    try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        return proc.inputStream.bufferedReader().readText()
    }
    catch(e: IOException) {
        e.printStackTrace()
        return null
    }
}

fun main(args: Array<String>) {
    staticFiles.location("/public")

    // TODO: find a way to serve out public/index.html from /home and /uptime also, so we can support permalinking.
    get(path = "/uprecords") {
        val output = "uprecords".runCommand(File("."));
        //output = "foo";
        output ?: "Getting uptime data failed"
    }
}