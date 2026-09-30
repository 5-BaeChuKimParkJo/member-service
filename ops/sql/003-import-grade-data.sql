BEGIN;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM account_grade_import_stage) THEN
        RAISE EXCEPTION 'No source grade rows were staged; refusing to invent grade thresholds';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM account_grade_import_stage
        WHERE order_number = 5
          AND min_point <= 100
          AND max_point >= 100
    ) THEN
        RAISE EXCEPTION 'Source data has no order 5 grade containing the initial 100 points';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM account_grade_history_import_stage
        WHERE upper(post_type) NOT IN ('0', '1', 'PRODUCT', 'AUCTION')
    ) THEN
        RAISE EXCEPTION 'Source grade history contains an unsupported post_type';
    END IF;
END $$;

INSERT INTO grade (
    grade_id,
    grade_uuid,
    grade_name,
    min_point,
    max_point,
    description,
    order_number,
    grade_image_key,
    created_at,
    updated_at
)
SELECT
    grade_id,
    grade_uuid,
    grade_name,
    min_point,
    max_point,
    description,
    order_number,
    grade_image_key,
    created_at,
    updated_at
FROM account_grade_import_stage
ON CONFLICT (grade_uuid) DO UPDATE SET
    grade_name = EXCLUDED.grade_name,
    min_point = EXCLUDED.min_point,
    max_point = EXCLUDED.max_point,
    description = EXCLUDED.description,
    order_number = EXCLUDED.order_number,
    grade_image_key = EXCLUDED.grade_image_key,
    created_at = EXCLUDED.created_at,
    updated_at = EXCLUDED.updated_at;

INSERT INTO grade_history (
    id,
    member_uuid,
    post_uuid,
    post_type,
    point,
    total_point,
    grade_uuid,
    created_at,
    updated_at
)
SELECT
    id,
    member_uuid,
    post_uuid,
    CASE upper(post_type)
        WHEN '0' THEN 'PRODUCT'
        WHEN 'PRODUCT' THEN 'PRODUCT'
        WHEN '1' THEN 'AUCTION'
        WHEN 'AUCTION' THEN 'AUCTION'
    END,
    point,
    total_point,
    grade_uuid,
    created_at,
    updated_at
FROM account_grade_history_import_stage
ON CONFLICT (id) DO UPDATE SET
    member_uuid = EXCLUDED.member_uuid,
    post_uuid = EXCLUDED.post_uuid,
    post_type = EXCLUDED.post_type,
    point = EXCLUDED.point,
    total_point = EXCLUDED.total_point,
    grade_uuid = EXCLUDED.grade_uuid,
    created_at = EXCLUDED.created_at,
    updated_at = EXCLUDED.updated_at;

SELECT setval(
    pg_get_serial_sequence('grade', 'grade_id'),
    GREATEST((SELECT COALESCE(MAX(grade_id), 1) FROM grade), 1),
    true
);
SELECT setval(
    pg_get_serial_sequence('grade_history', 'id'),
    GREATEST((SELECT COALESCE(MAX(id), 1) FROM grade_history), 1),
    true
);

TRUNCATE account_grade_import_stage;
TRUNCATE account_grade_history_import_stage;

COMMIT;
