(ns mars-rover-kata.stateless-mars-rover)

(def orientations [:N :E :S :W])
(def orientations-indexes {:N 0 :E 1 :S 2 :W 3})
(def orientations-deltas {:N 1 :W -1 :S -1 :E 1})
(def movements-deltas {:f 1 :b -1 :l -1 :r 1})
(def grid-size 10)
(def obstacles #{{:x 1 :y 2} {:x 2 :y 4} {:x 5 :y 5} {:x 7 :y 2} {:x 9 :y 1}})

(defn correct-orientation? [orientation]
	(if (orientation orientations-indexes)
		true
		false))

(defn get-axis-value [status axis]
	(get-in status [:pos axis]))

(defn get-orientation [status] 
	(:orient status))

(defn movement-delta [status command]
	(* ((get-orientation status) orientations-deltas) 
		(command movements-deltas)))
		
(defn destination [status axis dest]
	(if (= :x axis) 
		{:x dest :y (get-axis-value status :y)} 
		{:x (get-axis-value status :x) :y dest}))

(defn obstacle-in-destination? [status axis dest]
	(if (contains? obstacles (destination status axis dest))
		true
		false))

(defn new-axis-pos [status command axis]
	(mod (+ (get-axis-value status axis) 
			(movement-delta status command))
		grid-size))

(defn set-new-axis-val [status command axis]
	(let [dest (new-axis-pos status command axis)]
		(if (obstacle-in-destination? status axis dest) 
			status ; better to throw an exception?
			(assoc-in status [:pos axis] dest))))

(defn new-orientation-index [status command]
	(mod (+ ((get-orientation status) orientations-indexes) 
			(command movements-deltas)) 
		(count orientations)))

(defn new-orientation [status command]
	(get orientations (new-orientation-index status command)))

(defn set-new-orientation [status command]
	(assoc-in status [:orient] (new-orientation status command)))

(defn move [status command]
	(let [set-new-axis-val-in (partial set-new-axis-val status command)]
		(case  #{(get-orientation status) command}
			(#{:N :f} #{:N :b} #{:S :f} #{:S :b}) 
				(set-new-axis-val-in :y)
			(#{:E :f} #{:E :b} #{:W :f} #{:W :b}) 
				(set-new-axis-val-in :x)
			(#{:N :l} #{:N :r} #{:S :l} #{:S :r} #{:W :l} #{:W :r} #{:E :l} #{:E :r}) 
				(set-new-orientation status command)
			)))

(defn do-commands [status commands]
	(reduce move status commands))












