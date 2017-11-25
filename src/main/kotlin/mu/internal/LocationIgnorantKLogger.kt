package mu.internal

import mu.KLogger
import org.slf4j.Logger

/**
 * A class wrapping a [Logger] instance that is not location aware
 * all methods of [KLogger] has default implementation
 * the rest of the methods are delegated to [Logger]
 * Hence no implemented methods
 */
internal class LocationIgnorantKLogger(override val underlyingLogger: Logger) : KLogger(underlyingLogger), Logger by underlyingLogger
