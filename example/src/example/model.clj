(ns example.model
  (:require [korma.core :as korma]
            [schema.core :as s]
            [titan.model :refer [defmodel]]))

;; Standard Korma `defentity`
(korma/defentity app-user
  (korma/table :app_user))

;; Define the schema for our table. At the moment this is only really used for
;; controller schema coercion.
(def user
  {(s/optional-key :id) s/Int
   (s/optional-key :name) s/Str})

;; Declare a number of helper DB functions:
;; - `fetch-app-user`
;; - `fetch-one-app-user`
;; - `create-app-user!`
;; - `update-app-user!`
;; - `delete-app-user!`
(defmodel app-user)
