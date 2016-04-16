(ns titan.app)

(defonce app (atom nil))

(defmacro defapp
  "Given a routing body, store it in the `titan.app/app` atom."
  [& body]
  `(reset! app ~@body))
