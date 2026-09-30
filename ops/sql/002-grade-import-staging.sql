CREATE UNLOGGED TABLE IF NOT EXISTS account_grade_import_stage (
    grade_id BIGINT NOT NULL,
    grade_uuid VARCHAR(255) NOT NULL,
    grade_name VARCHAR(255) NOT NULL,
    min_point INTEGER NOT NULL,
    max_point INTEGER NOT NULL,
    description VARCHAR(255),
    order_number INTEGER NOT NULL,
    grade_image_key VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE UNLOGGED TABLE IF NOT EXISTS account_grade_history_import_stage (
    id BIGINT NOT NULL,
    member_uuid VARCHAR(50) NOT NULL,
    post_uuid VARCHAR(255) NOT NULL,
    post_type VARCHAR(32) NOT NULL,
    point DOUBLE PRECISION NOT NULL,
    total_point DOUBLE PRECISION NOT NULL,
    grade_uuid VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
