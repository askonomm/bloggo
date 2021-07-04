(ns editor.blocks
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]
            [editor.events]))

(defn add-block->paragraph [index block-menu]
  (dispatch [:add-block
             {:position index
              :block {:type :paragraph
                      :content ""}}])
  (reset! block-menu nil))

(defn add-block->heading-big [index block-menu]
  (dispatch [:add-block
             {:position index
              :block {:type :heading-big
                      :content ""}}])
  (reset! block-menu nil))

(defn add-block [index]
  (let [block-menu (r/atom nil)]
    (fn []
      [:div.add-block
       (when @block-menu
         [:div.menu
          [:ul
           [:li {:on-click #(add-block->paragraph index block-menu)} "Paragraph"]
           [:li {:on-click #(add-block->heading-big index block-menu)} "Heading Big"]]])
       [:div {:on-click #(reset! block-menu true)} "Add block"]])))

(defn paragraph-onkeypress [event]
  (if (or (= "Enter" (.-key event))
          (= 13 (.-keyCode event)))
    (.preventDefault event)
    (prn "enter was not pressed")))

(defn paragraph-oninput [index height event]
  (dispatch
   [:update-paragraph-block
    {:position index
     :content (.-innerHTML (.-target event))}])
  (reset! height (.-scrollHeight (.-target event))))

(defn paragraph [index block]
  (let [height (r/atom 0)]
    (fn []
      [:div.paragraph-content
       {:contentEditable true
        :style {:height (str @height "px")}
        :ref (fn [el]
               (when el
                 (reset! height (.-scrollHeight el))))
        :on-key-press #(paragraph-onkeypress %)
        :on-input #(paragraph-oninput index height %)
        :dangerouslySetInnerHTML {:__html (get block :content)}}])))

(defn heading-big-onkeypress [event]
  (if (or (= "Enter" (.-key event))
          (= 13 (.-keyCode event)))
    (.preventDefault event)
    (prn "enter was not pressed")))

(defn heading-big-onchange [index height event]
  (prn "change happened")
  (dispatch [:update-heading-block
             {:position index
              :content (.-value (.-target event))}])
  (reset! height (.-scrollHeight (.-target event))))

(defn heading-big [index block]
  (let [height (r/atom 0)]
    (fn []
      [:textarea
       {:default-value (get block :content)
        :style {:height (str @height "px")}
        :ref (fn [el]
               (when el
                 (reset! height (.-scrollHeight el))))
        :on-key-press #(heading-big-onkeypress %)
        :on-change #(heading-big-onchange index height %)}])))

(defn block [index block]
  (cond (= :paragraph (get block :type))
        (paragraph index block)
        (= :heading-big (get block :type))
        (heading-big index block)))