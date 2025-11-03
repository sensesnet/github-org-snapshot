package org.snapshot.github.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.snapshot.github.model.GetGitHubRepoResponse;
import org.snapshot.github.model.GitHubRepoResponseItem;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.snapshot.github.model.rest.RestApiConstants.USER_AGENT_HEADER_KEY;

@Slf4j
@RequiredArgsConstructor
public class GetGitHubRepoService {
    private static final String GITHUB_API_URL_TEMPLATE = "%s/orgs/%s/repos?per_page=%d";
    private static final int MIN_REPOS_LIMIT = 1;
    private static final int MAX_REPOS_LIMIT = 20;
    private final String baseUrl;
    private final String userAgent;
    private final int perPage;
    private final RestTemplate restTemplate;


    public GetGitHubRepoResponse fetchTopRepositories(String org,
                                                      int limit,
                                                      String sort) {
        String url = String.format(GITHUB_API_URL_TEMPLATE, baseUrl, org, perPage);

        HttpHeaders headers = new HttpHeaders();
        headers.add(USER_AGENT_HEADER_KEY, userAgent);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        List<GitHubRepoResponseItem> repos;
        try {
            var response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    GitHubRepoResponseItem[].class
            );

            repos = Optional.ofNullable(response.getBody())
                    .map(Arrays::asList)
                    .orElse(List.of());
            log.info("Fetched {} repositories for org '{}'", repos.size(), org);
        } catch (RestClientException e) {
            log.error("Failed to fetch repositories for org '{}': {}", org, e.getMessage());
            repos = List.of();
        }

        return toResponse(repos, sort, limit);
    }

    private GetGitHubRepoResponse toResponse(List<GitHubRepoResponseItem> repos,
                                             String sort,
                                             int limit) {
        if (repos.isEmpty()) {
            return GetGitHubRepoResponse.builder()
                    .repos(List.of())
                    .build();
        }

        int safeLimit = Math.max(MIN_REPOS_LIMIT, Math.min(MAX_REPOS_LIMIT, limit));
        String safeSort = "updated".equalsIgnoreCase(sort) ? "updated" : "stars";

        Comparator<GitHubRepoResponseItem> comparator = safeSort.equals("updated")
                ? Comparator.comparing(GitHubRepoResponseItem::getUpdatedAt).reversed()
                : Comparator.comparing(GitHubRepoResponseItem::getStargazersCount).reversed();

        List<GitHubRepoResponseItem> topRepos = repos.stream()
                .sorted(comparator)
                .limit(safeLimit)
                .collect(Collectors.toList());

        return GetGitHubRepoResponse.builder()
                .repos(topRepos)
                .build();
    }
}
