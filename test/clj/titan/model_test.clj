(ns titan.model-test
  (:require [clojure.test :refer :all]
            [korma.core :as korma]
            [titan.test.fixtures :refer [use-db-fixtures]]
            [titan.model :refer [defmodel]]))

;; Set up connection to db, etc.
(use-db-fixtures {:no-transactions true})

;; Define Korma entities
(declare author blog post)

(korma/defentity author)
(korma/defentity blog)
(korma/defentity post
  (korma/belongs-to author)
  (korma/belongs-to blog))

(defmodel author)

(deftest model-stuff-works
  (is (= (fetch-one-author)
         {:id 1
          :name "Venantius"})))

(deftest fetch-x-works
  (is (= (fetch-author)
         '({:id 1
            :name "Venantius"}
           {:id 2
            :name "Test User"}))))

(deftest create-x-works
  (let [user (create-author! {:name "New user"})]
    (is (= user
           {:id 3 :name "New user"}))
    (is (= (fetch-one-author {:name "New user"})
           {:id 3 :name "New user"}))))

;; Verify that the above db insertion transaction was rolled back and not
;; committed.
(deftest tests-are-wrapped-in-transaction-blocks
  (is (= (fetch-one-author {:name "New user"}) nil)))

(deftest update-x-works
  (let [user (update-author! 1 {:name "Bear"})]
    (is (= user
           {:id 1
            :name "Bear"}))))

(deftest delete-x-works
  (delete-author! {:name "Venantius"})
  (is (= (count (fetch-author))
         1)))
