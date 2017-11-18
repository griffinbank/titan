(ns titan.util.token
  "Convenience functions for dealing with tokens."
  (:require [clj-time.coerce :as c]
            [clj-time.core :as t]
            [crypto.random :as random]))

(defn generate-token
  "Generate a random, base32-encoded token."
  []
  (random/base32 35))

(defn valid-token?
  "Is this token still valid (i.e. has it not expired yet)?"
  [token]
  (t/after? (c/from-sql-time (:expires_at token)) (t/now)))
