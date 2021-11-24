package com.atlassian.performance.tools.awsinfrastructure.api.network.access

class NoopAccessRequester : AccessRequester {
    override fun requestAccess(
        accessProvider: AccessProvider
    ) = false
}
