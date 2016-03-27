(ns mars-rover-kata.stateless-mars-rover)

(def orientations {:N 0 :E 1 :S 2 :W 3})
(def turns [:N :E :S :W])
(def directions {:N 1 :W -1 :S -1 :E 1})
(def turn-modulo-base 4)
(def movements {:f 1 :b -1 :l -1 :r 1})
(def grid-size 10)
(def obstacles #{{:x 1 :y 2} {:x 2 :y 4} {:x 5 :y 5} {:x 7 :y 2} {:x 9 :y 1}})

(defn get-x [status] 
	(get-in status [:pos :x]))

(defn get-y [status] 
	(get-in status [:pos :y]))

(defn get-orient [status] 
	(:orient status))

(defn correct-orientation? [orientation]
	(if (orientation orientations)
		true
		false))

(defn movement-delta [status stp]
	(* ((get-orient status) directions) 
		(stp movements)))
		
(defn new-pos [status fun stp]
	(mod (+ (fun status) 
			(movement-delta status stp))
		grid-size))

(defn other-axis [axis]
	(if (= :y axis)
		:x
		:y))

(defn complete-destination [status axis dest]
	(if (= :x axis) 
		{:x dest :y (get-y status)} 
		{:x (get-x status) :y dest}))

(defn obstacle-in-destination? [status axis dest]
	(println "complete-destination:" (complete-destination status axis dest))
	(if (contains? obstacles (complete-destination status axis dest))
		(do (println "obstacle found")
			true)
		false))

(defn set-new-pos [status get-fun axis stp]
	(let [dest (new-pos status get-fun stp)]
		(if (obstacle-in-destination? status axis dest) 
			(do (println "obstacle in" axis "=" dest)
				status)
			(assoc-in status [:pos axis] dest))))

(defn new-orientation-index [status stp]
	(mod (+ ((get-orient status) orientations) 
			(stp movements)) 
		turn-modulo-base))

(defn new-orientation [status stp]
	(get turns (new-orientation-index status stp)))

(defn set-new-orientation [status stp]
	(assoc-in status [:orient] (new-orientation status stp)))

(defn move [status stp]
	(println "stepping" stp)
	(case  #{(get-orient status) stp}
		(#{:N :f} #{:N :b} #{:S :f} #{:S :b}) 
			(set-new-pos status get-y :y stp)
		(#{:E :f} #{:E :b} #{:W :f} #{:W :b}) 
			(set-new-pos status get-x :x stp)
		(#{:N :l} #{:N :r} #{:S :l} #{:S :r} #{:W :l} #{:W :r} #{:E :l} #{:E :r}) 
			(set-new-orientation status stp)
		))

(defn do-commands [status commands]
	(println "commands" commands)
	(reduce move status commands))












