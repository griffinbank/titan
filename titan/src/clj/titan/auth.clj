(ns titan.auth
  (:import [org.mindrot.jbcrypt BCrypt]))

(defn hashpw
  "Hash a password using the OpenBSD bcrypt scheme."
  [password]
  (BCrypt/hashpw password (BCrypt/gensalt)))

(defn checkpw
  "Check that a plaintext password matches a previously hashed one."
  [candidate hashed]
  (BCrypt/checkpw candidate hashed))
