(ns bloggo.hbs-helpers
  (:require [bloggo.data.config :refer [get-config]]))

(defn config [key]
  (if-let [config-item (get-config key)]
    (get config-item :value)
    ""))