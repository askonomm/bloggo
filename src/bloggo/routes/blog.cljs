(ns bloggo.routes.blog)

(defn get! [req res]
  (.send res "Hi, this here will have blog posts!"))