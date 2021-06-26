(ns bloggo.routes.admin.setup)

(defn post! [req ^js res]
  (let [title (.-title (.-body req))
        description (.-description (.-body req))
        email (.-email (.-body req))
        password (.-password (.-body req))]
    (.render res "setup" (clj->js (cond (empty? title)
                                        {:error "Blog title is required."
                                         :error-title true}
                                        (empty? description)
                                        {:error "Blog description is required."
                                         :error-description true}
                                        (empty? email)
                                        {:error "E-mail is required."
                                         :error-email true}
                                        (empty? password)
                                        {:error "Password is required."
                                         :error-password true})))))

(defn get! [_ ^js res]
  (.render res "setup"))