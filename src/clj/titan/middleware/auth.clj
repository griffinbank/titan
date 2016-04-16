(ns titan.middleware.auth
  (:require [cemerick.url :refer [map->query url url-encode]]
            [clojure.tools.logging :as log]
            [clout.core :as clout]
            [ring.util.request :as req]
            [titan.http.response :as responses]))

(defn matches-any-path?
  "Does this request match any of the paths?

   Expects paths to be a coll."
  [paths request]
  (some #(clout/route-matches % request) paths))

;; TODO: Clean this up. It could easily go into that function above.
(defn is-site-route?
  "Is this a blacklisted site route?"
  [request site-paths]
  (matches-any-path? site-paths request))

(defn redirect-to-signin
  [request]
  (let [root-target-url (req/path-info request)
        query-str (map->query (:query-params request))
        redirect-url (str root-target-url
                          (when query-str
                            (str "?" query-str)))]
    (responses/redirect
     (str "/login?redirect="
          (url-encode redirect-url)))))

(defn wrap-authentication
  "Wraps authentication for the handler. If a user is successfully authenticated,
  then the return value of the auth-fn is assoc'd onto the :user key of the request
  map."
  [handler auth-fn & {:keys [site-paths
                             whitelist]
                      :or {site-paths []
                           whitelist #{}}}]
  (fn [request]
    (if (whitelist (req/path-info request))
      (handler request)
      (if-let [user (auth-fn request)]
        (handler (assoc request :user user))
        (if (matches-any-path? site-paths request)
          (redirect-to-signin request)
          (responses/unauthorized "User not authenticated."))))))
