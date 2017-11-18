(ns titan.controller.parser
  (:require [schema.coerce :as coerce]
            [schema.core :as schema]))

(defn validate
  "Given a request map, run a validation on that request. If the validation
  succeeds, just returns the request. If it fails, throws an exception."
  [k s]
  (fn [r]
    ((schema/validator s) (k r))
    r))

(defmacro parser
  "Generate a parser with the associated validators. Example usage:

    (def create-user-parser
      (parser
       (validate :params user-schema))))"
  [& args]
  `(fn [r#] (-> r# ~@args)))
