package org.snapshot.github.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.snapshot.github.model.GetGitHubRepoResponse;
import org.snapshot.github.model.GitHubRepoResponseItem;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetGitHubRepoServiceTest {

    private static final GitHubRepoResponseItem REPO_A = GitHubRepoResponseItem.builder()
            .name("some-repo-A")
            .htmlUrl("some-a-url.com")
            .stargazersCount(150)
            .forksCount(30)
            .language("Java")
            .updatedAt("2024-01-01T00:00:00Z")
            .description("Some description A")
            .build();

    private static final GitHubRepoResponseItem REPO_B = GitHubRepoResponseItem.builder()
            .name("some-repo-B")
            .htmlUrl("some-b-url.com")
            .stargazersCount(100)
            .forksCount(20)
            .language("Java")
            .updatedAt("2024-01-02T00:00:00Z")
            .description("Some description B")
            .build();

    @Mock
    private RestTemplate restTemplate;
    private GetGitHubRepoService service;

    @BeforeEach
    void setUp() {
        service = new GetGitHubRepoService(
                "https://api.github.com",
                "test-agent",
                100,
                restTemplate
        );
    }

    @Test
    void shouldReturnGitHubRepoByOrganization() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(GitHubRepoResponseItem[].class)
        )).thenReturn(ResponseEntity.ok(new GitHubRepoResponseItem[]{REPO_A, REPO_B}));

        GetGitHubRepoResponse response = service.fetchTopRepositories("test-org", 5, "stars");

        assertThat(response.getRepos())
                .hasSize(2)
                .extracting("name")
                .containsExactly("some-repo-A", "some-repo-B");
    }

    @Test
    void shouldReturnGitHubRepoByOrganizationWithLimit() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(GitHubRepoResponseItem[].class)
        )).thenReturn(ResponseEntity.ok(new GitHubRepoResponseItem[]{REPO_A, REPO_B}));

        GetGitHubRepoResponse response = service.fetchTopRepositories("test-org", 1, "stars");

        assertThat(response.getRepos())
                .hasSize(1)
                .extracting(GitHubRepoResponseItem::getName)
                .containsExactly("some-repo-A");
    }

    @Test
    void shouldReturnGitHubRepoByOrganizationWithSort() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(GitHubRepoResponseItem[].class)
        )).thenReturn(ResponseEntity.ok(new GitHubRepoResponseItem[]{REPO_A, REPO_B}));

        GetGitHubRepoResponse response = service.fetchTopRepositories("test-org", 5, "updated");

        assertThat(response.getRepos())
                .hasSize(2)
                .extracting(GitHubRepoResponseItem::getName)
                .containsExactly("some-repo-B", "some-repo-A");
    }

    @Test
    void shouldHandleGitHubApiErrorsAndReturnEmptyRepos() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(GitHubRepoResponseItem[].class)
        )).thenThrow(new RestClientException("GitHub API error"));

        GetGitHubRepoResponse response = service.fetchTopRepositories("test-org", 5, "stars");

        assertThat(response)
                .isNotNull()
                .extracting(GetGitHubRepoResponse::getRepos)
                .asList()
                .isEmpty();
    }

    @Test
    void shouldCapLimitBelowMinimum() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(GitHubRepoResponseItem[].class)
        )).thenReturn(ResponseEntity.ok(new GitHubRepoResponseItem[]{REPO_A, REPO_B}));

        GetGitHubRepoResponse response = service.fetchTopRepositories("test-org", 0, "stars");

        assertThat(response.getRepos()).hasSize(1);
    }

    @Test
    void shouldCapLimitAboveMaximum() {
        GitHubRepoResponseItem[] repos = IntStream.range(1, 26)
                .mapToObj(i -> GitHubRepoResponseItem.builder()
                        .name("repo-" + i)
                        .stargazersCount(i)
                        .updatedAt("2024-01-01T00:00:00Z")
                        .build())
                .toArray(GitHubRepoResponseItem[]::new);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(GitHubRepoResponseItem[].class)
        )).thenReturn(ResponseEntity.ok(repos));

        GetGitHubRepoResponse response = service.fetchTopRepositories("test-org", 25, "stars");

        assertThat(response.getRepos()).hasSize(20);
    }

    @Test
    void shouldDefaultToStarsWhenSortIsInvalid() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(GitHubRepoResponseItem[].class)
        )).thenReturn(ResponseEntity.ok(new GitHubRepoResponseItem[]{REPO_B, REPO_A}));

        GetGitHubRepoResponse response = service.fetchTopRepositories("test-org", 5, "invalid-sort");

        assertThat(response.getRepos().get(0).getName()).isEqualTo("some-repo-A");
    }
}
