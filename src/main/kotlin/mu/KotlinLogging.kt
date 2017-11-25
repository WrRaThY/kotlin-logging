package mu

import mu.internal.KLoggerFactory


object KotlinLogging {
    /**
     * This method allow defining the LOG in a file in the following way:
     * val LOG = KotlinLogging.LOG {}
     */
    fun logger(func: () -> Unit): KLogger = KLoggerFactory.logger(func)

    fun logger(name: String): KLogger = KLoggerFactory.logger(name)
}
