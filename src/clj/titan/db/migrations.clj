(ns titan.db.migrations
  "Database migrations."
  (:require [environ.core :as env]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))

(defn load-config []
  {:datastore  (jdbc/sql-database (env/env :database-url))
   :migrations (jdbc/load-resources "migrations")})

(defn migrate
  ([] (repl/migrate (load-config)))
  ([opts] (repl/migrate opts)))

(defn rollback
  ([] (repl/rollback (load-config)))
  ([opts] (repl/rollback opts)))

(defn rollback-all
  "Roll back the last Integer/MAX_VALUE migrations."
  ([] (repl/rollback (load-config) Integer/MAX_VALUE))
  ([opts] (repl/rollback opts Integer/MAX_VALUE)))
