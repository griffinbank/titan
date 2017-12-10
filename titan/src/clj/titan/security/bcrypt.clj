(ns titan.security.bcrypt
  (:import [org.mindrot.jbcrypt BCrypt]))

(defn encrypt-password
  "Perform BCrypt hash of password"
  [password]
  (BCrypt/hashpw password (BCrypt/gensalt)))

(defn valid-password?
  "Verify that candidate password matches the hashed bcrypted password"
  [candidate hashed]
  (BCrypt/checkpw candidate hashed))

