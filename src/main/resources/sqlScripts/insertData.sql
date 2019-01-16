MERGE INTO roles (ID,name) VALUES (1,'user');
MERGE INTO users (ID, username, password, email) VALUES (1, 'testUser', '$2a$10$7q7.duaRXXmHyk/jmIImLuBsTtY9qol5YCR.BSgAECMJNC1mtOdGe', 'whitei.v.sky@gmail.com');
MERGE INTO `groups` (ID, groupname) VALUES (1, 'testGroup');
MERGE INTO users_roles key(user_id, role_id) VALUES (1, 1);
