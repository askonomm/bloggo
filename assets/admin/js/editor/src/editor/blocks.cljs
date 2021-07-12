(ns editor.blocks
  (:require ["@fortawesome/react-fontawesome" :refer (FontAwesomeIcon)]
            ["@fortawesome/free-solid-svg-icons" :refer (faTrash)]
            [re-frame.core :refer [dispatch]]
            [editor.events]
            [editor.blocks.paragraph :as blocks.paragraph]
            [editor.blocks.heading-big :as blocks.heading-big]))

(defn content [index block]
  (cond (= :paragraph (get block :type))
        (blocks.paragraph/block index block)
        (= :heading-big (get block :type))
        (blocks.heading-big/block index block)
        :else nil))

(defn controls [index]
  [:div.controls
   [:div.control
    {:on-click #(dispatch [:delete-block index])}
    [:> FontAwesomeIcon {:icon faTrash}]]])

(defn block [index block]
  [:div.block {:class (get block :type)}
   [controls index]
   [content index block]])
