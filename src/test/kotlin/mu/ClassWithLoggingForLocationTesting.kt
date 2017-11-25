package mu

class ClassWithLoggingForLocationTesting {
    companion object : KLogging()

    fun log() {
        LOG.info("test")
    }

    fun logLazy() {
        LOG.info { "test" }
    }

    fun logNull() {
        LOG.info(null)
    }
}
