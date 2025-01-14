package at.nukular.core.extensions

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun ContentResolver.getFileName(uri: Uri): String? {
    return when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> {
            query(uri, null, null, null, null)?.use { cursor ->
                cursor.moveToFirst()
                cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
            }
        }

        else -> uri.path?.let { File(it).name }
    }
}