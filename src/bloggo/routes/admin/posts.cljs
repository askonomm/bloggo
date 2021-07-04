(ns bloggo.routes.admin.posts
  (:require [bloggo.data.posts :refer [get-posts]])
  (:require-macros [bloggo.macros :refer [view protected]]))

(defn get! [^js req ^js res]
  (protected
   (let [posts (get-posts 10)]
     (view "admin/posts" {:layout "admin"
                          :title "Posts"
                          :posts posts}))))