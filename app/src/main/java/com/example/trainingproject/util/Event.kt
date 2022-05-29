package com.example.trainingproject.util

import androidx.lifecycle.Observer

class Event<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else null
    }

    fun peekContent() = content
}

class EventObserver<T>(
    private inline val onError: ((Int?, String?) -> Unit)? = null,
    private inline val onLoading: (() -> Unit)? = null,
    private inline val onSuccess: (T) -> Unit
) : Observer<Event<Resource<T>>> {

    override fun onChanged(t: Event<Resource<T>>?) {
        when(val content = t?.peekContent()) {
            is Resource.Success -> {
                content.data?.let(onSuccess)
            }
            is Resource.Error -> {
                t.getContentIfNotHandled()?.let {
                    onError?.invoke(it.errorRes, it.message)
                }
            }
            is Resource.Loading -> {
                onLoading?.invoke()
            }
            else -> {}
        }
    }

}