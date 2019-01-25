MERGE INTO roles (ID,name) VALUES (1,'ROLE_USER');
MERGE INTO roles (ID,name) VALUES (2,'ROLE_ADMIN');
MERGE INTO users (ID, username, password, email) VALUES (1, 'testUser', '$2a$10$7q7.duaRXXmHyk/jmIImLuBsTtY9qol5YCR.BSgAECMJNC1mtOdGe', 'whitei.v.sky@gmail.com');
MERGE INTO users (ID, username, password, email) VALUES (2, 'root', '$2a$10$/rPLdy/DWvvUbtuGDsbZUu7ZXtfhbCSInvbOHXI4bYXPvd/lE75Qy', 'whitei.v.sky2@gmail.com');

MERGE INTO users_roles key(user_id, role_id) VALUES (1, 1);
MERGE INTO users_roles key(user_id, role_id) VALUES (2, 2);

MERGE INTO `groups` (ID, groupname) VALUES (1, 'testGroup');
