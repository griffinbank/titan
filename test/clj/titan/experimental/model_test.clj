(ns titan.experimental.model-test
  (:require [titan.model-test]
            [clojure.test :refer :all]
            [titan.experimental.model :as model]
            [titan.test.fixtures :refer [use-db-fixtures]]))

(use-db-fixtures)

(deftest create!-works
  (is (= @(model/create!
           titan.model-test/app-user {:name "Sample"})
         {:id 3 :name "Sample"})))

(deftest fetch-works
  (is (= @(model/fetch titan.model-test/app-user {:name "Venantius"})
         {:id 1 :name "Venantius"})))
