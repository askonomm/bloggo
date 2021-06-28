(ns bloggo.routes.blog
  (:require [bloggo.data.config :as config]))

(defn get! [_ res]
  (if (not (config/get "version"))
    (.redirect res "/admin/setup")
    (.send res "Hi, this here will have blog posts!")))