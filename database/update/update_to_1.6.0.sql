ALTER TABLE dpa_user_dvel.customer_user ADD validate_access tinyint(1);

UPDATE dpa_user_dvel.customer_user SET validate_access=0 WHERE customer_user_id = 184