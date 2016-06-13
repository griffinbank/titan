(ns titan.deprecated.model
  "Original Titan model logic. Currently deprecated."
  (:require [korma.core :as korma]))

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

(defn- intern-*ns*
  [name-str obj metadata]
  (intern *ns* (with-meta (symbol name-str) metadata) obj))

(defn- intern-delete!-fn
  [{:keys [name] :as entity}]
  (intern-*ns*
   (str "delete-" name "!")
   (delete! entity)
   {:doc (format "Delete all %s records from the database matching params." name)
    :arglists '([params])}))

(defn- intern-update!-fn
  [{:keys [name] :as entity}]
  (intern-*ns*
   (str "update-" name "!")
   (update! entity)
   {:doc (format "Update one %s record from the database with the provided id to the given params." name)
    :arglists '([id params])}))

(defn- intern-fetch-one-fn
  [{:keys [name] :as entity}]
  (intern-*ns*
   (str "fetch-one-" name)
   (fetch-one entity)
   {:doc (format "Fetch one %s record from the database matching the given params." name)
    :arglists '([params])}))

(defn- intern-fetch-fn
  [{:keys [name] :as entity}]
  (intern-*ns*
   (str "fetch-" name)
   (fetch entity)
   {:doc (format "Fetch all %s records from the database matching the given params." name)
    :arglists '([params])}))

(defn- intern-create!-fn
  [{:keys [name] :as entity}]
  (intern-*ns*
   (str "create-" name "!")
   (create! entity)
   {:doc (format "Insert a new %s record into the database." name)
    :arglists '([params])}))

(defn- intern-fns
  [{:keys [name] :as entity}]
  (intern-create!-fn entity)
  (intern-fetch-fn entity)
  (intern-fetch-one-fn entity)
  (intern-update!-fn entity)
  (intern-delete!-fn entity))

(defmacro defmodel
  "Define basic database methods for the target entity:
    * create-X!
    * fetch-X
    * fetch-one-X
    * update-X!
    * delete-X!"
  [entity]
  `(#'intern-fns ~entity))
