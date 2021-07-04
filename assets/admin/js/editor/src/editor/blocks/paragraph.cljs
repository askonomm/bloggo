(ns editor.blocks.paragraph
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]))

(defn on-key-press [event]
  (if (or (= "Enter" (.-key event))
          (= 13 (.-keyCode event)))
    (.preventDefault event)
    (prn "enter was not pressed")))

(defn on-input [index height event]
  (dispatch
   [:update-paragraph-block
    {:position index
     :content (.-innerHTML (.-target event))}])
  (reset! height (.-scrollHeight (.-target event))))

(defn block [index block]
  (let [height (r/atom 0)]
    (fn []
      [:div.paragraph-content
       {:contentEditable true
        :style {:height (str @height "px")}
        :ref (fn [el]
               (when el
                 (reset! height (.-scrollHeight el))))
        :on-key-press #(on-key-press %)
        :on-input #(on-input index height %)
        :dangerouslySetInnerHTML {:__html (get block :content)}}])))