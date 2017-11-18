(ns titan.commands.db.migrate
  (:gen-class)
  (:require [titan.db.migrations :refer [migrate]]))

(defn -main
  []
  (migrate))
