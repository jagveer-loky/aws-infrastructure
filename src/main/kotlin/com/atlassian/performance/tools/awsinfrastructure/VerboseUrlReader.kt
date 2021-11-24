package com.atlassian.performance.tools.awsinfrastructure

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.net.URL

internal class VerboseUrlReader : (URL) -> String? {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    override fun invoke(
        serviceUrl: URL
    ) = try {
        serviceUrl
            .also { logger.debug("Querying $it") }
            .readText()
            .also { logger.debug("Got \"$it\" from $serviceUrl") }
    } catch (e: Exception) {
        logger.error("Failed when querying $serviceUrl", e)
        throw e
    }
}