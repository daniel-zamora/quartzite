(defproject gov.nasa.earthdata/quartzite "2.2.1-SNAPSHOT"
  :description "Quarzite is a thin Clojure layer on top the Quartz Scheduler"
  :min-lein-version "2.5.1"
  :license {:name "Eclipse Public License"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 ;; 2.5.0 Has a bug that should be fixed before
                 ;; updating https://github.com/quartz-scheduler/quartz/issues/1298
                 [org.quartz-scheduler/quartz "2.4.0"]]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :test-selectors {:all     (constantly true)
                   :focus   :focus
                   :default (constantly true)}
  :profiles {:1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0"]]}
             :1.10 {:dependencies [[org.clojure/clojure "1.10.3"]]}
             :1.11 {:dependencies [[org.clojure/clojure "1.11.4"]]}
             :1.12 {:dependencies [[org.clojure/clojure "1.12.0"]]}
             :dev {:resource-paths ["test/resources" "resources"]
                   :dependencies [[org.clojure/tools.logging "1.3.0" :exclusions [org.clojure/clojure]]
                                  [org.slf4j/slf4j-log4j12   "2.0.16"]]}}
  :aliases {"all" ["with-profile" "dev:dev,1.12:dev,master"]}
  :repositories {"sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail :update :always}}
                 "sonatype-snapshots" {:url "http://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}}
  :global-vars {*warn-on-reflection* true}
  :mailing-list {:name "clojure-quartz"
                 :archive "https://groups.google.com/group/clojure-quartz"
                 :post "clojure-quartz@googlegroups.com"}
  :plugins [[codox "0.10.8"]]
  :codox {:sources ["src/clojure"]
          :output-dir "doc/api"})
