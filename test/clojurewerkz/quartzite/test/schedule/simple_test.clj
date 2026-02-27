(ns clojurewerkz.quartzite.test.schedule.simple-test
  (:require [clojure.test :refer [deftest is]]
            [clojurewerkz.quartzite.schedule.simple :as simple])
  (:import (org.quartz DateBuilder SimpleTrigger)
           (org.quartz.impl.triggers SimpleTriggerImpl)))


(deftest test-simple-schedule-dsl-example1
  (let [i     2
        n     10
        ^SimpleTriggerImpl sched (simple/schedule
                                   (simple/with-interval-in-seconds i)
                                   (simple/with-repeat-count        n)
                                   (simple/with-misfire-handling-instruction-ignore-misfires)
                                   (simple/finalize))]
    (is (= (* 1000 i) (.getRepeatInterval sched)))
    (is (= n          (.getRepeatCount    sched)))))


(deftest test-simple-schedule-dsl-example2
  (let [i     5
        n     10
        ^SimpleTriggerImpl sched (simple/schedule
                                   (simple/with-interval-in-milliseconds i)
                                   (simple/with-repeat-count        n)
                                   (simple/ignore-misfires)
                                   (simple/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= n (.getRepeatCount    sched)))))


(deftest test-simple-schedule-dsl-example3
  (let [i     3
        n     10
        ^SimpleTriggerImpl sched (simple/schedule
                                   (simple/with-interval-in-minutes i)
                                   (simple/with-repeat-count        n)
                                   (simple/next-with-remaining-count)
                                   (simple/finalize))]
    (is (= (* i (DateBuilder/MILLISECONDS_IN_MINUTE)) (.getRepeatInterval sched)))
    (is (= n (.getRepeatCount    sched)))))


(deftest test-simple-schedule-dsl-example4
  (let [i     333
        n     10
        ^SimpleTriggerImpl sched (simple/schedule
                                   (simple/with-interval-in-hours i)
                                   (simple/with-repeat-count      n)
                                   (simple/now-with-remaining-count)
                                   (simple/finalize))]
    (is (= (* i (DateBuilder/MILLISECONDS_IN_HOUR)) (.getRepeatInterval sched)))
    (is (= n (.getRepeatCount    sched)))))


(deftest test-simple-schedule-dsl-example5
  (let [i       4
        ^SimpleTriggerImpl sched (simple/schedule
                                   (simple/with-interval-in-hours i)
                                   (simple/repeat-forever)
                                   (simple/now-with-existing-count)
                                   (simple/finalize))]
    (is (= (* i (DateBuilder/MILLISECONDS_IN_HOUR)) (.getRepeatInterval sched)))
    (is (= (SimpleTrigger/REPEAT_INDEFINITELY) (.getRepeatCount sched)))))


(deftest test-simple-schedule-dsl-example6
  (let [i       3
        ^SimpleTriggerImpl sched (simple/schedule
                                   (simple/with-interval-in-days i)
                                   (simple/next-with-existing-count)
                                   (simple/repeat-forever)
                                   (simple/finalize))]
    (is (= (* 24 i (DateBuilder/MILLISECONDS_IN_HOUR)) (.getRepeatInterval sched)))))
