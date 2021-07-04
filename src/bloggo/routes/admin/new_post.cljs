(ns bloggo.routes.admin.new-post
  (:require [bloggo.data.post :refer [add-post!]])
  (:require-macros [bloggo.macros :refer [redirect session protected]]))

(defn get! [^js req ^js res]
  (protected
   (let [post-id (add-post! (session "uid"))]
     (redirect (str "/admin/edit-post/" post-id)))))