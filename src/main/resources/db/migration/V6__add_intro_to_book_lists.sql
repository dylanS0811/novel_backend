-- Older MySQL versions (prior to 8.0.29) do not support the
-- `IF NOT EXISTS` clause when adding columns. The earlier version
-- of this migration used it and caused a failure, leaving a record in
-- the Flyway schema history table. To keep the migration compatible
-- and allow it to run after a repair, simply add the column without
-- the conditional clause.
ALTER TABLE book_lists ADD COLUMN intro TEXT;
