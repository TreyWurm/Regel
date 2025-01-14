package at.nukular.core.ui

import android.os.Bundle


/**
 * Implement this interface if you want to pass arguments to [android.app.Activity] or [androidx.fragment.app.Fragment]
 */
interface Arguments {

    /**
     * Check if given intent extras are valid and component should be started
     *
     * @param arguments The intent extras
     * @return True if everything's okay; False otherwise
     */
    fun intentArgumentsValid(arguments: Bundle?): Boolean {
        return parseBundle(arguments)
    }


    /**
     * Check if given arguments stored during [android.app.Activity.onSaveInstanceState] or [androidx.fragment.app.Fragment.onSaveInstanceState]
     * are valid and component should be started
     *
     * @param savedInstanceState Bundle containing saved data
     * @return True if everything's okay; False otherwise
     */
    fun savedInstanceStateValid(savedInstanceState: Bundle): Boolean {
        return parseBundle(savedInstanceState)
    }


    /**
     * Check if the passed bundle is valid
     *
     * @return True if everything's okay; False otherwise
     */
    fun parseBundle(bundle: Bundle?): Boolean {
        return true
    }
}