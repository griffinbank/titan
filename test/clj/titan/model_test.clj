(ns titan.model-test
  (:require [clojure.test :refer :all]
            [korma.core :as korma]
            [titan.test.fixtures :refer [use-db-fixtures]]
            [titan.model :refer [defmodel]]))

;; Set up connection to db, etc.
(use-db-fixtures)

;; Define Korma entity
(korma/defentity app-user
  (korma/table :app_user))

(defmodel app-user)

(deftest model-stuff-works
  (is (= (fetch-one-app-user)
         {:id 1
          :name "Venantius"})))

(deftest fetch-x-works
  (is (= (fetch-app-user)
         '({:id 1
            :name "Venantius"}
           {:id 2
            :name "Test User"}))))

(deftest create-x-works
  (let [user (create-app-user! {:name "New user"})]
    (is (= user
           {:id 3 :name "New user"}))
    (is (= (fetch-one-app-user {:name "New user"})
           {:id 3 :name "New user"}))))

;; Verify that the above db insertion transaction was rolled back and not
;; committed.
(deftest tests-are-wrapped-in-transaction-blocks
  (is (= (fetch-one-app-user {:name "New user"}) nil)))

(deftest update-x-works
  (let [user (update-app-user! 1 {:name "Bear"})]
    (is (= user
           {:id 1
            :name "Bear"}))))

(deftest delete-x-works
  (delete-app-user! {:name "Venantius"})
  (is (= (count (fetch-app-user))
         1)))
