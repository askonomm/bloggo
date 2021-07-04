(ns editor.core
  (:require [clojure.edn :as edn]
            [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [editor.events]
            [editor.subs]
            [editor.utils :as utils]
            [editor.blocks :as blocks]))

(defn editor []
  (let [blocks @(subscribe [:blocks])]
    (utils/set-entry! blocks)
    [:div.blocks
     [blocks/add-block 0]
     (map-indexed
      (fn [index block]
        ^{:key index}
        [:<>
         [:div.block {:class (get block :type)}
          [blocks/block index block]]
         [blocks/add-block (+ index 1)]])
      blocks)]))

(defn ^:export main [raw-content]
  (dispatch-sync [:initialise-db])
  (when-not (empty? raw-content)
    (dispatch [:set-blocks (edn/read-string (js/decodeURIComponent raw-content))]))
  (rdom/render [editor] (.querySelector js/document ".block-editor")))