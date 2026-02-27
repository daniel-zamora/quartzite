(ns clojurewerkz.quartzite.test.schedule.daily-interval-test
  (:require [clojure.test :refer [deftest is]]
            [clojurewerkz.quartzite.schedule.daily-interval :as sdi])
  (:import (org.quartz DateBuilder$IntervalUnit SimpleTrigger)
           (org.quartz.impl.triggers DailyTimeIntervalTriggerImpl)))


(deftest test-daily-interval-schedule-dsl-example1
  (let [i     2
        n     10
        ^DailyTimeIntervalTriggerImpl sched (sdi/schedule
                (sdi/with-interval-in-seconds i)
                (sdi/with-repeat-count        n)
                (sdi/on-days-of-the-week (set [(Integer/valueOf 1) (Integer/valueOf 2) (Integer/valueOf 3) (Integer/valueOf 4)]))
                (sdi/with-misfire-handling-instruction-ignore-misfires)
                (sdi/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/SECOND (.getRepeatIntervalUnit sched)))
    (is (= n          (.getRepeatCount    sched)))))


(deftest test-daily-interval-schedule-dsl-example2
  (let [i     5
        n     10
        ^DailyTimeIntervalTriggerImpl sched (sdi/schedule
                (sdi/with-interval-in-seconds i)
                (sdi/with-repeat-count        n)
                (sdi/monday-through-friday)
                (sdi/starting-daily-at (sdi/time-of-day 15 00 00))
                (sdi/ending-daily-at (sdi/time-of-day 15 00 00))
                (sdi/ignore-misfires)
                (sdi/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/SECOND (.getRepeatIntervalUnit sched)))
    (is (= n (.getRepeatCount    sched)))))


(deftest test-daily-interval-schedule-dsl-example3
  (let [i     3
        n     10
        ^DailyTimeIntervalTriggerImpl sched (sdi/schedule
                (sdi/with-interval-in-minutes i)
                (sdi/with-repeat-count        n)
                (sdi/saturday-and-sunday)
                (sdi/with-misfire-handling-instruction-fire-and-proceed)
                (sdi/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/MINUTE (.getRepeatIntervalUnit sched)))
    (is (= n (.getRepeatCount    sched)))))


(deftest test-daily-interval-schedule-dsl-example4
  (let [i     333
        n     10
        ^DailyTimeIntervalTriggerImpl sched (sdi/schedule
                (sdi/with-interval-in-hours i)
                (sdi/with-repeat-count      n)
                (sdi/every-day)
                (sdi/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/HOUR (.getRepeatIntervalUnit sched)))
    (is (= n (.getRepeatCount    sched)))))


(deftest test-daily-interval-schedule-dsl-example5
  (let [i       4
        ^DailyTimeIntervalTriggerImpl sched (sdi/schedule
                (sdi/with-interval-in-hours i)
                (sdi/on-saturday-and-sunday)
                (sdi/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= (SimpleTrigger/REPEAT_INDEFINITELY) (.getRepeatCount sched)))))


(deftest test-daily-interval-schedule-dsl-example6
  (let [i       3
        ^DailyTimeIntervalTriggerImpl sched (sdi/schedule
                (sdi/with-interval-in-days i)
                (sdi/on-monday-through-friday)
                (sdi/finalize))]
    (is (= (* 24 i) (.getRepeatInterval sched)))))
