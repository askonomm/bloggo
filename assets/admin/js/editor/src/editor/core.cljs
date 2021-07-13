(ns editor.core
  (:require
   [clojure.edn :as edn]
   [reagent.dom :as rdom]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [editor.events]
   [editor.subs]
   [editor.blocks :as blocks]
   [editor.blocks.add :as blocks.add]))

(defn editor [on-change-callback]
  (let [blocks @(subscribe [:blocks])]
    (on-change-callback (js/encodeURIComponent (pr-str blocks)))
    [:div.blocks
     [blocks.add/block 0]
     (map-indexed
      (fn [index block]
        ^{:key (get block :id)}
        [:<>
         [blocks/block index block]
         [blocks.add/block (+ index 1)]])
      blocks)]))

(defn ^:export main [args]
  (let [container (get (js->clj args :keywordize-keys true) :container)
        content (get (js->clj args :keywordize-keys true) :initialContent)
        on-change-callback (get (js->clj args :keywordize-keys true) :onChange)]
    (dispatch-sync [:initialise-db])
    (when-not (empty? content)
      (dispatch [:set-blocks (edn/read-string (js/decodeURIComponent content))]))
    (rdom/render [editor on-change-callback] (.querySelector js/document container))))