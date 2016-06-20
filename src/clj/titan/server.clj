(ns titan.server
  (:require [immutant.web :as web]
            [clojure.tools.logging :as log]
            [environ.core :as env]
            [ring.middleware.reload :refer [wrap-reload]]
            [titan.app :as app]
            [titan.db :as db]
            [titan.server.nrepl :as nrepl]))

(defonce server (atom nil))

(defn -start-web-server!
  "Start the Titan web server."
  []
  (let [host (or (env/env :titan_host) "127.0.0.1")
        port (or (env/env :titan_port) 5000)]
    (if (nil? @server)
      (do
        (log/infof "Starting Titan server on %s:%s..." host port)
        (reset! server (web/run @app/app {:titan_host host
                                          :titan_port port})))
      (log/error "The Titan server is already running. To restart the server,"
                 "use `titan.server/restart`"))))

(defn start-server!
  "Start the Titan server."
  []
  (db/set-korma-db!)
  #_(nrepl/start-server) ; deal with this later
  (-start-web-server!))

(defn stop-server!
  "Stop the Titan server."
  []
  (if-let [s (web/server @server)]
    (do
      (log/info "Stopping Titan server...")
      (.stop s)
      (log/info "Stopped!")
      (reset! server nil))
    (log/error "Titan server is not currently running!")))

(defn restart-server
  "Restart the Titan server."
  []
  (stop-server!)
  (log/info "Restarting Titan server...")
  (-start-web-server!)
  (log/info "Titan server re-started!"))