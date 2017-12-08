(ns {{name}}.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as json]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.nested-params :refer [wrap-nested-params]]
            [ring.middleware.params :as params]
            [ring.middleware.x-headers :refer [wrap-content-type-options
                                               wrap-frame-options
                                               wrap-xss-protection]]
            [titan.app :refer [defapp]]
            [titan.server :as server]))

(defroutes app-routes
  (GET "/demo" {:status 200
                :body {:message "ok!"}})

  (route/not-found {:status 404
                    :body {:error "boop"}}))

(def app
  (-> app-routes
      (wrap-content-type-options :nosniff)
      (wrap-frame-options :sameorigin)
      (wrap-xss-protection true {:mode :block})
      wrap-keyword-params
      json/wrap-json-params
      json/wrap-json-response
      params/wrap-params
      wrap-nested-params))

(defapp app)

(defn -main
  []
  (server/start-server!))
