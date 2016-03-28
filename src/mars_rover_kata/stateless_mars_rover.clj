(ns mars-rover-kata.stateless-mars-rover)

(def orientations [:N :E :S :W])
(def orientations-indexes {:N 0 :E 1 :S 2 :W 3})
(def orientations-deltas {:N 1 :W -1 :S -1 :E 1})
(def movements-deltas {:f 1 :b -1 :l -1 :r 1})
(def grid-size 10)
(def obstacles #{{:x 1 :y 2} {:x 2 :y 4} {:x 5 :y 5} {:x 7 :y 2} {:x 9 :y 1}})

; Basic operations on status *********************************
(defn get-orientation [status]
	(:orient status))

(defn get-axis-value [status axis]
	(get-in status [:pos axis]))

(defn update-element [status element new-value]
	(assoc-in status [element] new-value))

; Position manipulation operations ***************************
(defn obstacle-in-destination? [destination]
	(contains? obstacles destination))

(defn calc-destination [status axis axis-dest]
	(if (= :x axis)
		{:x axis-dest :y (get-axis-value status :y)}
		{:x (get-axis-value status :x) :y axis-dest}))

(defn wrap-axis-destination [linear-axis-destination]
	(mod linear-axis-destination grid-size))

(defn set-new-axis-val [status movement-delta axis]
	(let [destination ((comp (partial calc-destination status axis)
														wrap-axis-destination
														(partial + movement-delta)
														get-axis-value)
										status axis)]
		(if (obstacle-in-destination? destination)
			status ; better to throw an exception?
			(update-element status :pos destination))))

; Orientation manipulation operations **************************
(defn get-new-orientation [index]
	(get orientations index))

(defn wrap-orientation-index [linear-orientation-index]
	(mod linear-orientation-index (count orientations)))

(defn get-orientation-index [orientation]
	(orientation orientations-indexes))

(defn calc-new-orientation-index [status command-delta]
	((comp wrap-orientation-index
				(partial + command-delta)
				get-orientation-index
				get-orientation)
	status))

(defn set-new-orientation [status command-delta]
	((comp (partial update-element status :orient)
					get-new-orientation
					calc-new-orientation-index)
	status command-delta))

; Movement preparatioin functions ******************************
(defn get-orientation-delta [orientation]
  (orientation orientations-deltas))

(defn calc-movement-delta [status command-delta]
  ((comp (partial * command-delta)
  				get-orientation-delta
  				get-orientation)
  status))

; Main "orchestration" functions ******************************
(defn move [status command]
	(let [command-delta (command movements-deltas)
				movement-delta (calc-movement-delta status command-delta)
				set-new-axis-val-in (partial set-new-axis-val status movement-delta)]
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












