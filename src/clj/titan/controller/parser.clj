(ns titan.controller.parser
  (:require [schema.coerce :as coerce]
            [schema.core :as schema]))

;; Should include both validation and coercion logic.


;; validation logic starts here

'blah

;; coercion logic starts here

(defn json-coercer
  "Given a Schema, returns a function that will parse a ring request's `:params`
  into the types corresponding to the schema."
  [schema]
  (coerce/coercer schema coerce/json-coercion-matcher))

(defn validate
  "Given a request map, run a validation on that request. If the validation
  succeeds, just returns the request. If it fails, throws an exception."
  [k s]
  (fn [r]
    ((schema/validator s) (k r))
    r))

(defmacro parser
  [& args]
  `(fn [r#] (-> r# ~@args)))

;; two possible options:

'(controller
  {:parser parser
   :presenter presenter})

'(parser
  (validate :params schema)
  (coerce :path-params :schema))
