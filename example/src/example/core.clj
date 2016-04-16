(ns example.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [example.controller :as controller]
            [ring.middleware.json :refer [wrap-json-response]]
            [titan.app :refer [defapp]]
            [titan.server :as server]))

;; some very minimal routing.
(defroutes app-routes
  (wrap-json-response (GET "/" [] controller/get-users))
  (route/not-found "Beep boop not found"))

;; `defapp` takes a routing body and stores it in the `titan.app/app` atom
(defapp app-routes)

;; define a main method. start-server will start a server using the handler
;; located at `titan.app/app` and then store the server details in `titan.server/server`
(defn -main
  []
  (server/start-server!))
