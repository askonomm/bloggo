(ns bloggo.routes.admin.delete-post
  (:require [bloggo.data.post :as post])
  (:require-macros [bloggo.macros :refer [protected param redirect]]))

(defn get! [^js req ^js res]
  (protected
   (post/delete-post! (param "id"))
   (redirect "/admin/posts")))