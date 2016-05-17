(ns titan.db
  (:require [titan.db.util :as util]
            [environ.core :refer [env]]
            [korma.db :as korma]))

(defn construct-db-map
  []
  (let [db-url (env :database-url)]
    (assoc (util/parse-url db-url)
           :subprotocol "postgresql"
           :subname (util/build-db-subname db-url))))

(defn set-korma-db!
  "Set Korma's default database connection if it hasn't been set already"
  []
  (when (nil? @korma.db/_default)
    (korma/default-connection
      (korma/create-db
       (construct-db-map)))))
