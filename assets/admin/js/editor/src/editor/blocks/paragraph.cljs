(ns editor.blocks.paragraph
  (:require [re-frame.core :refer [dispatch]]))

(defn on-key-press [event]
  (if (or (= "Enter" (.-key event))
          (= 13 (.-keyCode event)))
    (.preventDefault event)
    (prn "enter was not pressed")))

(defn on-input [index event]
  (dispatch
   [:update-paragraph-block
    {:position index
     :content (.-innerHTML (.-target event))}]))

(defn block [index block]
  (fn []
    [:div.paragraph-content
     {:contentEditable true
      :on-key-press #(on-key-press %)
      :on-input #(on-input index %)
      :dangerouslySetInnerHTML {:__html (get block :content)}}]))