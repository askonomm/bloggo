(ns editor.blocks
  (:require
   ["@fortawesome/react-fontawesome" :refer (FontAwesomeIcon)]
   ["@fortawesome/free-solid-svg-icons" :refer (faTrash)]
   [re-frame.core :refer [dispatch]]
   [editor.events]
   [editor.blocks.paragraph :as blocks.paragraph]
   [editor.blocks.heading :as blocks.heading]
   [editor.utils :as utils]))

(defn content [index block]
  (cond
    (= :paragraph (get block :type))
    (blocks.paragraph/block index block)
    (= :heading (get block :type))
    (blocks.heading/block index block)
    :else nil))

(defn controls [index]
  [:div.controls
   [:div.control
    {:on-click #(dispatch [:delete-block index])}
    [:> FontAwesomeIcon {:icon faTrash}]]])

(defn block [index block]
  [:div.block {:class (get block :type)
               :data-id (get block :id)}
   [controls index]
   [content index block]])

(defn focus! [{:keys [id where]} blocks]
  (when-let [block-el (.querySelector js/document (str ".block[data-id='" id "']"))]
    (let [block (utils/find-by-predicate #(= (:id %) id) blocks)]
      (cond (= :paragraph (get block :type))
            (.focus (.querySelector block-el ".paragraph-content"))
            (= :heading (get block :type))
            (.focus (.querySelector block-el "textarea"))))))