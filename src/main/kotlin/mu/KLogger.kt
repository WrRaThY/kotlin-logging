package mu

import mu.internal.toStringSafe
import org.slf4j.Logger
import org.slf4j.ext.XLogger

/**
 * An extension for [Logger] with Lazy message evaluation
 * example:
 * LOG.info{"this is $lazy evaluated string"}
 */
abstract class KLogger(open val underlyingLogger: Logger) : XLogger(underlyingLogger) {

    /**
     * Lazy add a log message if isTraceEnabled is true
     */
    open fun trace(msg: () -> Any?) {
        if (isTraceEnabled) trace(msg.toStringSafe())
    }

    /**
     * Lazy add a log message if isDebugEnabled is true
     */
    open fun debug(msg: () -> Any?) {
        if (isDebugEnabled) debug(msg.toStringSafe())
    }

    /**
     * Lazy add a log message if isInfoEnabled is true
     */
    open fun info(msg: () -> Any?) {
        if (isInfoEnabled) info(msg.toStringSafe())
    }

    /**
     * Lazy add a log message if isWarnEnabled is true
     */
    open fun warn(msg: () -> Any?) {
        if (isWarnEnabled) warn(msg.toStringSafe())
    }

    /**
     * Lazy add a log message if isErrorEnabled is true
     */
    open fun error(msg: () -> Any?) {
        if (isErrorEnabled) error(msg.toStringSafe())
    }

    /**
     * Lazy add a log message with throwable payload if isTraceEnabled is true
     */
    open fun trace(t: Throwable, msg: () -> Any?) {
        if (isTraceEnabled) trace(msg.toStringSafe(), t)
    }

    /**
     * Lazy add a log message with throwable payload if isDebugEnabled is true
     */
    open fun debug(t: Throwable, msg: () -> Any?) {
        if (isDebugEnabled) debug(msg.toStringSafe(), t)
    }

    /**
     * Lazy add a log message with throwable payload if isInfoEnabled is true
     */
    open fun info(t: Throwable, msg: () -> Any?) {
        if (isInfoEnabled) info(msg.toStringSafe(), t)
    }

    /**
     * Lazy add a log message with throwable payload if isWarnEnabled is true
     */
    open fun warn(t: Throwable, msg: () -> Any?) {
        if (isWarnEnabled) warn(msg.toStringSafe(), t)
    }

    /**
     * Lazy add a log message with throwable payload if isErrorEnabled is true
     */
    open fun error(t: Throwable, msg: () -> Any?) {
        if (isErrorEnabled) error(msg.toStringSafe(), t)
    }
}
