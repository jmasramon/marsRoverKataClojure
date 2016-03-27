(ns mars-rover-kata.stateless-mars-rover-test
  (:require [clojure.test :refer :all]
            [mars-rover-kata.stateless-mars-rover :refer :all]))

(deftest correct-orientation?-test
  (testing "correct-orientation?"
    (is (correct-orientation? :S))
    (is (correct-orientation? :N))
    (is (correct-orientation? :W))
    (is (correct-orientation? :E))
    (is (not (correct-orientation? :K)))))

(deftest move-forwards
  (testing "Move forwards."
    (is (= {:pos {:x 0 :y 1} :orient :N} (do-commands {:pos {:x 0 :y 0} :orient :N} '(:f))))
    (is (= {:pos {:x 0 :y 9} :orient :S} (do-commands {:pos {:x 0 :y 0} :orient :S} '(:f))))))

(deftest move-backwards
  (testing "Move backwards."
    (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands {:pos {:x 0 :y 1} :orient :N} '(:b))))
    (is (= {:pos {:x 0 :y 2} :orient :S} (do-commands {:pos {:x 0 :y 1} :orient :S} '(:b))))))

(deftest turn-left
  (testing "Turn left."
    (is (= {:pos {:x 0 :y 0} :orient :W} (do-commands {:pos {:x 0 :y 0} :orient :N} '(:l))))
    (is (= {:pos {:x 0 :y 0} :orient :S} (do-commands {:pos {:x 0 :y 0} :orient :W} '(:l))))
    (is (= {:pos {:x 0 :y 0} :orient :E} (do-commands {:pos {:x 0 :y 0} :orient :S} '(:l))))
    (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands {:pos {:x 0 :y 0} :orient :E} '(:l))))
))

(deftest turn-right
  (testing "Turn right."
    (is (= {:pos {:x 0 :y 0} :orient :E} (do-commands {:pos {:x 0 :y 0} :orient :N} '(:r))))
    (is (= {:pos {:x 0 :y 0} :orient :S} (do-commands {:pos {:x 0 :y 0} :orient :E} '(:r))))
    (is (= {:pos {:x 0 :y 0} :orient :W} (do-commands {:pos {:x 0 :y 0} :orient :S} '(:r))))
    (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands {:pos {:x 0 :y 0} :orient :W} '(:r))))
))

(deftest mixed-orders
	(testing "Mixed string of orders.")
    (is (= {:pos {:x 1 :y 0} :orient :S} (do-commands {:pos {:x 0 :y 0} :orient :N} '(:f :r :f :r :f))))
    (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands {:pos {:x 1 :y 0} :orient :S} '(:b :l :b :l :b))))
    (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands {:pos {:x 0 :y 0} :orient :N} '(:f :b :b :f))))
    (is (= {:pos {:x 2 :y 2} :orient :N} (do-commands {:pos {:x 0 :y 0} :orient :N} '(:r :f :f :l :f :f))))
)  

(deftest wrapping
	(testing "Wrapping to an spheric planet.")
    (is (= {:pos {:x 1 :y 9} :orient :N} (do-commands {:pos {:x 0 :y 0} :orient :N} '(:f :b :r :f :l :b))))
    (is (= {:pos {:x 9 :y 9} :orient :W} (do-commands {:pos {:x 1 :y 9} :orient :N} '(:l :f :f))))
)

(deftest obstacles-in-the-way
	(testing "Finding obstacles in the way.")
    (is (= {:pos {:x 0 :y 2} :orient :E} (do-commands {:pos {:x 0 :y 0} :orient :N} '(:f :f :r :f :f))))
)











