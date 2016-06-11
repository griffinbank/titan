(ns example.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [titan.app :refer [defapp]]
            [titan.server :as server]))

;; some very minimal routing.
(defroutes app-routes
  (GET "/" [] {:status 200 :body "<h1>Hello World!</h1>"})
  (route/not-found "Beep boop not found"))

;; `defapp` takes a routing body and stores it in the `titan.app/app` atom
(defapp app-routes)

;; define a main method. start-server will start a server using the handler
;; located at `titan.app/app` and then store the server details in `titan.server/server`
(defn -main
  []
  (server/start-server!))
