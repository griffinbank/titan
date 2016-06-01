INSERT INTO author (name) VALUES (
  'Venantius'
), (
  'Test User'
);

INSERT INTO blog (title) VALUES (
  'venanti.us'
);

INSERT INTO post (author_id, blog_id, content) VALUES (
  1,
  1,
  E'I\'m a sample blog post!'
);
