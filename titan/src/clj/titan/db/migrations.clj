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
  (let [db (env/env :database-url)]
    (when-not db
      (throw (ex-info "Migration failed: Titan could not find a :database-url in the project map or a DATABASE_URL environment variable." {})))
    {:datastore  (jdbc/sql-database (env/env :database-url))
     :migrations (jdbc/load-resources "migrations")}))

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

(defn reapply-all
  "Roll back all migrations and then re-apply them. Good for reverting a data
  store to a known clean state."
  ([]
   (rollback-all)
   (migrate))
  ([opts]
   (rollback-all opts)
   (migrate opts)))
