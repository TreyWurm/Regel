package at.nukular.regel

import android.content.Context
import android.os.storage.StorageManager
import androidx.core.text.isDigitsOnly
import at.nukular.core.extensions.size
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton


private const val DIR_NAME = "logs"
private const val MAX_LOG_DIR_SIZE = 1_000_000L * 2 // 2MB == ~9k log entries
private const val CURRENT_FILE_NAME = "current"
private const val BACKUP_FILE_NAME = "current.backup"

@Singleton
class FileWriterReader @Inject constructor(
    @ApplicationContext val context: Context,
) {

    private var cacheSpaceQuota: Long = MAX_LOG_DIR_SIZE
    private val dataDir: File = File(context.dataDir, DIR_NAME)
    private val entries = mutableSetOf<LocalDate>()


    init {
        (context.getSystemService(Context.STORAGE_SERVICE) as? StorageManager)?.let { storageManager ->
            try {
                cacheSpaceQuota = storageManager.getCacheQuotaBytes(storageManager.getUuidForPath(context.cacheDir))
            } catch (_: Exception) {
            }
        }

        if (!dataDir.exists() && !dataDir.mkdirs()) {
            Timber.e("Failed to make disk-data dir $dataDir")
        }
    }

    fun addEntry(date: LocalDate) {
        entries.add(date)
    }

    fun writeEntriesToFile() {
        val newFile = File(dataDir, CURRENT_FILE_NAME)
        var dataWritten = false

        newFile.bufferedWriter().use { writer ->
            entries.forEach { log ->
                try {
                    writer.appendLine(log.toString())
                    dataWritten = true
                } catch (e: Exception) {
                    Timber.w("Error writing Entry to file. $e")
                }
            }
        }

        if (!dataWritten) {
            newFile.delete()
        }
    }

    fun readEntriesFromFile(): Set<LocalDate> {
        val currentFile = File(dataDir, CURRENT_FILE_NAME)
        val backupFile = File(dataDir, BACKUP_FILE_NAME)
        val file = when {
            currentFile.exists() && currentFile.isFile -> currentFile
            backupFile.exists() && backupFile.isFile -> backupFile
            else -> return emptySet()
        }

        file.bufferedReader().use { reader ->
            var currentLine = reader.readLine()
            while (currentLine != null) {
                val buffer = currentLine
                currentLine = reader.readLine()

                try {
                    val date = LocalDate.parse(buffer)
                    entries.add(date)
                } catch (e: Exception) {
                    Timber.w("Error reading Entry from file. $e")
                }
            }
        }

        return entries.sortedDescending().toSet()
    }

    /**
     * Makes sure cache dir does not get too big.
     * The maximum size is defined by 2 things, and always picks the lower boundary
     * * [MAX_LOG_DIR_SIZE] For API < 26
     * * [StorageManager.getCacheQuotaBytes] For API >= 26
     *
     * The log directory only allows files with UNIX-timestamps as names. Every other file
     * and possible subfolder will be deleted first, BEFORE deleting the oldest log files
     */
    private fun pruneLogDir() {
        // Make sure cache dir does not get too big
        if (!dataDir.exists() && !dataDir.mkdirs()) {
            return
        }

        val files = dataDir.listFiles() ?: return

        // Remove all files that are not valid log files == filename has to be a unix timestamp
        files
            .filterNot { it.name.isDigitsOnly() }
            .forEach { file ->
                val size = file.size
                Timber.w("Deleting not recognized log ${if (file.isDirectory) "directory" else "file"} \"${file.name}\" of size: $size")
                file.deleteRecursively()
            }

        var folderSize = dataDir.size
        if (folderSize > cacheSpaceQuota || folderSize > MAX_LOG_DIR_SIZE) {
            Timber.w("Log directory too big")
        }

        files.forEach { file ->
            if (folderSize <= cacheSpaceQuota && folderSize <= MAX_LOG_DIR_SIZE) {
                return
            }

            val size = file.size
            folderSize -= size
            Timber.w("Deleting cached log ${if (file.isDirectory) "directory" else "file"} \"${file.name}\" of size: $size")
            file.deleteRecursively()
        }
    }

}