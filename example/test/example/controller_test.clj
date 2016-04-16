(ns example.controller-test
  (:require [clojure.test :refer :all]
            [example.controller :as controller]
            [titan.test.fixtures :refer [use-db-fixtures]]))

(use-db-fixtures)

;; This is what happens when the provided types are wrong.
(deftest create-user-controller-returns-400
  (is (= (controller/create-user {:body {}
                                  :params {:name 1}})
         {:status 400
          :body "Parameter validation failed: {:name (not (instance? java.lang.String 1))}"})))

(deftest create-user-controller-succeeds
  (is (= (controller/create-user {:body {}
                                  :params {:name "Alphalpha"}})
         {:status 200
          :body {:id 3
                 :name "Alphalpha"}})))
