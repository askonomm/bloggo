(ns bloggo.data.posts
  (:require [bloggo.data :as data]))

(defn get-posts
  [limit]
  (if (> limit 1)
    (data/query-all
     "SELECT * FROM posts ORDER BY timestamp DESC LIMIT ?"
     [limit])
    (data/query "SELECT * FROM posts ORDER BY timestamp DESC")))