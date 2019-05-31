package com.codding.test.startoverflowuser.util
import android.os.Bundle
import android.util.Log
import com.codding.test.startoverflowuser.AppConfigs
object AppLogger {
    private const val TYPE_DEBUG = 0
    private const val TYPE_ERROR = 1
    private const val TYPE_VERBOSE = 2
    private const val TYPE_INFO = 3

    fun <T> error(message:T) {
        showLog(TYPE_ERROR, AppConfigs.LOG_TAG, "", message)
    }

    fun <T> error(obj : Any, message:T) {
        error(obj.javaClass.simpleName, message)
    }

    private fun <T> error(prefix:String, message:T) {
        showLog(TYPE_ERROR, AppConfigs.LOG_TAG, prefix, message)
    }

    fun <T> debug(message:T) {
        showLog(TYPE_DEBUG, AppConfigs.LOG_TAG, "", message)
    }

    fun <T> debug(obj : Any, message:T) {
        debug(obj.javaClass.getSimpleName(), message)
    }

    private fun <T> debug(prefix:String, message:T) {
        showLog(TYPE_DEBUG, AppConfigs.LOG_TAG, prefix, message)
    }


    private fun <T> showLog(type:Int, tag:String, prefix:String, message:T) {
        if (!AppConfigs.ENABLE_LOG) return
        var prefixString = ""
        if (prefix != null && prefix.length > 0) prefixString = "[" + prefix + "] "
        val messageString = getMessage<T>(message)

        when (type) {
            TYPE_DEBUG -> Log.d(tag, prefixString + messageString)
            TYPE_ERROR -> Log.e(tag, prefixString + messageString)
            TYPE_VERBOSE -> Log.e(tag, prefixString + messageString)
            TYPE_INFO -> Log.e(tag, prefixString + messageString)
            else -> Log.e(tag, prefixString + messageString)
        }
    }

    private fun <T> getMessage(message:T):String {
        if (message == null) return "NULL!!!"
        var messageString : String
        if (message is Exception)
        {
            messageString = ("[" + (message as Exception).javaClass.getSimpleName() + "]"
                    + (message as Exception).message)
        }
        else if (message is Throwable)
        {
            messageString = ("[" + (message as Throwable).javaClass.getSimpleName() + "]"
                    + (message as Throwable).message)
        }
        else
        {
            if ((message is String
                        || message is Boolean
                        || message is Float
                        || message is Int
                        || message is Long))
            {
                messageString = (message).toString()
            }
            else if (message is Bundle)
            {
                messageString = (message as Bundle).toString()
            }
            else
            {
                try
                {
                    messageString = getJsonPrettyData(message)
                }
                catch (e:Exception) {
                    messageString = message.toString()
                }
            }
        }
        if (messageString == null) return "NULL!!!"
        return messageString
    }
}