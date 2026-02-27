(ns clojurewerkz.quartzite.test.schedule.calendar-interval-test
  (:require [clojure.test :refer [deftest is]]
            [clojurewerkz.quartzite.schedule.calendar-interval :as calendar-interval])
  (:import (org.quartz DateBuilder$IntervalUnit)
           (org.quartz.impl.triggers CalendarIntervalTriggerImpl)))


(deftest test-calendar-interval-schedule-dsl-example1
  (let [i     2
        ^CalendarIntervalTriggerImpl sched (calendar-interval/schedule
                                              (calendar-interval/with-interval-in-seconds i)
                                              (calendar-interval/with-misfire-handling-instruction-ignore-misfires)
                                              (calendar-interval/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/SECOND (.getRepeatIntervalUnit sched)))))


(deftest test-calendar-interval-schedule-dsl-example2
  (let [i     5
        ^CalendarIntervalTriggerImpl sched (calendar-interval/schedule
                                              (calendar-interval/with-interval-in-seconds i)
                                              (calendar-interval/ignore-misfires)
                                              (calendar-interval/finalize))]
    (is (= i (.getRepeatInterval sched)))))


(deftest test-calendar-interval-schedule-dsl-example3
  (let [i     3
        ^CalendarIntervalTriggerImpl sched (calendar-interval/schedule
                                              (calendar-interval/with-interval-in-minutes i)
                                              (calendar-interval/with-misfire-handling-instruction-fire-and-proceed)
                                              (calendar-interval/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/MINUTE (.getRepeatIntervalUnit sched)))))


(deftest test-calendar-interval-schedule-dsl-example4
  (let [i     333
        ^CalendarIntervalTriggerImpl sched (calendar-interval/schedule
                                              (calendar-interval/with-interval-in-hours i)
                                              (calendar-interval/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/HOUR (.getRepeatIntervalUnit sched)))))


(deftest test-calendar-interval-schedule-dsl-example5
  (let [i       4
        ^CalendarIntervalTriggerImpl sched (calendar-interval/schedule
                                              (calendar-interval/with-interval-in-days i)
                                              (calendar-interval/with-misfire-handling-instruction-do-nothing)
                                              (calendar-interval/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/DAY (.getRepeatIntervalUnit sched)))))


(deftest test-calendar-interval-schedule-dsl-example6
  (let [i       3
        ^CalendarIntervalTriggerImpl sched (calendar-interval/schedule
                                              (calendar-interval/with-interval-in-weeks i)
                                              (calendar-interval/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/WEEK (.getRepeatIntervalUnit sched)))))


(deftest test-calendar-interval-schedule-dsl-example7
  (let [i       3
        ^CalendarIntervalTriggerImpl sched (calendar-interval/schedule
                                              (calendar-interval/with-interval-in-months i)
                                              (calendar-interval/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/MONTH (.getRepeatIntervalUnit sched)))))

(deftest test-calendar-interval-schedule-dsl-example8
  (let [i       3
        ^CalendarIntervalTriggerImpl sched (calendar-interval/schedule
                                              (calendar-interval/with-interval-in-years i)
                                              (calendar-interval/finalize))]
    (is (= i (.getRepeatInterval sched)))
    (is (= DateBuilder$IntervalUnit/YEAR (.getRepeatIntervalUnit sched)))))
