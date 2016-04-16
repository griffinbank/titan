(ns titan.db
  (:require [titan.db.util :as util]
            [environ.core :refer [env]]
            [korma.db :as korma]))

(defn construct-db-map
  []
  (assoc (util/parse-url (env :database-url))
         :subprotocol "postgresql"
         :subname (util/build-db-subname (env :database-url))))

(defn set-korma-db!
  "Set Korma's default database connection if it hasn't been set already"
  []
  (when (nil? @korma.db/_default)
    (korma.db/default-connection (construct-db-map))))
