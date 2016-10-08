(ns titan.server
  (:require [immutant.web :as web]
            [clojure.tools.logging :as log]
            [environ.core :as env]
            [ring.middleware.reload :refer [wrap-reload]]
            [titan.app :as app]
            [titan.db :as db]))

(defonce server (atom nil))

;; TODO: This is for development, to allow hot-reloading of code.
;; In production, we should just directly target @app/app
(defn handler
  [req]
  (@app/app req))

(defn -start-web-server!
  "Start the Titan web server."
  ([] (-start-web-server! {}))
  ([{:keys [host path port]
     :or {host (or (env/env :titan_host) "127.0.0.1")
          path (or (env/env :titan_path) "/")
          port (or (env/env :titan_port) 8080)}}]
   (if (nil? @server)
     (do
       (log/infof "Starting Titan server on %s:%s..." host port)
       (reset! server (web/run handler {:host host
                                        :path path
                                        :port port})))
     (log/error "The Titan server is already running. To restart the server,"
                "use `titan.server/restart`"))))

(defn start-server!
  "Start the Titan server."
  []
  (when (env/env :database-url)
    (db/set-korma-db!))
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

(defn restart-server!
  "Restart the Titan server."
  []
  (stop-server!)
  (log/info "Restarting Titan server...")
  (-start-web-server!)
  (log/info "Titan server re-started!"))
