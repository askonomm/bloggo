(ns bloggo.routes.admin.new-post
  (:require [bloggo.data.post :refer [add-post!]])
  (:require-macros [bloggo.macros :refer [redirect session]]))

(defn get! [^js req ^js res]
  (if (not (session "uid"))
    (redirect "/admin/signin")
    (let [post-id (add-post! (session "uid"))]
      (redirect (str "/admin/edit-post/" post-id)))))