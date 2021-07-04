(ns bloggo.routes.admin.edit-post
  (:require [bloggo.data.post :refer [get-post update-post!]])
  (:require-macros [bloggo.macros :refer [view protected redirect session param input]]))

(defn post! [^js req ^js res]
  (protected
   (if-let [post (get-post (param "id"))]
     (let [new-data {:title (input "title")
                     :entry (input "entry")}]
       (do
         (update-post! (param "id") new-data)
         (view "admin/edit-post" {:layout "admin"
                                  :title "Edit Post"
                                  :post (merge post new-data)})))
     (redirect "/admin/posts"))))

(defn get! [^js req ^js res]
  (if (not (session "uid"))
    (redirect "/admin/signin")
    (if-let [post (get-post (param "id"))]
      (view "admin/edit-post" {:layout "admin"
                               :title "Edit Post"
                               :post post})
      (redirect "/admin/posts"))))