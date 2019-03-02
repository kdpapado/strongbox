package org.carlspring.strongbox.services.support;

import org.carlspring.strongbox.configuration.ConfigurationManager;
import org.carlspring.strongbox.providers.io.RepositoryFiles;
import org.carlspring.strongbox.providers.io.RepositoryPath;
import org.carlspring.strongbox.storage.repository.Repository;
import org.carlspring.strongbox.storage.routing.RepositoryIdentifiable;
import org.carlspring.strongbox.storage.routing.RoutingRule;
import org.carlspring.strongbox.storage.routing.RoutingRules;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author Przemyslaw Fusik
 * @see <a href="https://github.com/strongbox/strongbox/wiki/Artifact-Routing-Rules">Artifact Routing Rules</a>
 */
@Component
public class ArtifactRoutingRulesChecker
{

    @Inject
    private ConfigurationManager configurationManager;

    public boolean isDenied(Repository groupRepository,
                            RepositoryPath repositoryPath)
            throws IOException
    {
        final RoutingRules routingRules = configurationManager.getConfiguration().getRoutingRules();
        final List<RoutingRule> denyRules = routingRules.getDenied();
        final List<RoutingRule> acceptRules = routingRules.getAccepted();

        return fitsRoutingRules(groupRepository, repositoryPath, denyRules) &&
               !fitsRoutingRules(groupRepository, repositoryPath, acceptRules);
    }

    private boolean fitsRoutingRules(Repository groupRepository,
                                     RepositoryPath repositoryPath,
                                     List<RoutingRule> routingRules)
            throws IOException
    {

        String artifactPath = RepositoryFiles.relativizePath(repositoryPath);
        Repository subRepository = repositoryPath.getRepository();

        return routingRules.stream()
                           .filter(routingRule -> repositoryMatchesExactly(groupRepository, routingRule)
                                                  || repositoryStorageIdMatches(groupRepository.getStorage().getId(),
                                                                                routingRule)
                                                  || repositoryIdMatches(groupRepository.getId(), routingRule)
                                                  || allMatches(routingRule))
                           .filter(routingRule -> routingRule.getRegex().matcher(artifactPath).matches())
                           .flatMap(routingRule -> routingRule.getRepositories().stream())
                           .filter(routingRuleRepository ->
                                           repositoryMatchesExactly(subRepository, routingRuleRepository)
                                           || repositoryStorageIdMatches(subRepository.getStorage().getId(),
                                                                         routingRuleRepository)
                                           || repositoryIdMatches(subRepository.getId(), routingRuleRepository)
                                           || allMatches(routingRuleRepository))
                           .findFirst()
                           .isPresent();
    }


    private boolean repositoryMatchesExactly(Repository repository,
                                             RepositoryIdentifiable repositoryIdentifiable)
    {
        return repositoryIdentifiable.getStorageId().equals(repository.getStorage().getId())
               &&
               repositoryIdentifiable.getRepositoryId().equals(repository.getId());
    }

    private boolean repositoryStorageIdMatches(String storageId,
                                               RepositoryIdentifiable repositoryIdentifiable)
    {
        return repositoryIdentifiable.getStorageId().equals(storageId)
               &&
               repositoryIdentifiable.getRepositoryId().equals("*");
    }

    private boolean repositoryIdMatches(String repositoryId,
                                        RepositoryIdentifiable repositoryIdentifiable)
    {
        return repositoryIdentifiable.getStorageId().equals("*")
               &&
               repositoryIdentifiable.getRepositoryId().equals(repositoryId);
    }

    private boolean allMatches(RepositoryIdentifiable repositoryIdentifiable)
    {
        return repositoryIdentifiable.getStorageId().equals("*")
               &&
               repositoryIdentifiable.getRepositoryId().equals("*");
    }
}
