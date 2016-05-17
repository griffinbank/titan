CREATE TABLE app_user (
  id  SERIAL  PRIMARY KEY,
  name  TEXT  NOT NULL
);

INSERT INTO app_user (name) VALUES (
  'Venantius'
), (
  'Test User'
);
