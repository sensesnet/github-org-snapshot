package org.snapshot.github.api.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.snapshot.github.model.GetGitHubRepoResponse;
import org.snapshot.github.service.GetGitHubRepoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.snapshot.github.model.rest.RestApiConstants.GITHUB_BASE_PATH;
import static org.snapshot.github.model.rest.RestApiConstants.GITHUB_ORG_VALUE;
import static org.snapshot.github.model.rest.RestApiConstants.GITHUB_REPO_SUBPATH;
import static org.snapshot.github.model.rest.RestApiConstants.GET_GITHUB_REPOS_DEFAULT_LIMIT_VALUE;
import static org.snapshot.github.model.rest.RestApiConstants.GET_GITHUB_REPOS_DEFAULT_SORT_KEY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = GITHUB_BASE_PATH)
@RequiredArgsConstructor
@CrossOrigin
public class GitHubController {
    private final GetGitHubRepoService getGitHubRepoService;

    @Operation(summary = "Fetch top github repositories by organization")
    @GetMapping(path = GITHUB_REPO_SUBPATH, produces = APPLICATION_JSON_VALUE)
    public GetGitHubRepoResponse get(@PathVariable(GITHUB_ORG_VALUE) String organization,
                                     @RequestParam(defaultValue = GET_GITHUB_REPOS_DEFAULT_LIMIT_VALUE) int limit,
                                     @RequestParam(defaultValue = GET_GITHUB_REPOS_DEFAULT_SORT_KEY) String sort) {
        return getGitHubRepoService.fetchTopRepositories(organization, limit, sort);
    }
}
