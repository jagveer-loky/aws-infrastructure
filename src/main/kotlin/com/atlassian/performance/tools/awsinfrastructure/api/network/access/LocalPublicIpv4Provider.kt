package com.atlassian.performance.tools.awsinfrastructure.api.network.access

import com.atlassian.performance.tools.awsinfrastructure.Ipv4Validator
import com.atlassian.performance.tools.awsinfrastructure.VerboseUrlReader
import java.net.URL
import java.util.function.Supplier

class LocalPublicIpv4Provider private constructor(
    private val servicesToQuery: List<URL>,
    private val urlReader: (serviceUrl: URL) -> String?,
    private val ipValidator: (ip: String) -> Boolean
) : () -> String, Supplier<String> {
    object Defaults {
        val servicesToQuery: List<URL> = listOf(
            URL("https://checkip.amazonaws.com")
        )
        val urlReader: (serviceUrl: URL) -> String? = VerboseUrlReader()
        val ipValidator: (ip: String) -> Boolean = Ipv4Validator()
    }

    constructor() : this(
        servicesToQuery = Defaults.servicesToQuery,
        urlReader = Defaults.urlReader,
        ipValidator = Defaults.ipValidator
    )

    override fun invoke() = get()

    override fun get() = servicesToQuery
        .asSequence()
        .mapNotNull { serviceUrl -> urlReader(serviceUrl)?.trim() }
        .filter { it.isNotEmpty() }
        .filter { ipValidator(it) }
        .toSet()
        .let { set ->
            if (set.isEmpty()) {
                throw IllegalStateException("Queried services didn't report any valid public IP for this machine")
            }
            if (set.size != 1) {
                throw IllegalStateException("Queried services reported different public IPs for this machine: ${set.toList()}")
            }
            set.first()
        }

    class Builder {
        private var servicesToQuery: MutableList<URL> = Defaults.servicesToQuery.toMutableList()
        private var urlReader: (serviceUrl: URL) -> String? = Defaults.urlReader
        private var ipValidator: (ip: String) -> Boolean = Defaults.ipValidator

        fun servicesToQuery(servicesToQuery: List<URL>) = apply { this.servicesToQuery = servicesToQuery.toMutableList() }
        fun serviceToQuery(serviceUrl: URL) = apply { this.servicesToQuery.add(serviceUrl) }
        fun urlReader(urlReader: (serviceUrl: URL) -> String?) = apply { this.urlReader = urlReader }
        fun ipValidator(ipValidator: (ip: String) -> Boolean) = apply { this.ipValidator = ipValidator }

        fun build() = LocalPublicIpv4Provider(
            servicesToQuery = servicesToQuery,
            urlReader = urlReader,
            ipValidator = ipValidator
        )
    }
}
