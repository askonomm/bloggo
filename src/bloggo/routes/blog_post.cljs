(ns bloggo.routes.blog-post)

(defn get! [req res]
  (.send res "this here is a blog post (or page)"))