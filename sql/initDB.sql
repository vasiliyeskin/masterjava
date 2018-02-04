DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;
DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS city_seq;
DROP TABLE IF EXISTS projectToGroup;
DROP TABLE IF EXISTS groups;
DROP SEQUENCE IF EXISTS groups_seq;
DROP TYPE IF EXISTS groupType;
DROP TABLE IF EXISTS projects;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
CREATE TYPE groupType AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');

CREATE SEQUENCE user_seq START 100000;
CREATE SEQUENCE city_seq START 100000;
CREATE SEQUENCE groups_seq START 100000;


CREATE TABLE cities (
  id    INTEGER PRIMARY KEY DEFAULT nextval('city_seq'),
  citykey   TEXT NOT NULL,
  name  TEXT NOT NULL
);
CREATE UNIQUE INDEX key_indx ON cities(citykey);

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL
);
CREATE UNIQUE INDEX email_idx ON users (email);

CREATE TABLE groups (
  id          INTEGER PRIMARY KEY DEFAULT nextval('groups_seq'),
  type        groupType,
  name        TEXT NOT NULL
);

CREATE TABLE projects (
  id            SERIAL PRIMARY KEY,
  description   TEXT NOT NULL
);

CREATE TABLE projectToGroup(
  project_id INTEGER,
  group_id   INTEGER,
  FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
  FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE
)


