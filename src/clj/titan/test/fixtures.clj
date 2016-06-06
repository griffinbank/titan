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
  "Initializes the DB connection pool and runs any necessary migrations."
  [test-ns]
  (db/set-korma-db!)
  (test-ns)
  (migrations/migrate {:reporter migrations/silent-reporter}))

(defn- reset-on-finish
  "After the test has finished running, fully roll back the database and re-run
  all migrations."
  [test-fn]
  (test-fn)
  (migrations/rollback-all {:reporter migrations/silent-reporter})
  (migrations/migrate {:reporter migrations/silent-reporter}))

(defn use-db-fixtures
  "Use db fixtures for all tests in this namespace. Call at the top of your
  test ns. By default, will initialize the database connection pool, apply any
  new migrations, and then and run each test inside of a transaction block. The
  transaction is rolled back at the end of the test.

  If you don't want to run your tests inside of a transaction block, you can pass
  `{:no-transactions true}` to this. Titan will then roll back the test database
  (presumably dropping any tables) and re-run migrations after each test in the
  test namespace. This is considerably slower and should only be used if needed."
  ([]
   (use-fixtures :once setup-db)
   (use-fixtures :each transactional-db-fixtures))
  ([{:keys [no-transactions] :as opts}]
   (if no-transactions
     (do
       (use-fixtures :once setup-db)
       (use-fixtures :each reset-on-finish))
     (use-db-fixtures))))
