(ns titan.model-test
  (:require [titan.deprecated.model-test :as db]
            [clojure.test :refer :all]
            [titan.auth :as auth]
            [titan.model :as model]
            [titan.test.fixtures :refer [use-db-fixtures]]))

(use-db-fixtures)

;; TODO(@venantius): query modifiers
(deftest having-works)
(deftest group-works)
(deftest from-works)

;; TODO(@venantius): query types
(deftest intersect-works)

;; Utility functions

(def password "sample-pw")
(def hashed-pw (auth/hashpw password))

(deftest create!-works
  (is (= @(model/create!
           db/author {:name "Sample" :password hashed-pw})
         {:id 3 :name "Sample" :password hashed-pw})))

(deftest fetch-works
  (is (= @(model/fetch db/author {:name "Venantius"})
         (list {:id 1
                :name "Venantius"
                :password "$2a$10$hjTKyciiHPHutU4YAL8TK.wG6LD8L7Z0H.7jQmsXCmMK/A0/8XqqO"}))))

(deftest fetch-composes
  (is (= @(-> (model/fetch db/author)
              (model/where {:name "Venantius"}))
         (list {:id 1
                :name "Venantius"
                :password "$2a$10$hjTKyciiHPHutU4YAL8TK.wG6LD8L7Z0H.7jQmsXCmMK/A0/8XqqO"}))))

(deftest variadic-composition-works
  (is (= @(-> (model/fetch db/author)
              (model/fields :name))
         (list {:name "Venantius"} {:name "Test User"}))))

(deftest update-works
  (is (= @(model/update! db/author {:id 1} {:name "Bear"})
         1))
  (is (= @(-> (model/fetch-one db/author)
              (model/where {:name "Bear"})
              (model/fields :id :name))
         {:id 1 :name "Bear"})))

(deftest delete!-works
  @(model/delete! db/author)
  (is (= @(model/fetch db/author)
         (list))))

(deftest delete!-works-with-provided-parameters
  @(model/delete! db/author {:name "Venantius"})
  (is (= @(model/fetch db/author)
         (list {:id 2
                :name "Test User"
                :password "$2a$10$xNi.5prsrvR/c6Tk0BvTa.KDZxeMaCc.OhZYGFvziNbmdcS21rzRe"}))))
