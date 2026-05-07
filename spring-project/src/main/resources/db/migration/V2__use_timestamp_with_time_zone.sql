DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'order_history'
          AND column_name = 'order_date'
          AND data_type = 'timestamp without time zone'
    ) THEN
        ALTER TABLE order_history
            ALTER COLUMN order_date TYPE TIMESTAMP WITH TIME ZONE
            USING order_date AT TIME ZONE 'UTC';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'order_history'
          AND column_name = 'created_at'
          AND data_type = 'timestamp without time zone'
    ) THEN
        ALTER TABLE order_history
            ALTER COLUMN created_at TYPE TIMESTAMP WITH TIME ZONE
            USING created_at AT TIME ZONE 'UTC';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'order_history'
          AND column_name = 'updated_at'
          AND data_type = 'timestamp without time zone'
    ) THEN
        ALTER TABLE order_history
            ALTER COLUMN updated_at TYPE TIMESTAMP WITH TIME ZONE
            USING updated_at AT TIME ZONE 'UTC';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'order_details'
          AND column_name = 'created_at'
          AND data_type = 'timestamp without time zone'
    ) THEN
        ALTER TABLE order_details
            ALTER COLUMN created_at TYPE TIMESTAMP WITH TIME ZONE
            USING created_at AT TIME ZONE 'UTC';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'order_details'
          AND column_name = 'updated_at'
          AND data_type = 'timestamp without time zone'
    ) THEN
        ALTER TABLE order_details
            ALTER COLUMN updated_at TYPE TIMESTAMP WITH TIME ZONE
            USING updated_at AT TIME ZONE 'UTC';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'member_status'
          AND column_name = 'created_at'
          AND data_type = 'timestamp without time zone'
    ) THEN
        ALTER TABLE member_status
            ALTER COLUMN created_at TYPE TIMESTAMP WITH TIME ZONE
            USING created_at AT TIME ZONE 'UTC';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'member_status'
          AND column_name = 'updated_at'
          AND data_type = 'timestamp without time zone'
    ) THEN
        ALTER TABLE member_status
            ALTER COLUMN updated_at TYPE TIMESTAMP WITH TIME ZONE
            USING updated_at AT TIME ZONE 'UTC';
    END IF;
END $$;
