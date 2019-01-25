CREATE TABLE IF NOT EXISTS users
(
  ID       int auto_increment
    primary key,
  username varchar(255) not null,
  password varchar(255) not null,
  email    varchar(100) not null,
  constraint users_email_uindex
    unique (email),
  constraint users_username_uindex
    unique (username)
);

CREATE TABLE IF NOT EXISTS roles
(
  ID   int auto_increment
    primary key,
  name varchar(100) not null,
  constraint roles_name_uindex
    unique (name)
);

CREATE TABLE IF NOT EXISTS `groups`
(
  ID        int auto_increment
    primary key,
  groupname varchar(60) null,
  constraint groups_groupname_uindex
    unique (groupname)
);

CREATE TABLE IF NOT EXISTS attachments
(
  ID          int auto_increment
    primary key,
  filename    varchar(255) null,
  filedata    longblob         null,
  description varchar(255) null,
  uploaddate  datetime     not null,
  owner_id    int          not null,
  size        long         null,
  mediatype   varchar(255)  null,
  constraint attachments_users_ID_fk
    foreign key (owner_id) references users (id)
);

CREATE TABLE IF NOT EXISTS attachments_groups
(
  attach_id int not null,
  group_id  int not null,
  constraint attachments_groups_group_ID_fk
    foreign key (group_id) references `groups` (id),
  constraint attachments_groups_attach_ID_fk
    foreign key (attach_id) references attachments (id)
);

CREATE TABLE IF NOT EXISTS users_roles
(
  user_id int not null,
  role_id int not null,
  constraint user_id
    unique (user_id, role_id),
  constraint users_roles_roles_ID_fk
    foreign key (role_id) references roles (id),
  constraint users_roles_users_ID_fk
    foreign key (user_id) references users (id)
);

CREATE TABLE IF NOT EXISTS users_groups
(
  user_id  int not null,
  group_id int not null,
  constraint users_groups_acces_group_ID_fk
    foreign key (group_id) references `groups` (id),
  constraint users_groups_users_id_fk
    foreign key (user_id) references users (id)
);
CREATE TABLE IF NOT EXISTS fileformats
(
  id   int auto_increment primary key,
  name varchar(10) not null,
  icon CLOB(max)    not null,
  constraint fileFormats_id_uindex
    unique (id),
  constraint fileFormats_name_uindex
    unique (name)
);
