(ns clojurewerkz.quartzite.test.schedule.cron-test
  (:require [clojure.test :refer [deftest is]]
            [clojurewerkz.quartzite.schedule.cron :as cron])
  (:import (java.time OffsetDateTime ZoneOffset)
           (org.quartz DateBuilder)
           (org.quartz.impl.triggers CronTriggerImpl)
           (java.util Calendar TimeZone)))

(defn date-time
  ([year month day hour]
   (date-time year month day hour ZoneOffset/UTC))
  ([year month day hour ^ZoneOffset zone-offset]
   (OffsetDateTime/of (int year) (int month) (int day) (int hour) (int 0) (int 0) (int 0) zone-offset)))

(defn offset-date-time-to-calendar [^OffsetDateTime odt]
  (doto (Calendar/getInstance (TimeZone/getTimeZone "UTC"))
    (.setTimeInMillis (.toEpochMilli (.toInstant odt)))))

(deftest test-cron-schedule-dsl-example1
  (let [s     "0 0 12 15 * ?"
        d1     (date-time 2035 2 15 12)
        d2     (date-time 2035 2 16 12)
        ^TimeZone tz     (TimeZone/getTimeZone "Europe/Moscow")
        ^CronTriggerImpl sched  (cron/schedule
                                  (cron/cron-schedule s)
                                  (cron/in-time-zone tz)
                                  (cron/with-misfire-handling-instruction-ignore-misfires)
                                  (cron/finalize))]
    (is (= s (.getCronExpression sched)))
    (is (.willFireOn sched (offset-date-time-to-calendar d1) true))
    (is (not (.willFireOn sched (offset-date-time-to-calendar d2) true)))))


(deftest test-cron-schedule-dsl-example2
  (let [d1     (date-time 2035 2 15 15)
        d2     (date-time 2035 2 16 15)
        ^CronTriggerImpl sched (cron/schedule
                                 (cron/daily-at-hour-and-minute 15 0)
                                 (cron/in-time-zone (TimeZone/getTimeZone "UTC"))
                                 (cron/ignore-misfires)
                                 (cron/finalize))]
    (is (= "0 0 15 ? * *" (.getCronExpression sched)))
    (is (.willFireOn sched (offset-date-time-to-calendar d1) true))
    (is (.willFireOn sched (offset-date-time-to-calendar d2) true))))


(deftest test-cron-schedule-dsl-example3
  (let [d1     (date-time 2035 1  3  15)
        d2     (date-time 2035 1  2  15)
        d3     (date-time 2035 1  10 15)
        ^CronTriggerImpl sched (cron/schedule
                                 (cron/weekly-on-day-and-hour-and-minute DateBuilder/WEDNESDAY 15 0)
                                 (cron/in-time-zone (TimeZone/getTimeZone "UTC"))
                                 (cron/with-misfire-handling-instruction-do-nothing)
                                 (cron/finalize))]
    (is (= "0 0 15 ? * 4" (.getCronExpression sched)))
    (is (.willFireOn sched (offset-date-time-to-calendar d1) true))
    (is (not (.willFireOn sched (offset-date-time-to-calendar d2) true)))
    (is (.willFireOn sched (offset-date-time-to-calendar d3) true))))


(deftest test-cron-schedule-dsl-example4
  (let [d1     (date-time 2035 1  7  15)
        d2     (date-time 2035 1  3  15)
        ^CronTriggerImpl sched (cron/schedule
                                 (cron/monthly-on-day-and-hour-and-minute 7 15 0)
                                 (cron/in-time-zone (TimeZone/getTimeZone "UTC"))
                                 (cron/with-misfire-handling-instruction-fire-and-proceed)
                                 (cron/finalize))]
    (is (= "0 0 15 7 * ?" (.getCronExpression sched)))
    (is (.willFireOn sched (offset-date-time-to-calendar d1) true))
    (is (not (.willFireOn sched (offset-date-time-to-calendar d2) true)))))



(deftest test-cron-schedule-last-day-of-the-month
  (let [
        d1     (date-time 2035 1  7   0)
        d2     (date-time 2035 1  31  0)
        d3     (date-time 2035 2  28  0)
        d4     (date-time 2035 3  31  0)
        d5     (date-time 2035 4  30  0)
        d6     (date-time 2035 4  28  0)
        d7     (date-time 2036 2  29  0)
        d8     (date-time 2037 2  28  0)
        ^CronTriggerImpl sched (cron/schedule
                                (cron/cron-schedule "0 0 0 L * ?")
                                (cron/in-time-zone (TimeZone/getTimeZone "UTC"))
                                (cron/finalize))]
    (is (not (.willFireOn sched (offset-date-time-to-calendar d1) true)))
    (is (.willFireOn sched (offset-date-time-to-calendar d2) true))
    (is (.willFireOn sched (offset-date-time-to-calendar d3) true))
    (is (.willFireOn sched (offset-date-time-to-calendar d4) true))
    (is (.willFireOn sched (offset-date-time-to-calendar d5) true))
    (is (not (.willFireOn sched (offset-date-time-to-calendar d6) true)))
    (is (.willFireOn sched (offset-date-time-to-calendar d7) true))
    (is (.willFireOn sched (offset-date-time-to-calendar d8) true))))

(deftest test-cron-schedule-next-to-last-day-of-the-month
  (let [d1     (date-time 2035 1  7  15)
        d2     (date-time 2035 1  30  15)
        d3     (date-time 2035 2  27  15)
        d4     (date-time 2035 3  30  15)
        d5     (date-time 2035 4  29  15)
        d6     (date-time 2035 5  31  15)
        ^CronTriggerImpl sched (cron/schedule
                                (cron/cron-schedule "0 0 15 L-1 * ?")
                                (cron/in-time-zone (TimeZone/getTimeZone "UTC"))
                                (cron/finalize))]
    (is (not (.willFireOn sched (offset-date-time-to-calendar d1) true)))
    (is (.willFireOn sched (offset-date-time-to-calendar d2) true))
    (is (.willFireOn sched (offset-date-time-to-calendar d3) true))
    (is (.willFireOn sched (offset-date-time-to-calendar d4) true))
    (is (.willFireOn sched (offset-date-time-to-calendar d5) true))
    (is (not (.willFireOn sched (offset-date-time-to-calendar d6) true)))))
