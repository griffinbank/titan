(ns titan.experimental.model-test
  (:require [titan.model-test]
            [clojure.test :refer :all]
            [titan.experimental.model :as model]
            [titan.test.fixtures :refer [use-db-fixtures]]))

(use-db-fixtures)

;; TODO(@venantius): query modifiers
(deftest having-works)
(deftest group-works)
(deftest from-works)

;; TODO(@venantius): query types
(deftest intersect-works)

;; Utility functions

(deftest create!-works
  (is (= @(model/create!
           titan.model-test/author {:name "Sample"})
         {:id 3 :name "Sample"})))

(deftest fetch-works
  (is (= @(model/fetch titan.model-test/author {:name "Venantius"})
         (list {:id 1 :name "Venantius"}))))

(deftest fetch-composes
  (is (= @(-> (model/fetch titan.model-test/author)
              (model/where {:name "Venantius"}))
         (list {:id 1 :name "Venantius"}))))

(deftest variadic-composition-works
  (is (= @(-> (model/fetch titan.model-test/author)
              (model/fields :name))
         (list {:name "Venantius"} {:name "Test User"}))))

(deftest delete!-works
  @(model/delete! titan.model-test/author)
  (is (= @(model/fetch titan.model-test/author)
         (list))))

(deftest delete!-works-with-provided-parameters
  @(model/delete! titan.model-test/author {:name "Venantius"})
  (is (= @(model/fetch titan.model-test/author)
         (list {:id 2 :name "Test User"}))))
