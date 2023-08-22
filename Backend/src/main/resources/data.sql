INSERT INTO schema_manager_role (id_schema_manager_role, name, description) VALUES (1,'ADMIN', 'This user role has access to everything within DBSchemaManager including passwords.');
INSERT INTO schema_manager_role (id_schema_manager_role, name, description) VALUES (2,'USER', 'This user role has access to every functionality but cant see passwords.');
INSERT INTO schema_manager_role (id_schema_manager_role, name, description) VALUES (3,'VISITOR', 'This user can only consult information about. It is not allowed to send queries.');

INSERT INTO schema_manager_user (id_schema_manager_user, username, password, role_id ) VALUES (1,'gcmoreira','',1);
INSERT INTO schema_manager_user (id_schema_manager_user, username, password, role_id ) VALUES (2,'fetcher_service','$2a$10$9vIku9LAPDQwYj7e2mZr0uMchjYPYragl2cOwOF1p5jNN.GekV9Di',1);
