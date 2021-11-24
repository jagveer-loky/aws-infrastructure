package com.atlassian.performance.tools.awsinfrastructure.api.network.access

/**
 * Used to allow network interface to request access to certain network resource.
 * Introduced to allow restriction of anonymous access.
 */
interface AccessRequester {

    /**
     * @param accessProvider
     * @return true if it was possible for accessProvider to provide access, false otherwise
     */
    fun requestAccess(accessProvider: AccessProvider): Boolean
}
