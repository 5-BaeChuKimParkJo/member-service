# Account database migration

Run these scripts explicitly against the `cn_account` PostgreSQL database. The application does not execute them automatically.

## Schema

```bash
psql "$ACCOUNT_DATABASE_URL" -v ON_ERROR_STOP=1 -f ops/sql/001-account-grade-schema.sql
```

## Grade data

Export the legacy MySQL `grade` and `grade_history` tables to CSV with headers. Keep the columns in this order:

```text
grade_id,grade_uuid,grade_name,min_point,max_point,description,order_number,grade_image_key,created_at,updated_at
id,member_uuid,post_uuid,post_type,point,total_point,grade_uuid,created_at,updated_at
```

The legacy `post_type` may be either the ordinal values `0`/`1` or the names `PRODUCT`/`AUCTION`.

Prepare the staging tables, load the exported files, and run the checked import:

```bash
psql "$ACCOUNT_DATABASE_URL" -v ON_ERROR_STOP=1 -f ops/sql/002-grade-import-staging.sql
psql "$ACCOUNT_DATABASE_URL" -v ON_ERROR_STOP=1 \
  -c "\copy account_grade_import_stage FROM '/absolute/path/grade.csv' WITH (FORMAT csv, HEADER true)"
psql "$ACCOUNT_DATABASE_URL" -v ON_ERROR_STOP=1 \
  -c "\copy account_grade_history_import_stage FROM '/absolute/path/grade_history.csv' WITH (FORMAT csv, HEADER true)"
psql "$ACCOUNT_DATABASE_URL" -v ON_ERROR_STOP=1 -f ops/sql/003-import-grade-data.sql
```

The import preserves grade, member, post, and history identifiers. It stops when no grade rows are staged or when the source does not contain the order `5` grade covering the initial `100` points. Do not create replacement thresholds during migration.
