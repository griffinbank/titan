(ns leiningen.new.titan-app
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "titan-app"))

(defn titan-app
  "FIXME: write documentation"
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' titan-app project.")
    (->files data
             ["src/{{sanitized}}/core.clj" (render "src/core.clj" data)]
             ["src/{{sanitized}}/model.clj" (render "src/model.clj" data)]
             ["resources/migrations/00000000000000-example.down.sql"
              (render "resources/migrations/00000000000000-example.down.sql" data)]
             ["resources/migrations/00000000000000-example.up.sql"
              (render "resources/migrations/00000000000000-example.up.sql" data)]
             ["project.clj" (render "project.clj" data)]
             "resources"
             "test/{{sanitized}}"
             )))
