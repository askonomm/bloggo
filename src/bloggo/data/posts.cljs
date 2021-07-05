(ns bloggo.data.posts
  (:require [bloggo.data :as data]))

(defn get-posts
  "Retrieves all posts, regardless of status, so long as 
  the number of results do not exceed the given `limit`."
  [limit]
  (if (> limit 1)
    (data/query-all
     "SELECT * FROM posts ORDER BY timestamp DESC LIMIT ?"
     [limit])
    (data/query "SELECT * FROM posts ORDER BY id DESC")))