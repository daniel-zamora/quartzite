;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.quartzite.schedule.simple
  (:import (org.quartz SimpleScheduleBuilder)
           (org.quartz.spi MutableTrigger)))

(defn with-interval-in-milliseconds
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb ^long milliseconds]
  (.withIntervalInMilliseconds ssb milliseconds))

(defn with-interval-in-seconds
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb ^long seconds]
  (.withIntervalInSeconds ssb seconds))

(defn with-interval-in-minutes
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb ^long minutes]
  (.withIntervalInMinutes ssb minutes))

(defn with-interval-in-hours
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb ^long hours]
  (.withIntervalInHours ssb hours))

(defn with-interval-in-days
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb ^long days]
  (.withIntervalInHours ssb (* 24 days)))


(defn with-repeat-count
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb ^long l]
  (.withRepeatCount ssb l))

(defn repeat-forever
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.repeatForever ssb))



(defn with-misfire-handling-instruction-ignore-misfires
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionIgnoreMisfires ssb))

(defn ignore-misfires
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionIgnoreMisfires ssb))

(defn with-misfire-handling-instruction-fire-now
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionFireNow ssb))


(defn with-misfire-handling-instruction-next-with-existing-count
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionNextWithExistingCount ssb))

(defn next-with-existing-count
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionNextWithExistingCount ssb))


(defn with-misfire-handling-instruction-next-with-remaining-count
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionNextWithRemainingCount ssb))

(defn next-with-remaining-count
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionNextWithRemainingCount ssb))


(defn with-misfire-handling-instruction-now-with-existing-count
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionNowWithExistingCount ssb))

(defn now-with-existing-count
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionNowWithExistingCount ssb))


(defn with-misfire-handling-instruction-now-with-remaining-count
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionNowWithRemainingCount ssb))

(defn now-with-remaining-count
  ^SimpleScheduleBuilder [^SimpleScheduleBuilder ssb]
  (.withMisfireHandlingInstructionNowWithRemainingCount ssb))



(defn finalize
  ^MutableTrigger [^SimpleScheduleBuilder ssb]
  (.build ssb))

(defmacro schedule
  [& body]
  `(let [ssb# (SimpleScheduleBuilder/simpleSchedule)]
     ;; unlike job and trigger builder DSLs, we do not finalize
     ;; result here because this is how Quartz works. MK.
     (-> ssb# ~@body)))
