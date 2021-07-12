(ns editor.blocks.paragraph
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]
            [editor.utils :as utils]))

(defn on-key-press! [event]
  (if (or (= "Enter" (.-key event))
          (= 13 (.-keyCode event)))
    (.preventDefault event)
    (prn "enter was not pressed")))

(defn on-input! [index event]
  (dispatch
   [:update-paragraph-block
    {:position index
     :content (utils/parse-html (.-innerHTML (.-target event)))}]))

(defn on-paste! [content-state caret-location-state event]
  (.preventDefault event)
  (.then
   (.readText (.-clipboard js/navigator))
   (fn [clip]
     (let [selection (.getSelection js/window)
           caret-location (.-anchorOffset selection)
           pasted-content (utils/parse-html clip)
           new-content (utils/string->string @content-state pasted-content caret-location)
           new-caret-location (+ caret-location (count pasted-content))]
       (reset! content-state new-content)
       (reset! caret-location-state new-caret-location)))))

(defn block [index block]
  (let [ref (r/atom nil)
        content-state (r/atom (get block :content))
        caret-location-state (r/atom nil)]
    (r/create-class
     {:component-did-update
      (fn []
        #_(let [selection (.getSelection js/window)
                range (.createRange js/document)]
            (.setStart range (first (.-childNodes @ref)) @caret-location-state)
            (.collapse range true)
            (.removeAllRanges selection)
            (.addRange selection range)
            (.focus @ref)))
      :reagent-render
      (fn []
        [:div.paragraph-content
         {:contentEditable true
          :ref (fn [el] (reset! ref el))
          :on-key-press #(on-key-press! %)
          :on-input #(on-input! index %)
          :on-paste #(on-paste! content-state caret-location-state %)
          :dangerouslySetInnerHTML {:__html @content-state}}])})))