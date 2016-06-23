(ns titan.controller.parser-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [titan.controller.parser :as parser]))

(def author-schema
  {:id s/Int
   :name s/Str})

(def valid-author
  {:id 1
   :name "Venantius"})

(def invalid-author
  (dissoc valid-author :id))

(deftest validate-works
  (let [v (parser/validate :params author-schema)]
    (try
      (v {:params {:name "herp"}})
      (catch Exception e (= (:error (ex-data e)))
             '{:id missing-required-key}))))

(deftest parser-works
  (let [v1 (parser/validate :params (dissoc author-schema :id))
        v2 (parser/validate :params author-schema)
        req {:params {:name "Venantius"}}
        p (parser/parser
           v1
           v2)]
    (try (p req)
         (catch Exception e (= (:error (ex-data e)))
                '{:id missing-required-key}))))
