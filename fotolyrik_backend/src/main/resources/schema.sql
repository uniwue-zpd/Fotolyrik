-- Add tsvector column if it does not exist
ALTER TABLE full_text
    ADD COLUMN IF NOT EXISTS full_text_vector tsvector
        GENERATED ALWAYS AS (to_tsvector('german', full_text)) STORED;

-- Create GIN index
CREATE INDEX IF NOT EXISTS idx_full_text_vector_gin
    ON full_text
        USING GIN (full_text_vector);
