INSERT INTO author (name, password) VALUES
  ('Venantius', '$2a$10$hjTKyciiHPHutU4YAL8TK.wG6LD8L7Z0H.7jQmsXCmMK/A0/8XqqO'),
  ('Test User', '$2a$10$xNi.5prsrvR/c6Tk0BvTa.KDZxeMaCc.OhZYGFvziNbmdcS21rzRe');
--;;
INSERT INTO blog (title) VALUES ('venanti.us');
--;;
INSERT INTO post (author_id, blog_id, content) VALUES
  (1, 1, E'I\'m a sample blog post!');
