(ns build-monitor-clj.reducer-test
  (:require [midje.sweet :refer :all]
            [build-monitor-clj.reducer :as subject]
            [build-monitor-clj.properties :refer :all]))

(defn project
  [overrides] (merge {:name      "project1"
                      :stage     "stage1"
                      :job       "job1"
                      :prognosis "healthy"} overrides))

(facts "aggregates project results"
       (fact "when both have same prognosis, only one is returned"
             (subject/aggregate [(project {:stage "stage1"})
                                 (project {:stage "stage2"})])
             => [(project {:stage "stage1"})])

       (fact "different prognosis return higher priority"
             (subject/aggregate [(project {:prognosis "sick"})
                                 (project {:prognosis "healthy"})])
             => [(project {:prognosis "sick"})])

       (fact "projects with different names are both returned"
             (subject/aggregate [(project {:name "one"})
                                 (project {:name "two"})])
             => [(project {:name "one"})
                 (project {:name "two"})]))


(fact "only includes included projects"
      (subject/filter-for-white-listed-projects [{:name "foo"}
                         {:name "bar"}]) => [{:name "foo"}]
      (provided
        (included-projects) => ["foo"]))