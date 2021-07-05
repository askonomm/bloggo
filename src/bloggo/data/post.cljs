(ns bloggo.data.post
  (:require [bloggo.data :as data]
            [bloggo.data.posts :refer [get-posts]]))

(defn add-post!
  "Creates a new empty post for a `user-id`, and
  auto-increments the post ID."
  [user-id]
  (let [last-post (get-posts 1)
        last-post-id (if last-post (get last-post :id) 0)
        new-post-id (+ last-post-id 1)]
    (prn "last post: " last-post)
    (prn "new post id: " new-post-id)
    (data/run!
     "INSERT INTO posts (`id`, `uid`) VALUES (?, ?)"
     [new-post-id user-id])))

(defn get-post
  "Retrieves a post by given `id`."
  [id]
  (data/query
   "SELECT * FROM posts WHERE id = ?"
   [id]))

(defn update-post!
  "Updates a post by a given `id` and merges the new `data`
  with the existing post by overwriting what is new."
  [id data]
  (let [post (get-post id)
        new-post (merge post data)]
    (data/run!
     "UPDATE posts SET title = ?, slug = ?, entry = ?, timestamp = ?, status = ? WHERE id = ?"
     [(get new-post :title)
      (get new-post :slug)
      (get new-post :entry)
      (get new-post :timestamp)
      (get new-post :status)
      (get new-post :id)])))

(defn delete-post!
  "Deletes a post with a given `id`."
  [id]
  (data/run!
   "DELETE FROM posts WHERE id = ?"
   [id]))