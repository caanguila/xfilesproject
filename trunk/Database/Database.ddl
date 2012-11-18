ALTER TABLE Users DROP CONSTRAINT FKUsers864709;
ALTER TABLE Messages DROP CONSTRAINT FKUser_Messages;
ALTER TABLE Messages DROP CONSTRAINT FKMessagesTypes;
ALTER TABLE Groups DROP CONSTRAINT FKGroups485329;
ALTER TABLE Users_Groups DROP CONSTRAINT FKUsers_Grou503233;
ALTER TABLE Users_Groups DROP CONSTRAINT FKUsers_Grou640674;
ALTER TABLE Users_Passwords DROP CONSTRAINT Passwords;
ALTER TABLE Files DROP CONSTRAINT FKFiles9125;
ALTER TABLE Users DROP CONSTRAINT FKUsers864710;
ALTER TABLE Messages DROP CONSTRAINT FKMessages341025;
ALTER TABLE Groups DROP CONSTRAINT FKGroups485330;
ALTER TABLE Files DROP CONSTRAINT FKFiles91587;
ALTER TABLE Files DROP CONSTRAINT FKUser_Files;
ALTER TABLE User_Files DROP CONSTRAINT FKUser_Files575887;
ALTER TABLE User_Files DROP CONSTRAINT FKUser_Files865345;
ALTER TABLE User_Session DROP CONSTRAINT FKUser_Sessi406376;
ALTER TABLE Files DROP CONSTRAINT FKFiles770503;
ALTER TABLE Files_Passwords DROP CONSTRAINT FKFiles_Passwords575887; 
ALTER TABLE Files_Passwords DROP CONSTRAINT FKFiles_Passwords865345; 
ALTER TABLE Files DROP CONSTRAINT FKFilesTypes;
ALTER TABLE Logs DROP CONSTRAINT FKLogs733636;
ALTER TABLE Logs DROP CONSTRAINT FKLogs865980;
ALTER TABLE Files_Groups DROP CONSTRAINT FKFiles_Grou569633;
ALTER TABLE Files_Groups DROP CONSTRAINT FKFiles_Grou941164;
ALTER TABLE Messages DROP CONSTRAINT FKMessages902981;
DROP TABLE Users;
DROP TABLE Messages;
DROP TABLE Groups;
DROP TABLE Users_Groups;
DROP TABLE Users_Passwords;
DROP TABLE Files;
DROP TABLE User_Files;
DROP TABLE User_Session;
DROP TABLE EncryptionFiles;
DROP TABLE Password_Storage;
DROP TABLE Files_Passwords;
DROP TABLE Logs;
DROP TABLE Action_types;
DROP TABLE Files_Groups;
DROP TABLE Types;

CREATE TABLE Users (
  user_id         BIGSERIAL NOT NULL, 
  name           varchar(255) NOT NULL, 
  surname        varchar(255) NOT NULL, 
  date_creation  date NOT NULL, 
  date_suspended date, 
  email          varchar(255) NOT NULL, 
  information    varchar(2000), 
  photo          bytea, 
  type_id        int8 , 
  PRIMARY KEY (user_id));

CREATE TABLE Types (
  type_id      BIGSERIAL NOT NULL, 
  category    varchar(255) NOT NULL, 
  name        varchar(255) NOT NULL, 
  description varchar(1000), 
  options     varchar(2000), 
  PRIMARY KEY (type_id));

CREATE TABLE Messages (
  message_id     BIGSERIAL NOT NULL, 
  date_send     date, 
  group_id      int8, 
  type_id       int8, 
  sender_id     int8, 
  recipient_id  int8, 
  message       varchar(5000), 
  date_recieved date, 
  PRIMARY KEY (message_id));

CREATE TABLE Groups (
  group_id     BIGSERIAL NOT NULL, 
  name        varchar(500) NOT NULL, 
  Description varchar(5000), 
  type_id     int8, 
  PRIMARY KEY (group_id));

CREATE TABLE Users_Groups (
  Users_user_id   int8 NOT NULL, 
  Groups_group_id int8 NOT NULL, 
  PRIMARY KEY (Users_user_id, 
  Groups_group_id));

CREATE TABLE Users_Passwords (
  user_id  int8 NOT NULL, 
  password varchar(255) NOT NULL, 
  login    varchar(255) NOT NULL UNIQUE, 
  PRIMARY KEY (user_id));

CREATE TABLE Files (
  file_id          BIGSERIAL NOT NULL, 
  name            varchar(1000) NOT NULL, 
  description     varchar(1000), 
  content_type    varchar(255) NOT NULL, 
  file_size       int8 NOT NULL, 
  crc             int8 NOT NULL, 
  en_file_id      int8, 
  parent_id       int8, 
  type_id         int8, 
  created_by      int8, 
  isFolder        bool NOT NULL, 
  enc_type_id	  int8,
  PRIMARY KEY (file_id));

CREATE TABLE User_Files (
  Users_user_id int8 NOT NULL, 
  Files_file_id int8 NOT NULL, 
  PRIMARY KEY (Users_user_id, 
  Files_file_id));
CREATE TABLE Files_Passwords (
  Files_file_id int8 NOT NULL, 
  Password_Storage_id int8 NOT NULL,   
  PRIMARY KEY (Password_Storage_id, 
  Files_file_id));

CREATE TABLE User_Session (
  session_id  BIGSERIAL NOT NULL, 
  user_id    int8, 
  ip_adress  varchar(255), 
  brouser    varchar(255), 
  session_name	  varchar(500),
  PRIMARY KEY (session_id));

CREATE TABLE EncryptionFiles (
  en_file_id  BIGSERIAL NOT NULL, 
  path       varchar(2000) NOT NULL, 
  attributes varchar(255), 
  PRIMARY KEY (en_file_id));

CREATE TABLE Password_Storage(
  password_storage_id BIGSERIAL NOT NULL,
  user_id         int8, 
  password        varchar(500),
  options		  varchar(500), 
  PRIMARY KEY (password_storage_id));

CREATE TABLE Logs (
  log_id          BIGSERIAL NOT NULL, 
  user_id        int8, 
  type_action_id int8, 
  ip_adress      varchar(255), 
  options        varchar(255),
  date_creation date,  
  session_name   varchar(300), 
  message        varchar(1000), 
  PRIMARY KEY (log_id));

CREATE TABLE Action_types (
  Action_type_id  BIGSERIAL NOT NULL, 
  name           varchar(255) NOT NULL, 
  description    varchar(2000), 
  PRIMARY KEY (Action_type_id));

CREATE TABLE Files_Groups (
  Filesfile_id   int8 NOT NULL, 
  Groupsgroup_id int8 NOT NULL, 
  PRIMARY KEY (Filesfile_id, 
  Groupsgroup_id));

ALTER TABLE Users ADD CONSTRAINT FKUsers864709 FOREIGN KEY (type_id) REFERENCES Types (type_id);
ALTER TABLE Messages ADD CONSTRAINT FKUser_Messages FOREIGN KEY (sender_id) REFERENCES Users (user_id);
ALTER TABLE Messages ADD CONSTRAINT FKMessagesTypes FOREIGN KEY (type_id) REFERENCES Types (type_id);
ALTER TABLE Groups ADD CONSTRAINT FKGroups485329 FOREIGN KEY (type_id) REFERENCES Types (type_id);
ALTER TABLE Users_Groups ADD CONSTRAINT FKUsers_Grou503233 FOREIGN KEY (Users_user_id) REFERENCES Users (user_id);
ALTER TABLE Users_Groups ADD CONSTRAINT FKUsers_Grou640674 FOREIGN KEY (Groups_group_id) REFERENCES Groups (group_id);
ALTER TABLE Users_Passwords ADD CONSTRAINT Passwords FOREIGN KEY (user_id) REFERENCES Users (user_id);
ALTER TABLE Files ADD CONSTRAINT FKFiles9125 FOREIGN KEY (type_id) REFERENCES Types (type_id);
ALTER TABLE Users ADD CONSTRAINT FKUsers864710 FOREIGN KEY (type_id) REFERENCES Types (type_id);
ALTER TABLE Messages ADD CONSTRAINT FKMessages341025 FOREIGN KEY (type_id) REFERENCES Types (type_id);
ALTER TABLE Groups ADD CONSTRAINT FKGroups485330 FOREIGN KEY (type_id) REFERENCES Types (type_id);
ALTER TABLE Files ADD CONSTRAINT FKFiles91587 FOREIGN KEY (parent_id) REFERENCES Files (file_id);
ALTER TABLE Files ADD CONSTRAINT FKUser_Files FOREIGN KEY (created_by) REFERENCES Users (user_id);
ALTER TABLE User_Files ADD CONSTRAINT FKUser_Files575887 FOREIGN KEY (Users_user_id) REFERENCES Users (user_id);
ALTER TABLE User_Files ADD CONSTRAINT FKUser_Files865345 FOREIGN KEY (Files_file_id) REFERENCES Files (file_id);
ALTER TABLE Files_Passwords ADD CONSTRAINT FKFiles_Passwords575887 FOREIGN KEY (Password_Storage_id) REFERENCES Password_Storage (password_storage_id);
ALTER TABLE Files_Passwords ADD CONSTRAINT FKFiles_Passwords865345 FOREIGN KEY (Files_file_id) REFERENCES Files (file_id);
ALTER TABLE Files ADD CONSTRAINT FKFilesTypes FOREIGN KEY (enc_type_id) REFERENCES Types (type_id);
ALTER TABLE User_Session ADD CONSTRAINT FKUser_Sessi406376 FOREIGN KEY (user_id) REFERENCES Users (user_id);
ALTER TABLE Files ADD CONSTRAINT FKFiles770503 FOREIGN KEY (en_file_id) REFERENCES EncryptionFiles (en_file_id);
ALTER TABLE Logs ADD CONSTRAINT FKLogs733636 FOREIGN KEY (type_action_id) REFERENCES Action_types (Action_type_id);
ALTER TABLE Logs ADD CONSTRAINT FKLogs865980 FOREIGN KEY (user_id) REFERENCES Users (user_id);
ALTER TABLE Files_Groups ADD CONSTRAINT FKFiles_Grou569633 FOREIGN KEY (Filesfile_id) REFERENCES Files (file_id);
ALTER TABLE Files_Groups ADD CONSTRAINT FKFiles_Grou941164 FOREIGN KEY (Groupsgroup_id) REFERENCES Groups (group_id);
ALTER TABLE Messages ADD CONSTRAINT FKMessages902981 FOREIGN KEY (recipient_id) REFERENCES Users (user_id);

