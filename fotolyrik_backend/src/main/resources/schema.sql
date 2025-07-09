-- Add tsvector column
ALTER TABLE full_text ADD COLUMN IF NOT EXISTS full_text_vector tsvector;

-- Create GIN index
CREATE INDEX IF NOT EXISTS idx_full_text_vector_gin
    ON full_text
        USING GIN (full_text_vector);

-- Define the function to update the full_text_vector
DO '
    BEGIN
        IF NOT EXISTS (
            SELECT 1
            FROM pg_proc
            WHERE proname = ''update_full_text_vector''
        ) THEN
            CREATE FUNCTION update_full_text_vector() RETURNS trigger AS $fn$
            BEGIN
                NEW.full_text_vector := to_tsvector(''german'', NEW.full_text);
                RETURN NEW;
            END;
            $fn$ LANGUAGE plpgsql;
        END IF;
    END;
';

-- Call trigger
DROP TRIGGER IF EXISTS trg_update_vector ON full_text;

CREATE TRIGGER trg_update_vector
    BEFORE INSERT OR UPDATE ON full_text
    FOR EACH ROW
EXECUTE FUNCTION update_full_text_vector();
