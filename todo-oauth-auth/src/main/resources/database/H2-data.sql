/* Resource Owner Records. password=demo */
INSERT INTO users(username, password, enabled) VALUES('demo', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', true);
INSERT INTO users(username, password, enabled) VALUES('demo2', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', true);
INSERT INTO authorities(username, authority) VALUES('demo', 'READ');
INSERT INTO authorities(username, authority) VALUES('demo2', 'READ');

/* Client Records. */
INSERT INTO clients(client_id, client_secret, client_name, access_token_validity, refresh_token_validity) VALUES('testClient', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 'hogehoge', 10, 20);
INSERT INTO clients(client_id, client_secret, client_name, access_token_validity, refresh_token_validity) VALUES('testClient2', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 'hogehoge', 10, 20);

INSERT INTO scopes(client_id, scope) VALUES('testClient', 'READ');
INSERT INTO scopes(client_id, scope) VALUES('testClient', 'WRITE');
INSERT INTO scopes(client_id, scope) VALUES('testClient2', 'READ');
INSERT INTO scopes(client_id, scope) VALUES('testClient2', 'WRITE');

INSERT INTO resource_ids(client_id, resource_id) VALUES('testClient', 'todoResource');
INSERT INTO resource_ids(client_id, resource_id) VALUES('testClient2', 'todoResource');

INSERT INTO authorized_grant_types(client_id, authorized_grant_type) VALUES('testClient', 'authorization_code');
INSERT INTO authorized_grant_types(client_id, authorized_grant_type) VALUES('testClient', 'password');
INSERT INTO authorized_grant_types(client_id, authorized_grant_type) VALUES('testClient', 'client_credentials');
INSERT INTO authorized_grant_types(client_id, authorized_grant_type) VALUES('testClient', 'refresh_token');
INSERT INTO authorized_grant_types(client_id, authorized_grant_type) VALUES('testClient', 'implicit');
INSERT INTO authorized_grant_types(client_id, authorized_grant_type) VALUES('testClient2', 'authorization_code');
INSERT INTO authorized_grant_types(client_id, authorized_grant_type) VALUES('testClient2', 'password');
INSERT INTO authorized_grant_types(client_id, authorized_grant_type) VALUES('testClient2', 'client_credentials');
INSERT INTO authorized_grant_types(client_id, authorized_grant_type) VALUES('testClient2', 'refresh_token');
INSERT INTO authorized_grant_types(client_id, authorized_grant_type) VALUES('testClient2', 'implicit');

INSERT INTO web_server_redirect_uris(client_id, web_server_redirect_uri) VALUES('testClient', 'http://localhost:8080/todo-oauth-client/');
INSERT INTO web_server_redirect_uris(client_id, web_server_redirect_uri) VALUES('testClient2', 'http://localhost:8080/todo-oauth-client/');

COMMIT;