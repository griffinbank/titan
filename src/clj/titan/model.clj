(ns titan.model
  (:require [clojure.tools.logging :as log]
            [korma.core :as korma]))

(defn- create!
  [entity]
  (fn [params]
    (korma/insert entity (korma/values params))))

(defn- fetch
  [entity]
  (fn
    ([]
     (korma/select entity (korma/where {})))
    ([params]
     (korma/select entity (korma/where params)))))

(defn- fetch-one
  [entity]
  (fn
    ([]
     (first (korma/select entity (korma/where {}))))
    ([params]
     (first (korma/select entity (korma/where params))))))

(defn- update!
  [entity]
  (fn [id params]
    (korma/update entity (korma/where {:id id}) (korma/set-fields params))
    (first (korma/select entity (korma/where {:id id})))))

(defn- delete!
  [entity]
  (fn [params]
    (korma/delete entity (korma/where params))))

(defn intern-fns
  [entity schema]
  (let [n (:name entity)]
    (intern *ns*
            (with-meta
              (symbol (str "create-" n "!"))
              {:doc (format "Insert a new %s to the database." n)
               :arglists '([params])})
            (create! entity))
    (intern *ns* (symbol (str "fetch-" n)) (fetch entity))
    (intern *ns* (symbol (str "fetch-one-" n)) (fetch-one entity))
    (intern *ns* (symbol (str "update-" n "!")) (update! entity))
    (intern *ns* (symbol (str "delete-" n "!")) (delete! entity))))

(defmacro defmodel
  "Define basic database methods for the target entity. Also does
   schema coercion.

    * create-X!
    * fetch-X
    * fetch-one-X
    * update-X!
    * delete-X!"
  [entity schema]
  `(intern-fns ~entity ~schema))
