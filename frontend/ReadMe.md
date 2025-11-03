## GitHub Org Snapshot – Frontend

A simple static frontend to display top repositories of a selected GitHub organization.  
It communicates with the backend API (`/api/org/{org}/repos`) and shows repository info in cards.

---

## Tech Stack

- HTML
- CSS
- JavaScript (vanilla)
- Fetch API to call backend

---

## Project Structure

```bash
/frontend
├── index.html # Main HTML page
├── script.js # JS logic for API calls and DOM manipulation
├── style.css # Styles
└── README.md
```
---

Prerequisites
- A running backend at `http://localhost:8080` (or custom) and use API_BASE_URL in `script.js`
- Any modern browser (Chrome, Firefox, Edge, Safari)

---

## Setup & Run

1. Make sure the backend is running locally
2. Open `index.html` in your browser (double-click or open via a local server)

> The frontend uses `script.js` with the constant:

```javascript
const API_BASE_URL = 'http://localhost:8080/github-org-snapshot/v1/api';
```
You can modify it if your backend runs on a different host/port.

Usage
Enter a GitHub organization (default: apple or spring-projects)