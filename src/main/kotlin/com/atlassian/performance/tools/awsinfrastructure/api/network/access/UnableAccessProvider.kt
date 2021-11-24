package com.atlassian.performance.tools.awsinfrastructure.api.network.access

import com.amazonaws.services.ec2.model.SecurityGroup

class UnableAccessProvider : AccessProvider {
    override fun provideAccess(
        cidr: String
    ) = false

    override fun provideAccess(
        securityGroup: SecurityGroup
    ) = false
}
