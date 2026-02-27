;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.quartzite.schedule.daily-interval
  (:import (org.quartz DailyTimeIntervalScheduleBuilder TimeOfDay)
           (java.util Set)
           (org.quartz.spi MutableTrigger)))

(defn with-interval-in-seconds
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb ^long seconds]
  (.withIntervalInSeconds dtisb seconds))

(defn with-interval-in-minutes
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb ^long minutes]
  (.withIntervalInMinutes dtisb minutes))

(defn with-interval-in-hours
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb ^long hours]
  (.withIntervalInHours dtisb hours))

(defn with-interval-in-days
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb ^long days]
  (.withIntervalInHours dtisb (* 24 days)))


(defn with-repeat-count
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb ^long l]
  (.withRepeatCount dtisb l))


(defn on-every-day
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb]
  (.onEveryDay dtisb))

(defn every-day
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb]
  (on-every-day dtisb))



(defn on-days-of-the-week
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb ^Set days]
  (.onDaysOfTheWeek dtisb days))

(defn days-of-the-week
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb ^Set days]
  (on-days-of-the-week dtisb days))



(defn on-monday-through-friday
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb]
  (.onMondayThroughFriday dtisb))

(defn monday-through-friday
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb]
  (on-monday-through-friday dtisb))


(defn on-saturday-and-sunday
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb]
  (.onSaturdayAndSunday dtisb))

(defn saturday-and-sunday
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb]
  (on-saturday-and-sunday dtisb))


(defn time-of-day
  ^TimeOfDay [^long hours ^long minutes ^long seconds]
  (TimeOfDay. hours minutes seconds))

(defn starting-daily-at
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb ^TimeOfDay at]
  (.startingDailyAt dtisb at))

(defn ending-daily-at
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb ^TimeOfDay at]
  (.endingDailyAt dtisb at))



(defn with-misfire-handling-instruction-ignore-misfires
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb]
  (.withMisfireHandlingInstructionIgnoreMisfires dtisb))

(defn ignore-misfires
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb]
  (.withMisfireHandlingInstructionIgnoreMisfires dtisb))

(defn with-misfire-handling-instruction-fire-and-proceed
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder dtisb]
  (.withMisfireHandlingInstructionFireAndProceed dtisb))

(defn with-misfire-handling-instruction-do-nothing
  ^DailyTimeIntervalScheduleBuilder [^DailyTimeIntervalScheduleBuilder cisb]
  (.withMisfireHandlingInstructionDoNothing cisb))



(defn finalize
  ^MutableTrigger [^DailyTimeIntervalScheduleBuilder dtisb]
  (.build dtisb))

(defmacro schedule
  [& body]
  `(let [dtisb# (DailyTimeIntervalScheduleBuilder/dailyTimeIntervalSchedule)]
     (-> dtisb# ~@body)))
