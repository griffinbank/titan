(ns titan.db.migrations
  "Database migrations."
  (:require [environ.core :as env]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))

(defn silent-reporter
  "A migration reporter that does nothing."
  [& args]
  nil)

(defn load-config []
  {:datastore  (jdbc/sql-database (env/env :database-url))
   :migrations (jdbc/load-resources "migrations")})

(defn migrate
  "Perform all unapplied migrations."
  ([] (repl/migrate (load-config)))
  ([opts] (repl/migrate (merge (load-config) opts))))

(defn rollback
  "Roll back the last migration applied."
  ([] (repl/rollback (load-config)))
  ([opts] (repl/rollback (merge (load-config) opts))))

(defn rollback-all
  "Roll back the last Integer/MAX_VALUE migrations."
  ([] (repl/rollback (load-config) Integer/MAX_VALUE))
  ([opts] (repl/rollback (merge (load-config) opts) Integer/MAX_VALUE)))
