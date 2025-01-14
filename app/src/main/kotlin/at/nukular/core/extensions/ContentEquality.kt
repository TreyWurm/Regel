package at.nukular.core.extensions

import at.nukular.core.ContentEqualityInterface

fun ContentEqualityInterface?.sameContent(toCompare: ContentEqualityInterface?): Boolean {
    return ContentEqualityInterface.sameContent(this, toCompare)
}

fun ContentEqualityInterface?.notSameContent(toCompare: ContentEqualityInterface?): Boolean {
    return ContentEqualityInterface.notSameContent(this, toCompare)
}

fun List<ContentEqualityInterface>?.sameContent(toCompare: List<ContentEqualityInterface>?): Boolean {
    return ContentEqualityInterface.sameListContent(this, toCompare)
}

fun List<ContentEqualityInterface>?.notSameContent(toCompare: List<ContentEqualityInterface>?): Boolean {
    return ContentEqualityInterface.notSameListContent(this, toCompare)
}