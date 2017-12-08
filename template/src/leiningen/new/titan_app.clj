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
             ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
             ["src/{{sanitized}}/model.clj" (render "model.clj" data)]
             ["project.clj" (render "project.clj" data)]
             "resources"
             "test/{{sanitized}}"
             )))
