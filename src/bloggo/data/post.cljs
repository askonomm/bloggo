(ns bloggo.data.post
  (:require [bloggo.data :as data]
            [bloggo.data.posts :refer [get-posts]]))

(defn add-post! [user-id]
  (let [last-post (get-posts 1)
        last-post-id (if last-post (get last-post :id) 0)
        new-post-id (+ last-post-id 1)]
    (data/run!
     "INSERT INTO posts (`id`, `uid`) VALUES (?, ?)"
     [new-post-id user-id])))

(defn get-post [id]
  (data/query
   "SELECT * FROM posts WHERE id = ?"
   [id]))

(defn update-post! [id data]
  (let [post (get-post id)
        new-post (merge post data)]
    (prn "new-post: " new-post)
    (data/run!
     "UPDATE posts SET title = ?, slug = ?, entry = ?, timestamp = ?, status = ? WHERE id = ?"
     [(get new-post :title)
      (get new-post :slug)
      (get new-post :entry)
      (get new-post :timestamp)
      (get new-post :status)
      (get new-post :id)])))