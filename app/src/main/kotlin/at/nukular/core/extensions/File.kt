package at.nukular.core.extensions

import java.io.File


/**
 * In case it is a file it returns its size;
 * Otherwise returns the size of the whole directory including all subfolders/-files
 */
val File.size: Long get() = walkTopDown().map { it.length() }.sum()

