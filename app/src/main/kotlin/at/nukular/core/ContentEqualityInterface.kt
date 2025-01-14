package at.nukular.core

/**
 * Interface to compare the contents of an object
 */
interface ContentEqualityInterface {
    fun notSameContent(toCompare: Any?): Boolean {
        return !sameContent(toCompare)
    }

    fun sameContent(toCompare: Any?): Boolean {
        return toCompare === this
    }

    companion object {
        @JvmStatic
        fun sameListContent(
            listA: List<ContentEqualityInterface?>?,
            listB: List<ContentEqualityInterface?>?
        ): Boolean {
            if (listA == null && listB == null) {
                return true
            }
            if (listA == null || listB == null) {
                return false
            }
            if (listA === listB) {
                return true
            }
            val e1 = listA.listIterator()
            val e2 = listB.listIterator()
            while (e1.hasNext() && e2.hasNext()) {
                val o1 = e1.next()
                val o2 = e2.next()
                if (!(o1?.sameContent(o2) ?: (o2 == null))) {
                    return false
                }
            }
            return !(e1.hasNext() || e2.hasNext())
        }

        @JvmStatic
        fun notSameListContent(
            listA: List<ContentEqualityInterface?>?,
            listB: List<ContentEqualityInterface?>?
        ): Boolean {
            return !sameListContent(listA, listB)
        }

        @JvmStatic
        fun sameContent(
            object1: ContentEqualityInterface?,
            object2: ContentEqualityInterface?
        ): Boolean {
            if (object1 == null && object2 == null) {
                return true
            }
            return if (object1 == null || object2 == null) {
                false
            } else object1.sameContent(object2)
        }

        @JvmStatic
        fun notSameContent(
            object1: ContentEqualityInterface?,
            object2: ContentEqualityInterface?
        ): Boolean {
            return !sameContent(object1, object2)
        }
    }
}
