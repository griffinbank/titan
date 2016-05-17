(ns example.controller
  (:require [example.model :as model]
            [titan.controller :refer [defcontroller]]
            [debugger.core]))

;; Fetch all users. Note that although we're using the `defcontroller` macro,
;; we're not actually doing any type casting.
(defcontroller get-users
  "Get all users."
  [request]
  {:status 200
   :body (model/fetch-app-user)})

;; Create a user. Note that here we are using the `defmacro` for its type
;; casting abilities. See the tests in `example.controller-test` for an idea of
;; how this works.
(defcontroller create-user
  "Create a user."
  {:params model/user}
  [request]
  {:status 200
   :body (model/create-app-user! (:params request))})
