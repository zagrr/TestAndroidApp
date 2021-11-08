package ru.zagrr.testandroidapp.utils.ui

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes

class PrimaryActionModeCallback : ActionMode.Callback {

    var actionItemClickListener: OnActionItemClickListener? = null
    var isStarted : Boolean = false

    private var mode: ActionMode? = null
    @MenuRes private var menuResId: Int = 0
    private var title: String? = null
    private var subtitle: String? = null

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {

        this.mode = mode
        mode.menuInflater.inflate(menuResId, menu)
        mode.title = title
        mode.subtitle = subtitle

        actionItemClickListener?.onStartActionMode()
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        this.mode = null
        actionItemClickListener?.onDestroyActionMode()
        isStarted = false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {

        val ret = actionItemClickListener?.onActionItemClick(item)
        if (ret == true)
            mode.finish()

        return true
    }

    fun startActionMode(view: View, @MenuRes menuResId: Int, title: String? = null, subtitle: String? = null) {
        this.menuResId = menuResId
        this.title = title
        this.subtitle = subtitle
        view.startActionMode(this)

        isStarted = true
    }

    fun finishActionMode() {
        mode?.finish()
        isStarted = false
    }

    fun setTitle(title: String) {
        mode?.title = title
    }

}

interface OnActionItemClickListener {
    fun onActionItemClick(item: MenuItem) : Boolean
    fun onDestroyActionMode()
    fun onStartActionMode()
}
