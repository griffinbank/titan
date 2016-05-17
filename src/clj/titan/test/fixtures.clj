(ns titan.test.fixtures
  "Clever ways of working with databases in tests."
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.test :refer [use-fixtures]]
            [korma.db :refer [rollback transaction]]
            [ragtime.jdbc :as rjdbc]
            [ragtime.repl :as repl]
            [titan.db :as db]
            [titan.db.migrations :as migrations]))

(defn- transactional-db-fixtures
  "Run the test inside of a transaction."
  [test-fn]
  (transaction
   (test-fn)
   (rollback)))

(defn- setup-db
  "Initializes the DB connection pool and runs migrations. Tears down the
  migrations after running the test namespace."
  [test-ns]
  (db/set-korma-db!)
  (migrations/migrate)
  (test-ns)
  (migrations/rollback-all))

(defn use-db-fixtures
  "Use db fixtures for all tests in this namespace. Call at the top of your
  test ns."
  []
  (use-fixtures :once setup-db)
  (use-fixtures :each transactional-db-fixtures))
