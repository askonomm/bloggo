(ns editor.utils)

(defn block->blocks
  "Takes an input of `blocks`, into which it adds `block, 
  depending on given `position`. If the `blocks` are empty, 
  it simply returns a vector with just the given `block` in it. 
  Otherwise, it will position it accordingly."
  [blocks block position]
  (if (empty? blocks)
    [block]
    (cond
      ; if position is 0, we want the new block
      ; to become before any other block
      (= position 0)
      [block blocks]

      ; if the position is after the last block,
      ; we want the new block to be the new last block.
      (= position (count blocks))
      [blocks block]

      ; otherwise simply add the new block to replace
      ; a position of another block, by coming before that.
      :else
      (vec
       (flatten
        (map-indexed
         (fn [index iteration-block]
           (if (= index position)
             [block iteration-block]
             iteration-block)) blocks))))))

(defn set-entry! [blocks]
  (set!
   (.-value (.querySelector js/document "input[name='entry']"))
   (js/encodeURIComponent (pr-str blocks))))