package org.snapshot.github.model.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RestApiConstants {

    public static final String GET_GITHUB_REPOS_DEFAULT_LIMIT_VALUE = "5";
    public static final String GET_GITHUB_REPOS_DEFAULT_SORT_KEY = "stars";
    public static final String USER_AGENT_HEADER_KEY = "User-Agent";
    public static final String GITHUB_ORG_VALUE = "org";

    public static final String GITHUB_BASE_PATH = "/github-org-snapshot/v1/api";
    public static final String GITHUB_HEALTH_SUBPATH = "/health";
    public static final String GITHUB_REPO_SUBPATH = "/org/{org}/repos";
}
