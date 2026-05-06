# Secrets & Deployment Quick Reference

## Where Secrets Live

| Environment   | Where Keys Go                    | File Location              |
|---------------|----------------------------------|----------------------------|
| Local Dev     | .env file (gitignored)           | flight-delay-guide/.env    |
| Render (prod) | Render Dashboard → Environment   | NOT in any file            |
| GitHub CI/CD  | GitHub Secrets                   | NOT in any file            |
| Vercel        | Not needed (URL in env.prod.ts)  | NOT in any file            |

## Secret Names Reference

| Secret Name             | Used By               | Where to Get               |
|-------------------------|-----------------------|----------------------------|
| GEMINI_API_KEY          | Backend + Render + GH | aistudio.google.com        |
| CLAUDE_API_KEY          | Backend + Render + GH | console.anthropic.com      |
| AVIATIONSTACK_API_KEY   | Backend + Render + GH | aviationstack.com          |
| CORS_ALLOWED_ORIGINS    | Backend + Render      | Your Vercel URL            |
| RENDER_DEPLOY_HOOK_URL  | GitHub Actions only   | Render Dashboard           |
| VERCEL_TOKEN            | GitHub Actions only   | Vercel Dashboard           |
| VERCEL_ORG_ID           | GitHub Actions only   | Vercel → Settings → General |
| VERCEL_PROJECT_ID       | GitHub Actions only   | Vercel → Project → Settings |

## Render Environment Variables (set in Render Dashboard)

| Key                    | Value                                  |
|------------------------|----------------------------------------|
| GEMINI_API_KEY         | Your Gemini key                        |
| AVIATIONSTACK_API_KEY  | Your AviationStack key                 |
| AI_PROVIDER            | gemini                                 |
| JAVA_OPTS              | -Xmx450m -Xms200m -XX:+UseG1GC        |
| CORS_ALLOWED_ORIGINS   | https://your-app.vercel.app            |

## Deployment Order

1. Push code to GitHub (no secrets)
2. Deploy backend to Render → note Render URL (`https://flight-delay-guide.onrender.com`)
3. Update `frontend/src/environments/environment.prod.ts` with Render URL
4. Deploy frontend to Vercel → note Vercel URL
5. Update `CORS_ALLOWED_ORIGINS` in Render with Vercel URL → Save → redeploy
6. Add GitHub Secrets for CI/CD automation

## Common Commands

```bash
# Local development
cd backend && mvn spring-boot:run
cd frontend && ng serve            # proxy auto-routes /api → localhost:8080

# Build verification
cd backend && mvn clean package -DskipTests
cd frontend && ng build --configuration=production

# Security check
git grep -r "AIza" -- "*.yml" "*.ts" "*.java"
git ls-files | grep "\.env"

# Deploy (Render + Vercel auto-deploy on push)
git add . && git commit -m "..." && git push
```

## What's Safe to Commit

```
application.yml          (uses ${VAR:placeholder} syntax)
environment.ts           (points to localhost)
environment.prod.ts      (points to Render URL — not a secret)
.env.example             (template with no real values)
vercel.json              (config only, no keys)
Dockerfile               (build instructions, no keys)
*.java, *.ts, *.html, *.scss source files
pom.xml, package.json
```

## What to NEVER Commit

```
.env                     (real keys)
application-local.yml    (real keys)
Any file with AIza...    (Gemini key pattern)
Any file with sk-ant-... (Claude key pattern)
```

## Free Tier Limits

| Service       | Limit                          |
|---------------|--------------------------------|
| Gemini AI     | 1,500 requests/day             |
| AviationStack | 100 requests/month             |
| Render        | 750 hours/month, sleeps after 15 min inactivity |
| Vercel        | 100 GB bandwidth/month         |
