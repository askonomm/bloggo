(ns editor.core
  (:require
   [devtools.core :as devtools]
   [clojure.edn :as edn]
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [editor.events]
   [editor.subs]
   [editor.blocks :as blocks]
   [editor.blocks.add :as blocks.add]))

(devtools/install!)

(defn render [on-change-callback]
  (let [blocks-state (r/atom [])
        on-change-callback (r/atom nil)]
    (r/create-class
     {:component-did-update
      (fn [this _]
        (let [new-argv (into {} (rest (r/argv this)))
              new-blocks (get new-argv :blocks)
              block-focus (get new-argv :block-focus)]
          (reset! blocks-state new-blocks)
          (when block-focus
            (blocks/focus! block-focus new-blocks))))
      :component-did-mount
      (fn [props]
        (reset! blocks-state (get (r/props props) :blocks))
        (reset! blocks-state (get (r/props props) :on-change-callback)))
      :reagent-render
      (fn []
        [:div.blocks
         [blocks.add/block 0]
         (map-indexed
          (fn [index block]
            ^{:key (get block :id)}
            [:<>
             [blocks/block index block]
             [blocks.add/block (+ index 1)]])
          @blocks-state)])})))

(defn editor [on-change-callback]
  (let [blocks-sub (subscribe [:blocks])
        block-focus (subscribe [:block-focus])]
    (on-change-callback (js/encodeURIComponent (pr-str @blocks-sub)))
    (fn []
      [render {:blocks @blocks-sub
               :block-focus @block-focus}])))

(defn ^:export main [args]
  (let [container (get (js->clj args :keywordize-keys true) :container)
        content (get (js->clj args :keywordize-keys true) :initialContent)
        on-change-callback (get (js->clj args :keywordize-keys true) :onChange)]
    (dispatch-sync [:initialise-db])
    (when-not (empty? content)
      (dispatch [:set-blocks (edn/read-string (js/decodeURIComponent content))]))
    (rdom/render [editor on-change-callback] (.querySelector js/document container))))