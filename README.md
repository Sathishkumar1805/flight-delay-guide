# Flight Delay Guide

A full-stack flight delay compensation checker. Passengers enter their flight details and receive an AI-powered eligibility assessment under EC 261/2004, UK261, or US DOT rules, plus a ready-to-send demand letter.

**Stack:** Spring Boot 3.2 (Java 17) · Angular 15 · Gemini AI · AviationStack

---

## Project Structure

```
flight-delay-guide/
├── Dockerfile            # Multi-stage Docker build for Render (repo root)
├── backend/              # Spring Boot REST API (Java 17)
├── frontend/             # Angular 15 SPA
├── .github/
│   └── workflows/
│       ├── backend-ci-cd.yml    # Java → Render
│       ├── frontend-ci-cd.yml   # Angular → Vercel
│       └── security-scan.yml    # Gitleaks + audit
├── .env.example
└── .gitignore
```

---

## Local Development

### Prerequisites
- Java 17
- Maven 3.8+
- Node.js 18 + npm
- Angular CLI 15 (`npm install -g @angular/cli@15`)

### Backend

```bash
cd backend
cp src/main/resources/application-local.yml.example src/main/resources/application-local.yml
# Edit application-local.yml and add your real API keys
mvn spring-boot:run
# Runs on http://localhost:8080/api
```

### Frontend

```bash
cd frontend
npm install
ng serve
# Runs on http://localhost:4200
```

---

## Deploy Backend to Render

### Step 1 — Push your code to GitHub

```bash
git push origin main
```

If you haven't added the remote yet:

```bash
git remote add origin https://github.com/YOUR_USERNAME/flight-delay-guide.git
git push -u origin main
```

### Step 2 — Create a Render account

Go to [render.com](https://render.com) and sign up (free). Connect your GitHub account when prompted.

### Step 3 — Create a new Web Service

> Render has no native Java runtime. The backend deploys via **Docker** using the `Dockerfile` in the `backend/` folder.

1. Click **New → Web Service**
2. Select your `flight-delay-guide` repository
3. Configure the service:

| Setting | Value |
|---------|-------|
| **Name** | `flight-delay-backend` |
| **Root Directory** | *(leave blank — repo root)* |
| **Runtime** | `Docker` |
| **Dockerfile Path** | `./Dockerfile` (auto-detected) |
| **Instance Type** | Free |

Render will auto-detect the `Dockerfile` at the repo root — no build or start commands needed.

### Step 4 — Add environment variables in Render

In your Render service → **Environment** tab, add:

| Key | Value |
|-----|-------|
| `GEMINI_API_KEY` | Your key from [aistudio.google.com](https://aistudio.google.com) |
| `AVIATIONSTACK_API_KEY` | Your key from [aviationstack.com](https://aviationstack.com) |
| `AI_PROVIDER` | `gemini` |
| `CORS_ALLOWED_ORIGINS` | `https://your-frontend.vercel.app` (update after frontend deploy) |

### Step 5 — Deploy

Click **Create Web Service**. Render will:
1. Clone your repo
2. Build the Docker image (runs `mvn package` inside the multi-stage build)
3. Start the container — `$PORT` is auto-injected by Render

First deploy takes ~5–8 minutes (Docker image pull + Maven build). Your backend URL will be:
```
https://flight-delay-backend.onrender.com
```

### Step 6 — Test the backend

```bash
curl https://flight-delay-backend.onrender.com/api/compensation/health
# Expected: {"status":"UP",...}
```

> **Note:** Free Render instances spin down after 15 minutes of inactivity. The first request after sleep takes ~30 seconds. The frontend app pings `/health` on load to pre-warm it.

### Step 7 — Get the Deploy Hook URL (for CI/CD)

In Render → your service → **Settings → Deploy Hook** → Copy URL.

Add it as a GitHub Secret named `RENDER_DEPLOY_HOOK_URL` (see GitHub Secrets section below).

### Step 8 — Update the frontend environment

Edit [frontend/src/environments/environment.prod.ts](frontend/src/environments/environment.prod.ts):

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://flight-delay-backend.onrender.com/api'
};
```

Then commit and push:

```bash
git add frontend/src/environments/environment.prod.ts
git commit -m "config: set Render backend URL in prod environment"
git push
```

---

## Deploy Frontend to Vercel

### Step 1 — Create a Vercel account

Go to [vercel.com](https://vercel.com), sign up, connect GitHub.

### Step 2 — Import project

1. Click **Add New → Project**
2. Import `flight-delay-guide`
3. Set **Root Directory** to `frontend`
4. Set **Framework Preset** to `Angular`
5. Set **Build Command** to `ng build --configuration=production`
6. Set **Output Directory** to `dist/flight-delay-frontend`

### Step 3 — Deploy

Click **Deploy**. Your frontend URL will be:
```
https://flight-delay-guide.vercel.app
```

### Step 4 — Update CORS on Render

Go back to Render → Environment → update `CORS_ALLOWED_ORIGINS` to your Vercel URL, then redeploy.

---

## GitHub Secrets Setup

Add these in GitHub → repository → **Settings → Secrets and variables → Actions → New repository secret**:

| Secret Name | Where to Get It |
|-------------|----------------|
| `GEMINI_API_KEY` | [aistudio.google.com](https://aistudio.google.com) → Get API Key |
| `AVIATIONSTACK_API_KEY` | [aviationstack.com](https://aviationstack.com) → Sign up, free tier |
| `RENDER_DEPLOY_HOOK_URL` | Render → service → Settings → Deploy Hook |
| `VERCEL_TOKEN` | Vercel → Account Settings → Tokens → Create |
| `VERCEL_ORG_ID` | Vercel → Settings → General → Team/Account ID |
| `VERCEL_PROJECT_ID` | Vercel → your project → Settings → General → Project ID |
| `CLAUDE_API_KEY` | [console.anthropic.com](https://console.anthropic.com) → API Keys (optional, only if switching AI provider) |

Once all secrets are added, push any backend change to trigger the full CI/CD pipeline.

---

## CI/CD Pipelines

| Workflow | Trigger | Jobs |
|----------|---------|------|
| `backend-ci-cd.yml` | Push to `backend/**` | Maven test → build JAR → trigger Render Docker deploy |
| `frontend-ci-cd.yml` | Push to `frontend/**` | npm ci → ng test → ng build → deploy to Vercel |
| `security-scan.yml` | Push to main + weekly | Gitleaks secret scan + npm/Maven dependency audit |

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/compensation/analyze` | Analyze a flight incident |
| `GET` | `/api/compensation/health` | Health check |
| `GET` | `/api/compensation/verify?flightNumber=X&date=Y` | Verify flight data |

### Sample request

```bash
curl -X POST https://flight-delay-backend.onrender.com/api/compensation/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "BA123",
    "flightDate": "2024-11-01",
    "airline": "British Airways",
    "departureAirport": "LHR",
    "arrivalAirport": "CDG",
    "departureCountry": "GB",
    "arrivalCountry": "FR",
    "incidentType": "DELAY",
    "delayDuration": 4
  }'
```
