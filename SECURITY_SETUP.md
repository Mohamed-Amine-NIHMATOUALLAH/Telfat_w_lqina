# Secure Database Configuration

## ⚠️ IMPORTANT - Initial Configuration

This project uses a `database.properties` file to store database connection information. This file MUST NOT be committed to Git because it contains sensitive information.

This document explains how to configure the file locally, how to remove it from Git tracking, and what to do if credentials are leaked.

---

## 📋 Windows (PowerShell) Configuration Steps

1. Copy the template `.env.example` to `src/main/resources/database.properties`:

```powershell
Copy-Item .env.example src/main/resources/database.properties
notepad src/main/resources/database.properties
```

2. Fill `db.username` and `db.password` with your local values.
3. Verify the file is ignored by Git (check `.gitignore`):

```powershell
git status --porcelain
```

The file `src/main/resources/database.properties` should not appear in the output. If it does, follow the steps below to remove it from tracking.

---

## 🔁 Remove the file from Git tracking (without deleting it locally)

```powershell
# On the main branch
git checkout master
git pull origin master
# Remove from tracking but keep file locally
git rm --cached src/main/resources/database.properties
# Ensure .gitignore contains the entry
if (-not (Select-String -Path .gitignore -Pattern "src/main/resources/database.properties" -Quiet)) {
    Add-Content .gitignore "src/main/resources/database.properties"
}

git add .gitignore
git commit -m "Remove sensitive database.properties from repo and ignore it"
git push origin master
```

---

## 🚨 If credentials were pushed (leak) — urgent actions

1. ROTATE / CHANGE the credentials immediately on your provider (Railway, AWS RDS, etc.).
2. Inform the team: repository history cleanup is required.

Methods to purge history (coordination required, destructive):

### A) Recommended: git-filter-repo (fast and reliable)

```bash
pip install git-filter-repo
# Mirror clone
git clone --mirror https://github.com/<OWNER>/<REPO>.git
cd <REPO>.git
# Remove the file from history
git-filter-repo --path src/main/resources/database.properties --invert-paths
# Force push (coordinate with team)
git push --force --all
git push --force --tags
```

### B) Alternative: BFG Repo-Cleaner (Windows-friendly)

```bash
# Download bfg.jar
git clone --mirror https://github.com/<OWNER>/<REPO>.git
cd <REPO>.git
java -jar path\to\bfg.jar --delete-files src/main/resources/database.properties
git reflog expire --expire=now --all
git gc --prune=now --aggressive
git push --force --all
git push --force --tags
```

> Note: These operations rewrite history. Coordinate with the team and ask everyone to re-clone after the purge.

---

## 🔐 Security Recommendations

- Use a secret manager (HashiCorp Vault, AWS Secrets Manager) for production secrets.
- Store environment variables in the hosting platform (Railway/Heroku/GCP) and never in code.
- Limit access to people who need it.
- Configure periodic rotation of passwords (30/90 days depending on policy).
- Enable two-factor authentication (2FA) on GitHub accounts.

---

## 📝 Quick Steps for New Developers

1. Clone the project
2. Copy `.env.example` to `src/main/resources/database.properties`
3. Request credentials from the project lead through a secure channel
4. Configure your local database

---

## 📞 Support

If you detect a leak or need help, open an issue labeled `security` and notify the team immediately.

