(ns titan.model-test
  (:require [clojure.test :refer :all]
            [korma.core :as korma]
            [titan.auth :as auth]
            [titan.model :as model]
            [titan.test.fixtures :refer [use-db-fixtures]]))

(declare author blog post)

(korma/defentity author)
(korma/defentity blog)
(korma/defentity post
  (korma/belongs-to author)
  (korma/belongs-to blog))

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
  (let [res @(model/create!
              author {:name "Sample" :password hashed-pw})
        res-no-id (dissoc res :id)] ;; rolled back transactions still advance the index
    (is (= res-no-id
           {:name "Sample" :password hashed-pw}))))

(deftest fetch-works
  (is (= @(model/fetch author {:name "Venantius"})
         (list {:id 1
                :name "Venantius"
                :password "$2a$10$hjTKyciiHPHutU4YAL8TK.wG6LD8L7Z0H.7jQmsXCmMK/A0/8XqqO"}))))

(deftest fetch-composes
  (is (= @(-> (model/fetch author)
              (model/where {:name "Venantius"}))
         (list {:id 1
                :name "Venantius"
                :password "$2a$10$hjTKyciiHPHutU4YAL8TK.wG6LD8L7Z0H.7jQmsXCmMK/A0/8XqqO"}))))

(deftest variadic-composition-works
  (is (= @(-> (model/fetch  author)
              (model/fields :name))
         (list {:name "Venantius"} {:name "Test User"}))))

(deftest update-works
  (is (= @(model/update! author {:id 1} {:name "Bear"})
         1))
  (is (= @(-> (model/fetch-one author)
              (model/where {:name "Bear"})
              (model/fields :id :name))
         {:id 1 :name "Bear"})))

(deftest delete!-works
  @(model/delete! author)
  (is (= @(model/fetch author)
         (list))))

(deftest delete!-works-with-provided-parameters
  @(model/delete! author {:name "Venantius"})
  (is (= @(model/fetch author)
         (list {:id 2
                :name "Test User"
                :password "$2a$10$xNi.5prsrvR/c6Tk0BvTa.KDZxeMaCc.OhZYGFvziNbmdcS21rzRe"}))))
