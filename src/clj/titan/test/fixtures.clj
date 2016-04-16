(ns titan.test.fixtures
  "Clever ways of working with databases in tests."
  (:require [clojure.test :refer [use-fixtures]]
            [korma.db :refer [rollback transaction]]
            [titan.db :as db]
            [titan.db.migrations :as migrations]))

(defn -db-fixtures
  "Migrates the database, and inserts fixture data into the database before
   each test."
  [test-fn]
  (transaction
   (migrations/migrate)
   (test-fn)
   (rollback)))

(defn -initialize-db-connection-fixture
  "Initializes the DB connection for Korma."
  [test-ns]
  (db/set-korma-db)
  (test-ns))

(defn use-db-fixtures
  "Use db fixtures for all tests in this namespace. Call at the top of your
  test ns."
  []
  (use-fixtures :once -initialize-db-connection-fixture)
  (use-fixtures :each -db-fixtures))
