(ns editor.blocks.add
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]))

(defn add-paragraph [index block-menu]
  (dispatch [:add-block
             {:position index
              :block {:type :paragraph
                      :content ""}}])
  (reset! block-menu nil))

(defn add-heading-big [index block-menu]
  (dispatch [:add-block
             {:position index
              :block {:type :heading-big
                      :content ""}}])
  (reset! block-menu nil))

(defn block [index]
  (let [block-menu (r/atom nil)]
    (fn []
      [:div.add-block
       (when @block-menu
         [:div.menu
          [:ul
           [:li {:on-click #(add-paragraph index block-menu)} "Paragraph"]
           [:li {:on-click #(add-heading-big index block-menu)} "Heading Big"]]])
       [:div {:on-click #(reset! block-menu true)} "Add block"]])))