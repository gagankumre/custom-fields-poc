-- query to create conditional unique index on jsonb column keys
CREATE UNIQUE INDEX vendor_unique_constraint ON vendor (
    workspace_id,
    (custom_field->>'name'),
    (custom_field->'address'->>'email'),
    (custom_field->'bankDetail'->>'accountNumber')
)
WHERE
    custom_field->>'name' IS NOT NULL AND
    custom_field->'address'->>'email' IS NOT NULL AND
    custom_field->'bankDetail'->>'accountNumber' IS NOT NULL;


-- query to create gin index on jsonb column
CREATE INDEX idx_gin_custom_field ON vendor USING GIN (custom_field);


-- query to get the size of the indexes
SELECT
     i.relname AS indexname,
     pg_size_pretty(pg_relation_size(i.oid)) AS index_size
FROM
     pg_class t
JOIN
     pg_index ix ON t.oid = ix.indrelid
JOIN
     pg_class i ON i.oid = ix.indexrelid
WHERE
     t.relname = 'vendor';


-- query to get the size of the table
SELECT pg_size_pretty(pg_total_relation_size('vendor')) AS total_size;


-- get all indexes on a table
select *
from pg_indexes
where tablename = 'vendor'


-- test query to check if the index is being used
EXPLAIN
SELECT *
FROM vendor
WHERE workspace_id = 'test-workspace-1'
AND custom_field->'address'->>'email' = 'vendor@example.com'
AND custom_field->'bankDetail'->>'accountNumber' = '123456789';