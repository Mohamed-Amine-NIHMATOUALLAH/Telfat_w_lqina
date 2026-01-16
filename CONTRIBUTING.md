# CONTRIBUTING

Thank you for your interest in contributing to Telfat W Lqina! This is a short guide to contribute safely and productively.

## General Rules
- Fork the repository and work on feature branches: `feature/my-feature`.
- Open clear Pull Requests with a descriptive title and summary.
- Follow the project's coding and style conventions.
- PRs to `master` require a review and a green CI.

## Commits
- Use clear commit messages, preferably following Conventional Commits (e.g., `feat: add feature X`).

## Security
- NEVER COMMIT secrets (files like `database.properties`, `.env`, private keys, etc.).
- Copy `.env.example` to `src/main/resources/database.properties` and fill it with your local values.
- If you find a secret accidentally committed, contact the team immediately (or open a `security` issue) and follow `SECURITY_SETUP.md`.

## Tests
- Add unit tests for new features.
- Make sure `mvn test` passes locally.

## CI
- PRs trigger the `CI` workflow which builds and runs tests. Ensure everything passes before requesting a review.

Thank you!

