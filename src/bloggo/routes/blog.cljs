(ns bloggo.routes.blog
  (:require [bloggo.data.config :refer [get-config]]))

(defn get! [_ res]
  (if (not (get-config "version"))
    (.redirect res "/admin/setup")
    (.send res "Hi, this here will have blog posts!")))