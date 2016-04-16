(ns titan.server.nrepl
  "Logic for starting an nREPL server if we're in development mode."
  (:require [clojure.tools.logging :as log]
            [clojure.tools.nrepl.middleware :as middleware]
            [clojure.tools.nrepl.middleware.render-values :refer [render-values]]
            [clojure.tools.nrepl.server :as nrepl]
            [whidbey.repl :as whidbey]))

(def server
  (atom nil))

(def highlight
  render-values)

#_(middleware/set-descriptor!
 #'highlight
 {:requires #{}
  :expects #{"eval"}
  :handles {}})

#_(def handler
  (do (whidbey/init!
       {:color-scheme {:delimiter [:red]
                       :tag       [:red]

                       :nil       [:cyan]
                       :boolean   [:cyan]
                       :number    [:cyan]
                       :string    [:cyan]
                       :character [:cyan]
                       :keyword   [:green]
                       :symbol    nil

                       :function-symbol [:blue]
                       :class-delimiter [:blue]
                       :class-name nil

                       :exception [:red]}})
      (nrepl/default-handler #'highlight)))

(defn start-server
  ([]
   (start-server {}))
  ([{:keys [port]
     :or {port 7003}
     :as opts}]
   (when-not @server
     (log/infof "Starting nREPL server on port %s..." port)
     (spit ".nrepl-port" port)
     #_(reset! server (nrepl/start-server :port port ; :handler handler
                                          )))))
