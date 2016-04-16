(ns titan.controller
  (:require [schema.core :as s]
            [schema.coerce :as coerce]
            [titan.schema :refer [human-readable-error]]))

(defn convert-to-keyword
  [k]
  (if (= (type k) schema.core.OptionalKey)
    (first (vals k))
    k))

(defn- schema-keys
  "Grab the keys for this schema. Convert optional keys to ordinary keywords."
  [schema]
  (map convert-to-keyword (keys schema)))

(defn coerce
  [schema]
  (fn [data]
    (let [schema-keys (schema-keys schema)
          data (select-keys data schema-keys)]
      ((coerce/coercer
        schema
        coerce/string-coercion-matcher)
       data))))

(defn coerce-req
  [{:keys [status] :as req} field schema]
  (let [{:keys [error] :as coercion} ((coerce schema) (field req))]
    (if error
      (assoc req :error (human-readable-error error))
      (assoc req field coercion))))

(defn- reduce-coercion
  [{:keys [error] :as req} opts]
  (if error
    {:error error}
    (let [[field schema] (first opts)]
      (if field
        (if (next opts)
          (reduce-coercion (coerce-req req field schema) (next opts))
          (coerce-req req field schema))
        req))))

(defn wrap-type-coersion
  [f opts]
  (fn [req]
    (let [{:keys [error] :as req} (reduce-coercion req opts)]
      (if error
        {:status 400
         :body error}
        (f req)))))

(def controller-keys
  [:body
   :form-params
   :params
   :route-params
   :query-params])

(defmacro controller
  [args body opts]
  `(let [f# (fn ~args ~@body)
         opts# (select-keys ~opts controller-keys)]
     (wrap-type-coersion f# opts#)))

(defn- take-if
  [func form]
  (if (and (func (first form))
           (next form))
    [(first form) (next form)]
    [nil form]))

(defmacro defcontroller
  "Define a new controller.

  Controllers take additional arguments as metadata, which constructs
  parameter and output validation around them. For example:

  ```
  (defcontroller create!
    \"Description of the controller\"
    {:query-params 1
     :path-params 2}
    [request]
    {:status 200
     :body \"Okay!\"})
  ```"
  [name & body]
  (let [[docstring? body] (take-if string? body)
        [metadata body] (take-if map? body)
        [arglist body] [(first body) (next body)]]
    `(def ~(with-meta name (merge (meta name)
                                  (if docstring?
                                    (assoc metadata :doc docstring?)
                                    metadata)))
       (controller ~arglist ~body ~metadata))))
