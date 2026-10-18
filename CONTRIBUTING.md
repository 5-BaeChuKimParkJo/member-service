# Contributing

Use `type(scope): imperative subject`, no gitmoji, maximum 100 characters.
Install the shared hook: `python scripts/install_git_hooks.py` (Python 3.10+).
Test it: `python -m unittest discover -s scripts -p test_check_conventions.py`.
The authoritative rule set and fixtures are in `.conventions/`; GitOps holds
the full engineering guide in `docs/engineering/conventions.md`.

This checkout publishes account-service. Keep runtime names and DB/API identities
stable. Business features use domain/application/adapter; inner layers cannot
import database, HTTP, messaging or storage implementations. Java classes use
PascalCase and methods use camelCase. Run `./gradlew clean test bootJar` with
Java 17 and Docker available for transaction integration tests.
Never commit credentials or generated sources.
