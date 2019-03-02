package org.carlspring.strongbox.services;

import org.carlspring.strongbox.client.MutableRemoteRepositoryRetryArtifactDownloadConfiguration;
import org.carlspring.strongbox.configuration.Configuration;
import org.carlspring.strongbox.configuration.MutableConfiguration;
import org.carlspring.strongbox.configuration.MutableProxyConfiguration;
import org.carlspring.strongbox.configuration.MutableSmtpConfiguration;
import org.carlspring.strongbox.storage.MutableStorage;
import org.carlspring.strongbox.storage.repository.MutableRepository;
import org.carlspring.strongbox.storage.routing.MutableRoutingRule;

import java.util.List;

/**
 * @author mtodorov
 */
public interface ConfigurationManagementService
{

    MutableConfiguration getMutableConfigurationClone();

    Configuration getConfiguration();

    void setConfiguration(MutableConfiguration configuration);

    void setInstanceName(String instanceName);

    void setBaseUrl(String baseUrl);

    void setPort(int port);

    void setProxyConfiguration(String storageId,
                               String repositoryId,
                               MutableProxyConfiguration proxyConfiguration);

    void saveStorage(MutableStorage storage);

    void removeStorage(String storageId);

    void saveRepository(String storageId,
                        MutableRepository repository);

    void removeRepositoryFromAssociatedGroups(String storageId,
                                              String repositoryId);

    void removeRepository(String storageId,
                          String repositoryId);

    void setProxyRepositoryMaxConnections(String storageId,
                                          String repositoryId,
                                          int numberOfConnections);

    boolean updateRoutingRule(int index,
                              MutableRoutingRule routingRule);

    boolean addRoutingRule(MutableRoutingRule routingRule);

    boolean removeRoutingRule(int index);

    void addRepositoryToGroup(String storageId,
                              String repositoryId,
                              String repositoryGroupMemberId);

    void setRepositoryArtifactCoordinateValidators();

    void putInService(String storageId,
                      String repositoryId);

    void putOutOfService(String storageId,
                         String repositoryId);

    void setArtifactMaxSize(String storageId,
                            String repositoryId,
                            long value);

    void set(MutableRemoteRepositoryRetryArtifactDownloadConfiguration remoteRepositoryRetryArtifactDownloadConfiguration);

    void addRepositoryArtifactCoordinateValidator(String storageId,
                                                  String repositoryId,
                                                  String alias);

    boolean removeRepositoryArtifactCoordinateValidator(String storageId,
                                                        String repositoryId,
                                                        String alias);

    void setCorsAllowedOrigins(List<String> allowedOrigins);

    void setSmtpSettings(MutableSmtpConfiguration smtpConfiguration);

}
