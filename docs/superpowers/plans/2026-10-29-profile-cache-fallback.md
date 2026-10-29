# Profile Cache Fallback Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan inline. Steps use checkbox syntax for tracking.

**Goal:** Keep member profile reads available when the profile Redis cache fails.
**Architecture:** Wire a scoped CacheErrorHandler through CachingConfigurer. Preserve the normal application/JPA query and all other error semantics.
**Tech Stack:** Java17, Spring Boot3.4/Spring Cache, Redis, JPA/H2 and CI PostgreSQL.
**Spec:** docs/superpowers/specs/2026-10-29-profile-cache-fallback.md

## Global Constraints

- Only member:v2 get/put errors are best effort. Other caches and evict/clear throw.
- No auth/token/verification fallback, schema/TTL/timeout changes or new dependency.
- Existing checkout/main; no local Docker and no production failure injection.
- Logs contain only cache operation and exception class, not keys/values/raw messages.

## Review Focus

- A cache read failure can be followed by a cache write failure; both must preserve a successful DB result.
- Global handler registration must not swallow other cache or invalidation errors.
- A repository failure or absent member must not become a fabricated successful profile.
- Recovery must resume cache population/hits rather than permanently bypass Redis.
- Production configuration wiring and log privacy need behavioral coverage.

### Task 1: Implement and verify scoped fallback

Files: new common/config/MemberCacheConfig.java, member/adapter/out/cache/MemberProfileCacheErrorHandler.java, member/MemberProfileCacheFallbackTest.java and member/MemberProfileCacheErrorHandlerTest.java.
Interface: CacheErrorHandler get/put for MemberCacheNames.MEMBER; normal getMember HTTP response remains unchanged.

- [x] Add real Spring proxy/MockMvc/H2 tests for cache failures and recovery; observe failures before implementation.
- [x] Register the scoped handler and sanitized warnings without changing business methods.
- [x] Add allowlist/invalidation/log-privacy tests and DB/absent-member regression assertions.
- [x] Run targeted tests then all local tests except the existing Docker-only PostgreSQL test, plus ArchUnit/Spotless/bootJar.
- [x] Perform one independent review and fix material findings; commit the bounded code and documentation.

### Task 2: Publish and verify

- [x] Push main and inspect full CI including the existing real PostgreSQL transaction test and GHCR build.
- [x] Confirm automatic GitOps tag, actual new account-service image/rollout and Argo health.
- [x] Read-only browser smoke: anonymous customer auction detail and seller lookup remain usable.
- [x] Record actual evidence/limits, sync GitOps and leave checkouts clean.
