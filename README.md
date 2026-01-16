# 🏆 Telfat W Lqina - Lost & Found Management System

<div align="center">

[![License](https://img.shields.io/badge/license-MIT-brightgreen)](./LICENSE)
[![Java](https://img.shields.io/badge/Java-24-orange?logo=openjdk)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-FF4081)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-9.5.0-4479A1?logo=mysql)](https://www.mysql.com/)
[![Hibernate](https://img.shields.io/badge/Hibernate-6.4.4-59666C?logo=hibernate)](https://hibernate.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?logo=apachemaven)](https://maven.apache.org/)

**Modern desktop application for managing lost and found items at major sporting events**

[🎯 Overview](#-overview) • [✨ Features](#-features) • [🚀 Quick Start](#-quick-start) • [📖 Documentation](#-documentation)

</div>
---

## 📋 Table of Contents

- [🎯 Overview](#-overview)
- [✨ Features](#-features)
- [Architecture](#architecture)
- [🏗️ Architecture](#️-architecture)
- [⚙️ Prerequisites](#️-prerequisites)
- [Database Configuration](#database-configuration)
- [🗄️ Database Configuration](#️-database-configuration)
- [🛡️ Security](#-security)
- [🛡️ Security](#️-security)
- [👥 Team](#-team)
- [📄 License](#-license)

---

## 🎯 Overview

**Telfat W Lqina** (CAN 2025) is a JavaFX desktop application designed to digitize, secure, and optimize the management of lost and found items in stadiums and sports venues during major events (e.g., CAN 2025, World Cup 2030).

### Key objectives
### 🎯 Key Objectives

- **Automate** the lost and found management process
- **Reduce** return time to rightful owners
- **Ensure** traceability and audit trails for all operations
- **Provide** dashboards and statistical reports for operational decision-making

### 👥 Target Audience

- **Administrators**: Account management, site configuration, supervision, and reporting
- **Field Agents**: Registration of found items, claim processing, and item return
### Target users
### 🌟 Why This Project?

During major sporting events, thousands of items are lost daily. Traditional paper-based systems are slow, error-prone, and lack traceability. **Telfat W Lqina** provides a modern, secure, and efficient solution to this challenge.
---

## ✨ Features

- Role-based authentication (ADMIN / AGENT)
### 🔐 Security & Authentication

- Automatic creation of initial administrator account (on first run)
- Automatic creation of initial administrator account (if not present)
- Password hashing using BCrypt
- Access controls on sensitive operations

### 📦 Object Management

- Detailed registration of found items (description, photos, location, date)
- Advanced multi-criteria search (category, date, site, keywords)
- Image support (JPG/PNG) with optimized storage
- Categorization and tagging for faster matching
- Complete claim and return workflow

### 🏟️ Site Management (Stadiums)

- Creation/editing of stadiums and drop-off zones
- Agent assignment by site
- Site-specific parameters (hours, procedures)

### 📊 Statistics & Reporting

- Role-based dashboards (Admin / Agent)
- Charts: returned vs. pending items, average return time
- Report export (PDF, Excel, CSV)
- Filters by period, site, item type

### 🔄 Complete Workflow

1. Agent reports a found item
2. Registration in database (with photos)
3. Owner searches via interface
4. Agent validates/identifies
5. Item return and transaction archiving
---

## Architecture
## 🏗️ Architecture
Tech stack:
### 🔧 Tech Stack

```yaml
Frontend:
  - JavaFX 21.0.6 (FXML + CSS)
  - MVC pattern controllers

Backend:
  - Java 24
  - Hibernate ORM 6.4.4
  - Jakarta Persistence API 3.1.0
  - Service layer and DAOs

Database:
  - MySQL Connector/J 9.5.0 (driver)
  - HikariCP 5.1.0 (connection pooling)
  - utf8mb4_unicode_ci schema (recommended)
Project structure (high level):
Tools:
  - Maven 3.6+ (build and plugins)
  - SLF4J (logging)
  - JUnit (testing)
```

### 🗂️ Package Structure

com.firstproject.telfat_w_lqina/
├── controllers/  # JavaFX Controllers
├── controllers/         # JavaFX Controllers
├── models/              # JPA Entities (@Entity)
├── service/             # Business Logic
├── dao/                 # Data Access Layer (Repository)
├── util/                # Utilities (HibernateUtil, Security...)
└── MainApp.java         # Entry Point
resources/
├── fxml/         # Views
├── fxml/                # Views
├── css/                 # Stylesheets
├── images/              # Assets
└── database.properties  # Local config (NOT committed)

---

## ⚙️ Prerequisites

- JDK 24 installed and JAVA_HOME configured
### ✅ Quick Checklist
Verify environment (PowerShell):
- [ ] JDK 24 installed with `JAVA_HOME` configured
- [ ] Maven 3.6+ installed
- [ ] MySQL (or compatible DBMS) available
- [ ] IDE (IntelliJ IDEA recommended)

### 🔧 Verification (PowerShell/Terminal)
```powershell
java -version
# Check Java
mvn -v

# Check Maven
git --version

# Check Git
```

---

## 🚀 Quick Start

1) Clone the repository
### 1️⃣ Clone the Repository
```powershell
```bash
cd Telfat_w_lqina
```

2) Configure database
### 2️⃣ Configure Database
Read `SECURITY_SETUP.md` first.
**⚠️ IMPORTANT: Read [SECURITY_SETUP.md](./SECURITY_SETUP.md) first!**
```powershell
Copy-Item .env.example src/main/resources/database.properties
# Copy template to create local configuration
notepad src/main/resources/database.properties

# Edit with your credentials
```

Example configuration (development):
**Example configuration (MySQL with auto-creation):**
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/telfat_w_lqina?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=YourSecurePassword
hibernate.dialect=org.hibernate.dialect.MySQLDialect
hibernate.hbm2ddl.auto=create
hibernate.show_sql=true
hibernate.format_sql=true
```

> Note: After the first successful run, change `hibernate.hbm2ddl.auto=create` to `update` to preserve data.
> **Note**: After first successful run, change `hibernate.hbm2ddl.auto=create` to `update`
3) Build and run
### 3️⃣ Build & Run
```powershell
mvn clean package
# Clean and compile
mvn javafx:run

# Run via JavaFX plugin
# or

# OR run JAR directly (if executable)
java -jar target/telfat_w_lqina-1.0-SNAPSHOT.jar

If you use IntelliJ: open the project, import Maven, set SDK to Java 24, and run `com.firstproject.telfat_w_lqina.Launcher` or `MainApp`.
**Using IntelliJ IDEA:**
1. Open project
2. Reload Maven dependencies
3. Configure SDK (Java 24)
4. Run `com.firstproject.telfat_w_lqina.Launcher` or `MainApp`
---

## Database Configuration
## 🗄️ Database Configuration

### Recommended Method: Auto-Creation (Development)

The application automatically creates the database and tables on first run with proper configuration:
Recommended development method: auto-creation with `createDatabaseIfNotExist=true` and `hibernate.hbm2ddl.auto=create` on first run.
```properties
db.url=jdbc:mysql://localhost:3306/telfat_w_lqina?createDatabaseIfNotExist=true...
hibernate.hbm2ddl.auto=create
```
Manual SQL initialization (optional):
Check logs for successful creation:
```
>>> INIT() STARTED - Application initialization...
>>> Hibernate loaded successfully
>>> Database initialized
>>> Admin account created: admin / admin123
```

### Manual Method: SQL Script
```sql
CREATE DATABASE IF NOT EXISTS telfat_w_lqina 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'telfat_app'@'localhost' 
  IDENTIFIED BY 'SecurePassword123!';

GRANT ALL PRIVILEGES ON telfat_w_lqina.* 
  TO 'telfat_app'@'localhost';

FLUSH PRIVILEGES;
```

Production notes: use environment variables or a secret manager for credentials and set `hibernate.hbm2ddl.auto=validate`.
### Production Configuration

```properties
# Use environment variables for secrets
db.username=${DB_USER}
db.password=${DB_PASSWORD}
db.url=jdbc:mysql://prod-db.example.com:3306/telfat_w_lqina_prod?useSSL=true&requireSSL=true&serverTimezone=UTC
hibernate.hbm2ddl.auto=validate
hibernate.show_sql=false
```
---

## 💻 Usage

First login after initial setup:
### First Login

1. Launch application: `mvn javafx:run`
2. Default credentials (change immediately!):
    - **Username**: `Admin`
    - **Password**: `Admin`
3. Change admin password in settings
Agent workflow summary:
### Agent Workflow

1. Register found item → Add photos and metadata
2. Inform potential owner
3. Process claim
4. Return item and archive transaction
---

## 🛡️ Security

Best practices implemented:
### Best Practices Implemented

- ✅ **Password hashing** with BCrypt
- ✅ **Role-based access control** (RBAC)
- ✅ **Connection pooling** with HikariCP
- ✅ **Prepared statements** (SQL injection prevention)
- ✅ **Secure configuration** management (not committed to Git)

### Security Configuration

**NEVER commit these files:**
- `src/main/resources/database.properties`
- `.env` files
- Any files containing credentials

See [SECURITY_SETUP.md](./SECURITY_SETUP.md) for:
- Credential rotation procedures
- Git history cleanup (if leaked)
- Production security recommendations

---

## 👥 Team

| Member | Role | Contact |
|--------|------|---------|
| Mohamed Amine Nihmatouallah |  Developer | [LinkedIn](https://ma.linkedin.com/in/mohamed-amine-nihmatouallah) |
| Yassine Atiki | Developer | [LinkedIn](https://ma.linkedin.com/in/yassine-atiki-b8a815332) |

---

## ❓ FAQ

**Q: How to reset the database?**

A: Set `hibernate.hbm2ddl.auto=create`, restart the application, then change back to `update`.

**Q: Application won't start - basic troubleshooting?**

A:
1. Verify Java: `java -version`
2. Clean build: `mvn clean compile`
3. Check `database.properties` configuration
4. Run: `mvn javafx:run`

**Q: Can I use PostgreSQL instead of MySQL?**

A: Yes! Update `database.properties`:
```properties
db.driver=org.postgresql.Driver
db.url=jdbc:postgresql://localhost:5432/telfat_w_lqina
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```
Never commit `src/main/resources/database.properties` or any file that contains secrets. Use environment variables or a secret manager in production.
---

## 🛠️ Troubleshooting

### Common Errors

| Error | Solution |
|-------|----------|
| `Connection refused` | Start MySQL service |
| `Driver not found` | Run `mvn clean install` |
| `Unknown database` | Add `?createDatabaseIfNotExist=true` to URL |
| `JavaFX not found` | Run via `mvn javafx:run` or configure module-path |

### Diagnostic Commands

```powershell
# Clean build
mvn clean
Remove-Item -Recurse -Force target/

# Check dependencies
mvn dependency:tree

# Run with verbose logging
mvn javafx:run -X
```
---

## 🤝 Contributing

Please read `CONTRIBUTING.md` for contribution guidelines.
We welcome contributions! Please see [CONTRIBUTING.md](./CONTRIBUTING.md) for guidelines.
Quick steps:
**Quick Start:**
1. Fork the repository
2. Create feature branch: `git checkout -b feature/my-feature`
3. Commit changes: `git commit -m 'feat: add amazing feature'`
4. Push to branch: `git push origin feature/my-feature`
5. Open Pull Request
---

## 📄 License

This project is licensed under the MIT License — see the `LICENSE` file for details.
This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
---

## 📞 Support
## 🌟 Acknowledgments
If you find a bug or security issue, open an issue and label it appropriately (e.g., `security`). For leaked secrets, follow the steps in `SECURITY_SETUP.md`.
- **CAN 2025** event organizers for the use case
- **OpenJDK** and **JavaFX** communities
- All contributors and testers
---

*If you want, I can also produce a clean French translation (`README_FR.md`) or a shorter README for the GitHub repository front page.*
<div align="center">

## 🏆 Ready for CAN 2025!

**Telfat W Lqina** - Professional Lost & Found Management for Major Events

[⬆️ Back to Top](#-telfat-w-lqina---lost--found-management-system)

</div>

---


