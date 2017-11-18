(ns titan.schema)

(defn human-readable-error
  [error]
  (format "Parameter validation failed: %s" error))
