(ns titan.util)

(defn fully-qualified-symbol
  "Builds a fully-qualified symbol from a namespace and an unqualified name."
  [ns name]
  (symbol (str ns "/" name)))

(defmacro redef
  "Redefines a symbol from another namespace in the current namespace.
  Practically this allows a namespace to proxy a definition from another."
  [ns defs]
  `(do ~@(for [d defs]
    `(def ~d ~(fully-qualified-symbol ns d)))))
