(ns bloggo.routes.admin.setup
  (:require-macros [hiccup.core :refer [html]]))

(defn post! [req res]
  (js/console.log req))

(defn get! [req res]
  (.send res (html [:div.setup-view
                    [:h1 "Bloggo Set-up"]
                    [:form {:method "post"}
                     [:input {:type "email"
                              :placeholder "Your e-mail"}]
                     [:input {:type "password"
                              :placeholder "Your password"}]
                     [:button {:type "submit"} "Set-up"]]])))