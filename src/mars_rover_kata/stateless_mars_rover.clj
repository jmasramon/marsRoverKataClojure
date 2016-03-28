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

(defn get-orientation-index [orientation]
	(orientation orientations-indexes))

(defn calculate-destination [status axis axis-dest]
	(if (= :x axis) 
		{:x axis-dest :y (get-axis-value status :y)} 
		{:x (get-axis-value status :x) :y axis-dest}))

(defn obstacle-in-destination? [destination]
	(if (contains? obstacles destination)
		true
		false))

(defn update-position [status destination]
	(assoc-in status [:pos] destination))

(defn movement-delta [status command]
	(* ((get-orientation status) orientations-deltas) 
		(command movements-deltas)))
		
(defn new-axis-pos [status command axis]
	(mod (+ (get-axis-value status axis) 
			(movement-delta status command))
		grid-size))

(defn set-new-axis-val [status command axis]
	(let [destination (calculate-destination status axis (new-axis-pos status command axis))]
		(if (obstacle-in-destination? destination) 
			status ; better to throw an exception?
			(update-position status destination))))

(defn update-orientation [status orientation]
	(assoc-in status [:orient] orientation))

(defn get-new-orientation [index]
	(get orientations index))

(defn get-new-orientation-index [status command-delta]
	(let [new-lin-orientation-ind ((comp (partial + command-delta) 
										get-orientation-index 
										get-orientation) 
									status)]
		(mod new-lin-orientation-ind (count orientations))))

(defn set-new-orientation [status command-delta]
	(let [set-new-orientation-fun (comp (partial update-orientation status) 
										get-new-orientation 
										get-new-orientation-index)]
		(set-new-orientation-fun status command-delta)))

(defn move [status command]
	(let [command-delta (command movements-deltas)
		set-new-axis-val-in (partial set-new-axis-val status command)]
		(case  #{(get-orientation status) command}
			(#{:N :f} #{:N :b} #{:S :f} #{:S :b}) 
				(set-new-axis-val-in :y)
			(#{:E :f} #{:E :b} #{:W :f} #{:W :b}) 
				(set-new-axis-val-in :x)
			(#{:N :l} #{:N :r} #{:S :l} #{:S :r} #{:W :l} #{:W :r} #{:E :l} #{:E :r}) 
				(set-new-orientation status command-delta)
			)))

(defn do-commands [status commands]
	(reduce move status commands))












