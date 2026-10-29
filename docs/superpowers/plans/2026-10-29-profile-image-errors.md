# Profile Image Errors Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans inline. Steps use checkbox syntax for tracking.

**Goal:** Correct profile image HTTP errors without changing member/auth APIs.
**Architecture:** A prioritized advice restricted to ProfileImageController maps known public errors and sanitizes failures. Exercise real Spring/controller/service/storage wiring.
**Tech Stack:** Java 17, Spring MVC, JUnit/MockMvc/H2, mocked external AmazonS3 client.
**Spec:** docs/superpowers/specs/2026-10-29-profile-image-errors.md

## Global Constraints

- Preserve known 400/404/413; real storage/server errors remain 500.
- Fixed code/message, no-store errors; successful image bytes/cache unchanged.
- Existing checkout/main, no new dependencies/local Docker/live fault injection.
- Full PostgreSQL transaction test in CI; only that test excluded locally.

## Review Focus

- Real advice scanning and all three image route aliases must select the new handler.
- Missing/invalid keys and invalid image metadata must be 400, never a storage read for a rejected key.
- Missing objects are 404; storage access/network/IO errors must remain generic 500.
- Exactly 10 MiB remains valid; larger reads return 413; errors must not inherit immutable caching.
- Member/auth response mapping and unset-profile penguins.svg selection must remain valid.

### Task 1: Reproduce, implement and verify

Files: new profile/adapter/in/web/ProfileImageExceptionHandler.java;
common/error/AccountErrorCode.java; new profile/ProfileImageHttpTest.java.
Interface: existing GET images?key=... and code/message JSON on failure.

- [x] Add actual Spring/MockMvc image tests using the real service/adapter and external S3 fixture only.
- [x] Observe wrong 500 responses before production edits.
- [x] Add scoped advice and 413 code; preserve fixed public messages/no-store.
- [x] Run all local Java checks/bootJar except the Docker-only transaction test;
  run the existing frontend default-profile tests and convention tool tests.
- [x] Commit and obtain one bounded independent diff review; fix material findings with RED→GREEN.

### Task 2: Publish and verify

- [x] Push and verify exact-SHA unfiltered CI/GHCR publication.
- [x] Confirm exact image, rollout/Argo health and automatic GitOps commit.
- [x] Live GET invalid key400/missing object404/existing profile image200 where available;
  verify anonymous customer default-profile display without mutations.
- [x] Record proof/limits, sync GitOps and remove only this task's scratch.

Evidence: local Java97, frontend existing default-profile6, tooling11; no review
findings. Full CI40887317311 success. Runtime0ccc995 ready/restarts0, rollout
complete, Argo Synced/Healthy7ea636d1babb4cc81bc2e0a1072d02941bda03ab. Live image
400/404/no-store and actual desktop/mobile default image passed. Existing uploaded
photo sample unavailable; no production data created. Full details in docs/profile-image-errors.md.
