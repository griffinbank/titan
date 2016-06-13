(ns titan.deprecated.model-test
  (:require [clojure.test :refer :all]
            [korma.core :as korma]
            [titan.auth :as auth]
            [titan.test.fixtures :refer [use-db-fixtures]]
            [titan.deprecated.model :refer [defmodel]]))

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
          :name "Venantius"
          :password "$2a$10$hjTKyciiHPHutU4YAL8TK.wG6LD8L7Z0H.7jQmsXCmMK/A0/8XqqO"})))

(deftest fetch-x-works
  (is (= (fetch-author)
         '({:id 1
            :name "Venantius"
            :password "$2a$10$hjTKyciiHPHutU4YAL8TK.wG6LD8L7Z0H.7jQmsXCmMK/A0/8XqqO"}
           {:id 2
            :name "Test User"
            :password "$2a$10$xNi.5prsrvR/c6Tk0BvTa.KDZxeMaCc.OhZYGFvziNbmdcS21rzRe"}))))

(deftest create-x-works
  (let [pw (auth/hashpw "dummypw")
        user (create-author! {:name "New user" :password pw})]
    (is (= user
           {:id 3 :name "New user" :password pw}))
    (is (= (fetch-one-author {:name "New user"})
           {:id 3 :name "New user" :password pw}))))

;; Verify that the above db insertion transaction was rolled back and not
;; committed.
(deftest tests-are-wrapped-in-transaction-blocks
  (is (= (fetch-one-author {:name "New user"}) nil)))

(deftest update-x-works
  (let [user (update-author! 1 {:name "Bear"})]
    (is (= user
           {:id 1
            :name "Bear"
            :password "$2a$10$hjTKyciiHPHutU4YAL8TK.wG6LD8L7Z0H.7jQmsXCmMK/A0/8XqqO"}))))

(deftest delete-x-works
  (delete-author! {:name "Venantius"})
  (is (= (count (fetch-author))
         1)))
