INSERT INTO roles (ID,name) VALUES (1,'user');
INSERT INTO users (ID, username, password, email) VALUES (1, 'testUser', '$2a$10$7q7.duaRXXmHyk/jmIImLuBsTtY9qol5YCR.BSgAECMJNC1mtOdGe', 'whitei.v.sky@gmail.com');
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO `groups` (ID, groupname) VALUES (1, 'testGroup');

