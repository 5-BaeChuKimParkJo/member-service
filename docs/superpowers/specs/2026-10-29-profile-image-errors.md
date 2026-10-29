# Profile image HTTP errors

The user approved preserving image request/not-found statuses, retaining real
storage/server failures as 500 and checking the existing default profile display.
Use the existing checkout and authorized commit/push/GitOps deployment workflow.

## Design

The real chain is ProfileImageController → ProfileImageService →
MinioProfileImageAdapter. The controller and adapter throw ResponseStatusException
for invalid keys/types (400), missing objects (404), oversized reads (413) and
upstream/IO failures (502). The common RuntimeException handler currently turns
all of them into 500. Scope a prioritized exception advice to the image-read
controller only. Preserve the known 400/404/413 outcomes; map adapter upstream
502 and unexpected storage failures to the agreed public 500. Handle a missing
required key as 400 within that same controller boundary.

Use existing code/message response fields and fixed public messages. Add code
413 to the common error enum. Never echo exception reasons, storage hosts, keys
or credentials. Error responses are no-store; successful bytes/content type and
the existing one-year immutable cache policy stay as-is.

A global ResponseStatusException passthrough would affect unrelated APIs and
expose upstream statuses/reasons. Rewriting storage/application errors is not
needed to correct this HTTP boundary.

No DB/business/storage config, auth policy, key regex, upload/signing changes,
frontend source changes or new dependencies. No local Docker, production uploads,
object deletion or outage injection. Test the actual application/advice wiring,
service and MinIO adapter with only the external S3 client replaced. The existing
default image applies to unset profile URLs; repairing arbitrary broken URLs or
all framework error handling is outside this bounded correction.
