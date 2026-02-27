(ns clj-kondo.clojurewerkz.quartzite
  (:require [clj-kondo.hooks-api :as api]))

(defn threaded* [builder-sym final-sym {:keys [node]}]
  (let [body (rest (:children node))]
    (let [new-node (api/list-node
                    (list*
                     (api/token-node '->)
                     (concat (when builder-sym
                               [(api/token-node builder-sym)])
                             body
                             (when final-sym
                               [(api/token-node final-sym)]))))]
      {:node new-node})))

(defn triggers-build [ctx]
  (threaded* '(org.quartz.TriggerBuilder/newTrigger)
             '(clojurewerkz.quartzite.triggers/finalize)
             ctx))

(defn jobs-build [ctx]
  (threaded* '(org.quartz.JobBuilder/newJob)
             '(clojurewerkz.quartzite.jobs/finalize)
             ctx))
