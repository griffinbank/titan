--generate the filename with `date -u +"%Y%m%d%H%M%S"`
CREATE TABLE author (
  id       SERIAL  PRIMARY KEY,
  username TEXT    NOT NULL,
  password TEXT    NOT NULL
);
-- ;;
CREATE TABLE blog (
  id    SERIAL  PRIMARY KEY,
  title TEXT    NOT NULL
);
-- ;;
CREATE TABLE post (
  id        SERIAL  PRIMARY KEY,
  author_id INTEGER NOT NULL REFERENCES author(id) ON DELETE CASCADE,
  blog_id   INTEGER NOT NULL REFERENCES blog(id) ON DELETE CASCADE,
  content   TEXT    NOT NULL
);
