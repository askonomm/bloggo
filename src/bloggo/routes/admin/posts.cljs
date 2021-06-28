(ns bloggo.routes.admin.posts
  (:require [bloggo.data.config :as config]))

(defn get! [req res]
  (if (not (and (.-uid (.-session req))
                (config/get "version")))
    (.redirect res "/admin/setup")
    (.render res "admin/posts")))