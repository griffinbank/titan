(ns leiningen.new.titan-app
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "titan-app"))

(defn titan-app
  "Configure a basic Titan application scaffolding with user login/logout endpoints."
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info
      (str "Generating Titan project scaffold for "
           name
           "..."))
    (->files data
             ["src/{{sanitized}}/core.clj" (render "src/core.clj" data)]
             ["src/{{sanitized}}/model.clj" (render "src/model.clj" data)]
             ["resources/migrations/00000000000000-example.down.sql"
              (render "resources/migrations/00000000000000-example.down.sql" data)]
             ["resources/migrations/00000000000000-example.up.sql"
              (render "resources/migrations/00000000000000-example.up.sql" data)]
             ["project.clj" (render "project.clj" data)]
             ["src/log4j.properties" (render "log4j.properties" data)]
             "resources"
             "test/{{sanitized}}"
             )))
