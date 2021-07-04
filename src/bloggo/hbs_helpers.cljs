(ns bloggo.hbs-helpers
  (:require [bloggo.data.config :refer [get-config]]))

(defn config 
  "Returns a configuration value by a given `key` 
  directly from the database. If not found, returns 
  an empty string instead."
  [key]
  (if-let [config-item (get-config key)]
    (get config-item :value)
    ""))