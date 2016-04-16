(ns titan.controller-test
  (:require [bond.james :as bond]
            [clojure.test :refer :all]
            [schema.core :as s]
            [titan.controller :refer [defcontroller]]
            [titan.schema :refer [human-readable-error]]))

(defcontroller sample-controller
  "Docstring"
  {:params {:name s/Str
            :nested {:id s/Int
                     :trait s/Str}
            :enum [{:herp s/Num}]}}
  [req]
  {:status 200
   :body req})

(defcontroller multi-parameter-typing
  {:params {:param_id s/Int}
   :query-params {:query_id s/Int}}
  [req]
  {:status 200
   :body "Okay!"})

(deftest sample-controller-returns-400
  (is (= (sample-controller {:body {}
                             :params {:name 1}})
         {:status 400
          :body "Parameter validation failed: {:name (not (instance? java.lang.String 1)), :nested missing-required-key, :enum missing-required-key}"})))

(deftest sample-controller-correct-parses
  (is (= (sample-controller {:body {}
                             :params {:name "name"
                                      :nested {:id "4" :trait "thing"}
                                      :enum []}})
         {:status 200
          :body {:body {}
                 :params {:name "name"
                          :nested {:id 4 :trait "thing"}
                          :enum []}}})))

(deftest multi-param-typing-checks-all-set-metadata
  (is (= {:status 400
          :body (human-readable-error {:query_id '(not (integer? ast))})}
         (multi-parameter-typing {:params {:param_id "1"}
                                  :query-params {:query_id "ast"}})))
  (is (= {:status 400
          :body (human-readable-error {:param_id '(not (integer? asdf))})}

         (multi-parameter-typing {:params {:param_id "asdf"}
                                  :query-params {:query_id "ast"}}))))

(deftest multi-param-typing-terminates-early
  (testing "coerce-req is only called once"
    (bond/with-spy [titan.controller/coerce-req]
      (multi-parameter-typing {:params {:param_id "asdf"}
                               :query-params {:query_id "ast"}})
      (is (= (-> titan.controller/coerce-req
                 bond/calls
                 count)
             1)))))

(deftest multi-param-typing-works
  (is (= {:status 200
          :body "Okay!"}
         (multi-parameter-typing {:params {:param_id "1"}
                                  :query-params {:query_id "2"}}))))

