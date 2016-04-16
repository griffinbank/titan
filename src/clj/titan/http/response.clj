(ns titan.http.response
  (:require [ring.util.response :as resp]))

(def redirect resp/redirect)

(defn ok
  [body]
  {:status 200
   :body body})

(defn bad-request
  [message]
  {:status 400
   :body {:message message}})

(defn unauthorized
  [message]
  {:status 401
   :headers {"Content-Type" "text/html"}
   :body message})

(defn not-found
  [message]
  {:status 404
   :body {:message message}})

(defn method-not-allowed
  [message]
  {:status 405
   :body {:message message}})

(defn conflict
  [message]
  {:status 409
   :body {:message message}})

(defn server-error
  [message]
  {:status 500
   :body {:message message}})
