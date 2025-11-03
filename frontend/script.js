const API_BASE_URL = 'http://localhost:8080/github-org-snapshot/v1/api';

const orgInput = document.getElementById('orgInput');
const sortSelect = document.getElementById('sortSelect');
const limitInput = document.getElementById('limitInput');
const loadButton = document.getElementById('loadButton');
const repoList = document.getElementById('repoList');
const loading = document.getElementById('loading');
const errorDiv = document.getElementById('error');

loadButton.addEventListener('click', () => {
    fetchRepos(orgInput.value, sortSelect.value, limitInput.value);
});

async function fetchRepos(org, sort, limit) {
    repoList.innerHTML = '';
    errorDiv.style.display = 'none';
    loading.style.display = 'block';

    try {
        const response = await fetch(`${API_BASE_URL}/org/${org}/repos?limit=${limit}&sort=${sort}`);
        if (!response.ok) throw new Error(`HTTP error: ${response.status}`);

        const data = await response.json();
        loading.style.display = 'none';

        if (data.repos.length === 0) {
            repoList.innerHTML = '<p>No repositories found.</p>';
            return;
        }

        data.repos.forEach(repo => {
            const card = document.createElement('div');
            card.className = 'repo-card';

            card.innerHTML = `
                <h2><a href="${repo.html_url}" target="_blank">${repo.name}</a></h2>
                <p>${repo.description || ''}</p>
                <p>Language: ${repo.language || 'N/A'}</p>
                <div class="repo-stats">
                    <span>⭐ ${repo.stargazers_count}</span>
                    <span>🍴 ${repo.forks_count}</span>
                    <span>Updated: ${new Date(repo.updated_at).toLocaleDateString()}</span>
                </div>
            `;
            repoList.appendChild(card);
        });
    } catch (err) {
        loading.style.display = 'none';
        errorDiv.style.display = 'block';
        errorDiv.textContent = `Failed to load repositories: ${err.message}`;
    }
}
